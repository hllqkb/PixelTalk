package com.pixeltalk.annotation;

import com.pixeltalk.constant.LimitKeyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hllqk
 * @data 2025年1月31日22:41:47
 * @description 限制url访问次数的注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UrlLimit {
    LimitKeyType keyType() default LimitKeyType.ID;
    int maxRequest() default 60;
}
