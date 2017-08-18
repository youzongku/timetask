package com.tomtop.timetask.config;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

/**
 * Created by LSL on 2016/11/15.
 */
public interface InsertUseGeneratedKeysMapper<T> {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = MapperProvider.class, method = "dynamicSQL")
    int insertUseGeneratedKeysSelective(T record);

}
