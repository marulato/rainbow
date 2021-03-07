package org.avalon.rainbow.common.ex;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {}

    public AccessDeniedException(String msg) {
        super(msg);
    }
}
