package com.dysy.bysj.common.annotation;


import com.dysy.bysj.common.enums.Logical;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface RequirePermissions {
    String[] value();
    Logical logical() default Logical.AND;
}
