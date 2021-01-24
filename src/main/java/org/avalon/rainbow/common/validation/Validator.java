package org.avalon.rainbow.common.validation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Validator {

    Class<? extends AbstractValidator> value();
}
