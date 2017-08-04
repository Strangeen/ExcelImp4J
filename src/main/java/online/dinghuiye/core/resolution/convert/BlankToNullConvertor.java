package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.convert.Convertor;
import online.dinghuiye.core.annotation.convert.BlankToNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Strangeen on 2017/7/1.
 * <p>
 * 空串或null转换为null
 */
public class BlankToNullConvertor implements Convertor {

    private static final Logger logger = LoggerFactory.getLogger(BlankToNullConvertor.class);

    @Override
    public Object convert(Object obj, Field field, Map<String, String> excelRecordMap) {

        logger.trace(new StringBuffer()
                .append("field: ").append(field.getName())
                .append(", obj: ").append(obj)
                .append(" run BlankToNullConvertor").toString());

        if (obj == null) return obj;
        if (StringUtils.isBlank(obj.toString())) return null;
        //return ConvertFactory.convertToType(field, obj);
        return obj;
    }
}
