package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.core.resolution.convert.ConstValueConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/8/3.
 *
 * POJO属性为常量值的转换注解标注
 *  比如：用户的启用和停用，导入数据时并没有传入值，需要手动设置常量值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(ConstValueConvertor.class)
public @interface ConstValue {

    /**
     * @return 常量值
     */
    String value();
}
