package com.tomtop.timetask.config;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;




/**
 * 
 * 所有mapper都需要继承这个接口
 * @author Administrator
 * @param <T>
 */
public interface SqlMapper<T> extends Mapper<T>, ConditionMapper<T>, MySqlMapper<T>{

}
