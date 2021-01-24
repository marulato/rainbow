package org.avalon.rainbow.common.aop.permission;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRoles {
    String[] value();
    Logical logical() default Logical.OR;
}
