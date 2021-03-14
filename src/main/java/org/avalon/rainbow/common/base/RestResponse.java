package org.avalon.rainbow.common.base;

import org.avalon.rainbow.common.constant.AppConst;

import java.io.Serializable;

public class RestResponse implements Serializable {

    private String status;
    private String response;
    private Object result;

    public RestResponse() {
        this.status = AppConst.RESPONSE_OK;
    }

    public RestResponse(String status, Object result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
