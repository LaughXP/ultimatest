package com.github.laughxp.service.impl;

import com.github.laughxp.dao.HelloMapper;
import com.github.laughxp.entity.Hello;
import com.github.laughxp.service.HelloService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-06-08 下午3:11
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Resource
    private HelloMapper helloMapper;


    public int update(Hello hello) {
        return helloMapper.updateByPrimaryKeySelective(hello);
    }
}
