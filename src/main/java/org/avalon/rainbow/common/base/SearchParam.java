package org.avalon.rainbow.common.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchParam implements Serializable {

    private Integer pageNo;
    private Integer pageSize;
    private Integer orderColumnNo;
    private String order;
    private Integer draw;
    private Map<String, Object> params;
    private Class<?> type;

    @Override
    public String toString() {
        return "SearchParam{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", orderColumnNo=" + orderColumnNo +
                ", order='" + order + '\'' +
                ", draw=" + draw +
                ", params=" + params +
                '}';
    }


    public void addParam(String name, Object param) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(name, param);
    }

    public Object getParam(String property) {
        if (params != null) {
            return params.get(property);
        }
        return null;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if (pageNo >= 0) {
            this.pageNo = pageNo;
            if (pageNo == 1) {
                this.pageNo = 0;
            }
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOrderColumnNo() {
        return orderColumnNo;
    }

    public void setOrderColumnNo(Integer orderColumnNo) {
        this.orderColumnNo = orderColumnNo;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order != null ? order.toUpperCase() : null;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
