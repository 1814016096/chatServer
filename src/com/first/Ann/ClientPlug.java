package com.first.Ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 原初
 * @create 2021 - 11 - 07
 * 客户端用的
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ClientPlug {
    //?
    String name();
}
