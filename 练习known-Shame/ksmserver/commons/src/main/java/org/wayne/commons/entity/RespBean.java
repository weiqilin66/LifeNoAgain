package org.wayne.commons.entity;

/**
 * @Description:
 * @author: LinWeiQi
 */

public class RespBean {
    private Integer status; // code
    private String message;
    private Object data;

    public static RespBean build(){
        return new RespBean();
    }
    public RespBean() {
    }

    public RespBean(Integer status, String message, Object obj) {
        this.status = status;
        this.message = message;
        this.data = obj;
    }
    public static RespBean ok(String msg) {
        return new RespBean(200, msg, null);
    }
    public static RespBean ok() {
        return new RespBean(200, null, null);
    }
    public static RespBean ok( Object o) {
        return new RespBean(200, null, o);
    }
    public static RespBean error() {
        return new RespBean(500, null, null);
    }
    public static RespBean ok(String msg, Object o) {
        return new RespBean(200, msg, o);
    }

    public static RespBean error(String msg) {
        return new RespBean(500, msg, null);
    }

    public static RespBean error(String msg, Object o) {
        return new RespBean(500, msg, o);
    }

    public Integer getStatus() {
        return status;
    }

    public RespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RespBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RespBean setData(Object data) {
        this.data = data;
        return this;
    }
}
