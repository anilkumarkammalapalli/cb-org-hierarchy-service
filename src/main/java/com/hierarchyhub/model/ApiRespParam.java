package com.hierarchyhub.model;

public class ApiRespParam {

    private String resmsgid;
    private String msgid;
    private String err;
    private String status;
    private String errmsg;

    public ApiRespParam() {
    }

    public ApiRespParam(String id) {
        resmsgid = id;
        msgid = id;
    }

    public String getResmsgid() {
        return resmsgid;
    }

    public void setResmsgid(String resmsgid) {
        this.resmsgid = resmsgid;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }



}
