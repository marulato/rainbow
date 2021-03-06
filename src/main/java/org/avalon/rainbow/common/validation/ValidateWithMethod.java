package org.avalon.rainbow.common.validation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateWithMethod {

    Class<?> type() default void.class;

    String methodName();

    String[] parameters() default {};

    String message() default "";

    String errorCode() default "err.default";

    String[] profile() default {};

    @Target(ElementType.FIELD)
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @interface Group {
        ValidateWithMethod[] value();
    }
}
