package org.avalon.rainbow.common.validation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Date {

    String pattern();

    String message();

    String[] profile() default {};
}
