package online.dinghuiye.core.validation;

import online.dinghuiye.api.entity.Process;
import online.dinghuiye.api.entity.*;
import online.dinghuiye.api.validation.RowRecordValidator;
import online.dinghuiye.core.resolution.torowrecord.RowRecordKit;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>hibernate validatior实现验证</p>
 *
 * @author Strangeen on 2017/8/3
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class RowRecordValidatorImpl implements RowRecordValidator {

    private Validator validator;

    public RowRecordValidatorImpl() {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
    }

    @Override
    public boolean valid(List<RowRecord> rowRecordList, Process process) {

        if (process != null)
            process.setNode(ProcessNode.VALIDATION);
        boolean allSuccess = true;
        for (RowRecord rowRecord : rowRecordList) {
            if (!valid(rowRecord)) allSuccess = false;
            if (process != null)
                process.updateProcess(1);
        }
        return allSuccess;
    }


    /**
     * 验证单个{@link RowRecord}，验证失败文本格式为：“Excel表头名称 + message”
     *
     * @param rowRecord {@link RowRecord}
     * @return true - 验证成功<br>
     *         false - 验证失败，失败消息存入{@link RowRecordHandleResult#msg}
     */
    @Override
    public boolean valid(RowRecord rowRecord) {

        // 解析成功的才进行验证
        if (rowRecord.getResult() != null &&
                rowRecord.getResult().getResult() != ResultStatus.SUCCESS)
            return false; // 解析不成功，不进行验证，标记为不成功

        Map<Class<?>, Object> pojoObjMap = rowRecord.getPojoRecordMap();
        for (Map.Entry<Class<?>, Object> pojoObjEntry : pojoObjMap.entrySet()) {

            // hibernate validator验证
            Set<ConstraintViolation<Object>> validResSet =
                    validator.validate(pojoObjEntry.getValue(),
                            // 限定groups，避免repaire了POJO对象后，hibernate存储前检查可能失败而报错的情况
                            online.dinghuiye.api.validation.Validator.class, Default.class);
            if (validResSet.size() > 0) {
                StringBuilder msg = new StringBuilder();
                for (ConstraintViolation<Object> cv : validResSet) {
                    msg.append(
                            // TODO 后期设置可以配置是否显示表头名称
                            RowRecordKit.getSheetTitleNameByField(
                                    ValidateKit.getConstraintViolationField(pojoObjEntry.getKey(), cv))
                    )
                            .append(cv.getMessage()).append(";");
                }
                rowRecord.getResult().setResult(ResultStatus.FAIL).setMsg(msg.toString());
            }

            // @Validate复杂验证
            Map<Field, String> complexValidRes = ValidateKit.validate(pojoObjEntry.getValue());
            if (complexValidRes.size() > 0) {
                StringBuilder newMsg = new StringBuilder();
                String msg = rowRecord.getResult().getMsg();
                if (msg != null) newMsg.append(msg);

                for (Map.Entry<Field, String> fieldMsg : complexValidRes.entrySet()) {
                    // TODO 后期设置可以配置是否显示表头名称
                    newMsg.append(RowRecordKit.getSheetTitleNameByField(fieldMsg.getKey()))
                            .append(fieldMsg.getValue())
                            .append(";");
                }
                rowRecord.getResult().setResult(ResultStatus.FAIL).setMsg(newMsg.toString());
            }
        }
        return rowRecord.getResult().getResult() == ResultStatus.SUCCESS;
    }
}
