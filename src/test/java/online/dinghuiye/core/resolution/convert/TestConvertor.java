package online.dinghuiye.core.resolution.convert;

import online.dinghuiye.api.resolution.Convertor;
import online.dinghuiye.core.resolution.convert.testcase.ListValueConvertor;
import online.dinghuiye.core.resolution.convert.testcase.User;
import online.dinghuiye.core.resolution.convert.testcase.Util;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Strangeen on 2017/7/2
 *
 * @author Strangeen on 2017/9/3
 * @version 2.1.0
 */
public class TestConvertor {

    private Field field;
    private List<Convertor> convertorList;
    private Object obj;

    @Test
    public void testConvert() throws NoSuchFieldException, ParseException {

        // @ValueConver多重Convert注解
        // @BlankToNull
        {
            field = User.class.getDeclaredField("name");
            convertorList = ConvertorFactory.getConvertors(field);
            obj = "   ";
            for (Convertor c : convertorList)
                obj = c.convert(obj, field, null);
            Assert.assertEquals(null, obj);
        }

        // @ValueMap
        {
            field = User.class.getDeclaredField("sex");
            convertorList = ConvertorFactory.getConvertors(field);
            obj = 0;
            for (Convertor c : convertorList)
                obj = c.convert(obj, field, null);
            Assert.assertEquals("female", obj);
        }

        // @DateFormat
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            field = User.class.getDeclaredField("birthday");
            convertorList = ConvertorFactory.getConvertors(field);
            obj = "1988-08-21 10:10:10";
            Date date = sdf.parse(obj.toString());
            for (Convertor c : convertorList)
                obj = c.convert(obj, field, null);
            Assert.assertEquals(date, obj);
        }

        // @ValueConver自定义Convertor
        {
            field = User.class.getDeclaredField("scoreList");
            convertorList = ConvertorFactory.getConvertors(field);
            obj = "15,16,17,18";
            for (Convertor c : convertorList)
                obj = c.convert(obj, field, null);
            List<Integer> list = new ArrayList<>();
            list.add(15);
            list.add(16);
            list.add(17);
            list.add(18);
            Assert.assertEquals(true, Util.listEquals(list, (List<?>) obj,
                    new ListValueConvertor() {
                        @Override
                        public Object change(Object obj) {
                            return obj;
                        }
                    })
            );
        }
    }


    @Test
    // @ConstValue
    // @DateFormat
    // 复合convertor
    public void testMultiConvertor() throws NoSuchFieldException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        field = User.class.getDeclaredField("modifyTime");
        convertorList = ConvertorFactory.getConvertors(field);
        obj = null;
        for (Convertor c : convertorList)
            obj = c.convert(obj, field, null);
        Assert.assertEquals(sdf.parse("2017-12-12"), obj);
    }
}
