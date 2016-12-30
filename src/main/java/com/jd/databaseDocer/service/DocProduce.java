package com.jd.databaseDocer.service;

import com.jd.databaseDocer.entity.DBInfo;
import com.jd.databaseDocer.entity.TableInfo;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by yanghui10 on 2016/8/20.
 */
public interface DocProduce {
    void export(List<TableInfo> tableInfos, String path) throws Exception;
}
