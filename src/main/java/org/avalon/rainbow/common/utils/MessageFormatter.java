package org.avalon.rainbow.common.utils;

public final class MessageFormatter {

    public static String getErrorMsg(String errorMsg) {
        if (StringUtils.isNotBlank(errorMsg)) {
            if (errorMsg.equals("mandatory")) {
                return "不能为空，请填写相关信息";
            } else if (errorMsg.startsWith("length[") && errorMsg.length() > 7) {
                String ss = errorMsg.substring(7, errorMsg.length() - 1);
                String[] range = ss.split(",");
                if (range.length == 2 && StringUtils.isInteger(range[0]) && StringUtils.isInteger(range[1])) {
                    int min = Integer.parseInt(range[0]);
                    int max = Integer.parseInt(range[1]);
                    return "长度只能为" + min + "~" + max + "个字符";
                }
            } else if (errorMsg.matches("^E[0-9]{3,}")) {

            } else {
                return errorMsg;
            }
        }
        return null;
    }

}
