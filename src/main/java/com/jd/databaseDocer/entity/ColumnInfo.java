package com.jd.databaseDocer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanghui10 on 2016/8/20.
 */
public class ColumnInfo {
    private String name;
    private String comment;//注释
    private String type;
    private String isNullable;
    private String key;
    private String defaultValue;
    private String extra;

    private List<RemarkInfo> remarkInfos;


    public ColumnInfo(){

    }

    public void setRemarkInfos(List<RemarkInfo> remarkInfos) {
        this.remarkInfos = remarkInfos;
    }

    public ColumnInfo(String name, String comment, String type, String isNullable, String key, String defaultValue, String extra) {
        this.name = name;
        this.comment = comment;
        this.type = type;
        this.isNullable = isNullable;
        this.key = key;
        this.defaultValue = defaultValue;
        this.extra = extra;
    }

    public List<RemarkInfo> getRemarkInfos() {
        return remarkInfos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return  name + ' ' +
                comment + ' ' +
                type + ' ' +
                isNullable + ' ' +
                key + ' ' +
                defaultValue + ' ' +
                extra + "\n" ;
    }
}
