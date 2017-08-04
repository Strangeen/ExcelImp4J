package online.dinghuiye.core.annotation.convert;

import online.dinghuiye.core.consts.Consts;
import online.dinghuiye.core.resolution.convert.ValueMapConvertor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Strangeen on 2017/6/27.<br>
 * <br>
 * 按json将值对应的key转换为value<br>
 * 注解用于少量数据形式转换，否则使用{@link ValueConvert}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Convert(ValueMapConvertor.class)
public @interface ValueMap {

    /**
     * @return json格式的转换mapping<br>
     *     如：excel中“性别”字段值为“男”和“女”，注解value="{'男':1, '女'：0}"时，
     *     将在解析为POJO过程中将“男”置为1，“女”置为0<br>
     *     注意：json解析利用fastjson完成，格式可以为：<br>
     *         1. value="{'男':1, '女'：'female'}" （推荐）<br>
     *         2. value="{男:1, 女：'female'}" （推荐）<br>
     *         3. value="{\"男\":1, \"女\"：\"female\"}" <br>
     *
     */
    String value();
}
