package com.github.service;

import com.github.dao.HelloMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Long getPrimaryKey(String name) {
        return helloMapper.getPrimaryKey(name);
    }

    public void update(long id, int number) {
        helloMapper.update(id, number);
    }
}
