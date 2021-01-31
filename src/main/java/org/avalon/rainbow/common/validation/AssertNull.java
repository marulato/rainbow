package org.avalon.rainbow.common.validation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertNull {

    String value();

    String message() default "";

    String errorCode() default "err.default";

    String[] profile() default {};
}
