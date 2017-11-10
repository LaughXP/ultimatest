package com.github.service.impl;

import com.github.Entity.Hello;
import com.github.dao.HelloMapper;
import com.github.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午9:20
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Autowired
    private HelloMapper helloMapper;

    @Override
    public Long getPrimaryKey(String name) {
        return helloMapper.getPrimaryKey(name);
    }

    @Override
    public void update(long id, int number) {
        helloMapper.update(id, number);
    }

    @Override
    public List<Hello> selectAll() {
        return helloMapper.selectAll();
    }
}
