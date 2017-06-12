package com.github.laughxp.dao;

import com.github.laughxp.entity.Hello;
import org.springframework.stereotype.Component;

@Component
public interface HelloMapper {

    int updateByPrimaryKeySelective(Hello hello);
}
