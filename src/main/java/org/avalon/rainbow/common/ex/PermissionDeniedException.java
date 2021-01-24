package org.avalon.rainbow.common.ex;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {}

    public PermissionDeniedException(String msg) {
        super(msg);
    }
}
