package org.avalon.rainbow.common.aop.validation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

    ValidationType value() default ValidationType.ANNOTATION;

    String profile() default "";
}
