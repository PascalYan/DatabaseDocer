package com.pascalx.databaseDocer.service;

import com.pascalx.databaseDocer.entity.TableInfo;

import java.util.List;

/**
 * Created by yanghui10 on 2016/8/20.
 */
public interface DocProduce {
    void export(List<TableInfo> tableInfos, String path) throws Exception;
}
