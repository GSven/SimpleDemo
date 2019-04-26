package com.gsven.library.utils.praserutils;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author admin
 * @Date 2019/4/26 14:40
 * @Desc 给实体类属性标识其对应的服务器json数据的key的注解
 * * 在实体类中的各个属性上加上注解，标识其在服务器返回的jsonObject中对应的key。
 * * 然后再在我们的json解析工具类{@link JsonParseUtils}中，根据这些注解去解析json数据，生成实体类对象。
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonKey {

    /**
     * 与注解下的属性对应的服务器json key
     */
    String value();
}
