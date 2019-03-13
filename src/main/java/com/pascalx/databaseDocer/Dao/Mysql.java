package com.pascalx.databaseDocer.Dao;

import com.pascalx.databaseDocer.entity.ColumnInfo;
import com.pascalx.databaseDocer.entity.DBInfo;
import com.pascalx.databaseDocer.entity.RemarkInfo;
import com.pascalx.databaseDocer.entity.TableInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanghui10 on 2016/8/19.
 */
public class Mysql {

//    static final String DB_URL="jdbc:mysql://172.24.7.89:3306/test?useUnicode=true&characterEncoding=utf8";
//    static final String USER = "mysql";
//    static final String PASS = "123456";
    /**
     * 复用
     */
    private static Connection conn;
    private static PreparedStatement preparedStatement;

    private static String dbName;

    /**
     * 提前读出sys_dict_code表中的数据，不用每个字段都去读取一次
     */
    public static Map<String,List<RemarkInfo>> cacheRemarkInfoMap;

    private static Connection getConn(String url,String user,String pass) throws Exception{
        if(conn==null){
            System.out.println("Connecting to database...");
            Class.forName("com.mysql.jdbc.Driver");
            conn=DriverManager.getConnection(url,user,pass);
        }
        return conn;
    }

    public static  List<TableInfo> connectDB(DBInfo dbInfo)throws Exception{
        getConn(dbInfo.getUrl(),dbInfo.getUser(),dbInfo.getPassword());
        dbName=dbInfo.getDbName();
        return getTableInfos();
    }

    public static List<TableInfo> getTableInfos() throws SQLException{
        List<TableInfo> tableInfos = new ArrayList<TableInfo>();
        String sql="select table_name,table_comment from information_schema.tables where table_schema='"+dbName+"' and table_type='base table'";
        Statement stmt=conn.createStatement();
        ResultSet resultSet=stmt.executeQuery(sql);
        while (resultSet.next()){
            tableInfos.add(new TableInfo(resultSet.getString("table_name"),resultSet.getString("table_comment")));
        }
        cacheRemarkInfoMap=getCacheRemarkInfoMap();
        resultSet.close();
        stmt.close();
        System.out.println(tableInfos);
        return tableInfos;
    }

    private static Map<String,List<RemarkInfo>> getCacheRemarkInfoMap() throws SQLException {
        Map<String,List<RemarkInfo>> cacheRemarkInfoMap_=new HashMap<String,List<RemarkInfo>>();
        String sql="select com_type,com_code,com_desc from sys_dict_code ";
        Statement stmt=conn.createStatement();
        ResultSet resultSet=stmt.executeQuery(sql);
        while (resultSet.next()){
            List<RemarkInfo> remarkInfos= cacheRemarkInfoMap_.get(resultSet.getString("com_type"));
            if(remarkInfos==null){
                remarkInfos=new ArrayList<RemarkInfo>();
                cacheRemarkInfoMap_.put(resultSet.getString("com_type"),remarkInfos);
            }
            remarkInfos.add(new RemarkInfo(resultSet.getInt("com_code"),resultSet.getString("com_desc")));
        }

        resultSet.close();
        stmt.close();
        return cacheRemarkInfoMap_;
    }

    public static List<ColumnInfo> getColumnInfosByTableName(String tableName) throws SQLException{
        List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        getColumnPreparedStatement().setString(1,tableName);
        ResultSet resultSet= getColumnPreparedStatement().executeQuery();
        while (resultSet.next()){
            ColumnInfo columnInfo=new ColumnInfo();
            columnInfo.setComment(resultSet.getString("COLUMN_COMMENT"));
            columnInfo.setExtra(resultSet.getString("EXTRA"));
            columnInfo.setIsNullable(resultSet.getString("IS_NULLABLE"));
            columnInfo.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
            columnInfo.setKey(resultSet.getString("COLUMN_KEY"));
            columnInfo.setType(resultSet.getString("COLUMN_TYPE"));
            columnInfo.setName(resultSet.getString("COLUMN_NAME"));

            List<RemarkInfo> remarkInfos=cacheRemarkInfoMap.get(columnInfo.getName());
            cacheRemarkInfoMap.remove(columnInfo.getName());
            if(remarkInfos==null){
                remarkInfos=cacheRemarkInfoMap.get(columnInfo.getName().toUpperCase());
                cacheRemarkInfoMap.remove(columnInfo.getName().toUpperCase());
            }
            columnInfo.setRemarkInfos(remarkInfos);

            columnInfos.add(columnInfo);
        }
        resultSet.close();
        System.out.println(columnInfos);
        return columnInfos;
    }

    public static List<ColumnInfo> getColumnInfosByTableName2(String tableName) throws SQLException{
        List<ColumnInfo> columnInfos = new ArrayList<ColumnInfo>();
        Statement stmt=conn.createStatement();
        String sql="select " +
                "column_name,column_comment,column_type,is_nullable," +
                "column_key,column_default,extra " +
                "from information_schema.columns" +
                " where table_schema='"+dbName +
                "' and table_name='"+tableName+"'";
        ResultSet resultSet=stmt.executeQuery(sql);
        while (resultSet.next()){
            ColumnInfo columnInfo=new ColumnInfo();
            columnInfo.setComment(resultSet.getString("COLUMN_COMMENT"));
            columnInfo.setExtra(resultSet.getString("EXTRA"));
            columnInfo.setIsNullable(resultSet.getString("IS_NULLABLE"));
            columnInfo.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
            columnInfo.setKey(resultSet.getString("COLUMN_KEY"));
            columnInfo.setType(resultSet.getString("COLUMN_TYPE"));
            columnInfo.setName(resultSet.getString("COLUMN_NAME"));

            columnInfos.add(columnInfo);
        }
        resultSet.close();
        stmt.close();
        System.out.println(columnInfos);
        return columnInfos;
    }
//
//    public static RemarkInfo getRemarkInfoByColumnName(String columnName){
//        return cacheRemarkInfoMap.get(columnName);
//    }

    private static PreparedStatement getColumnPreparedStatement()throws SQLException{
        if(preparedStatement==null){
            String sql="select " +
                    "column_name,column_comment,column_type,is_nullable," +
                    "column_key,column_default,extra " +
                    "from information_schema.columns" +
                    " where table_schema='"+dbName +
                    "' and table_name=?";
                preparedStatement=conn.prepareStatement(sql);
        }
        return preparedStatement;
    }


    public static ResultSet query(String sql) throws SQLException{
        Statement stmt =conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        return  rs;
    }
}
