package org.avalon.rainbow.common.aop.validation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresValidation {

    ValidationType value() default ValidationType.ANNOTATION;

    String profile() default "";
}
