package com.jd.databaseDocer.entity;

/**
 * Created by yanghui10 on 2016/8/20.
 */
public class DBInfo {
    private String dbName;
    private String url;
    private String password;
    private String user;

    public DBInfo(String dbName, String url, String password, String user) {
        this.dbName = dbName;
        this.url = url;
        this.password = password;
        this.user = user;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DBInfo{" +
                "url='" + url + '\'' +
                ", password='" + password + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
