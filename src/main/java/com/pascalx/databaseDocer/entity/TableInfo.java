package com.pascalx.databaseDocer.entity;

import java.util.List;

/**
 * Created by yanghui10 on 2016/8/20.
 */
public class TableInfo {
    private String tableName;
    private String tableComment;

    private List<ColumnInfo> columnInfos;

    public TableInfo(String tableName, String tableComment) {
        this.tableName = tableName;
        this.tableComment = tableComment;
    }

    public List<ColumnInfo> getColumnInfo() {
        return columnInfos;
    }

    public void setColumnInfo(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    @Override
    public String toString() {
        return this.getTableName()+"\t"+this.getTableComment()+"\t\n";
    }
}
