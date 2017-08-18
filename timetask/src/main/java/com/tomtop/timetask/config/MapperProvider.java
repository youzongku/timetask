package com.tomtop.timetask.config;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * Created by LSL on 2016/11/15.
 */
public class MapperProvider extends MapperTemplate {

    public MapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String insertUseGeneratedKeysSelective(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, true, true, isNotEmpty()));
        sql.append(SqlHelper.insertValuesColumns(entityClass, true, true, isNotEmpty()));
        return sql.toString();
    }

}
