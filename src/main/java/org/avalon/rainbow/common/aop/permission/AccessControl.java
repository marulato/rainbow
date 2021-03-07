package org.avalon.rainbow.common.aop.permission;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessControl {

    String module();

    String function();

    String operation();

}
