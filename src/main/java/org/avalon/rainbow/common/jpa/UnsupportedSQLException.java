package org.avalon.rainbow.common.jpa;

public class UnsupportedSQLException extends RuntimeException {

    public UnsupportedSQLException() {
        super();
    }

    public UnsupportedSQLException(String msg) {
        super(msg);
    }
}
