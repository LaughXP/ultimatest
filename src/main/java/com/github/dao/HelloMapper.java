package com.github.dao;

import com.github.Entity.Hello;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午8:52
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
public interface HelloMapper {

    public Long getPrimaryKey(@Param("name") String name);

    public void update(@Param("id") long id, @Param("number") int number);

    public List<Hello> selectAll();
}
