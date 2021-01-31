package org.avalon.rainbow.common.utils;

public class NumberUtils {

    public static boolean isPositive(Long id) {
        return id != null && id > 0;
    }

    public static boolean isPositive(Integer id) {
        return id != null && id > 0;
    }

    public static boolean isZero(Long id) {
        return id != null && id == 0;
    }

    public static boolean isInvalidId(Long id) {
        return !isPositive(id);
    }
}
