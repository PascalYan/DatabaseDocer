package com.jd.databaseDocer.entity;

/**
 * 备注信息
 * Created by yanghui10 on 2016/8/22.
 */
public class RemarkInfo{
    private int code;
    private String desc;

    public RemarkInfo(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
