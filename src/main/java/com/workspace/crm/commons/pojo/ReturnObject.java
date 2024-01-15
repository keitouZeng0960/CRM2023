package com.workspace.crm.commons.pojo;

public class ReturnObject {
    //处理成功或失败的标记，1是成功，0是失败
    private String code;

    //提示信息
    private String message;
    //其它属性
    private Object retData;

    public ReturnObject(String code, String message, Object retData) {
        this.code = code;
        this.message = message;
        this.retData = retData;
    }

    public ReturnObject(){};
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
