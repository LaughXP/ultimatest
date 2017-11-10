package com.github.service.impl;

import com.github.common.Constants;
import com.github.service.BalanceService;
import com.github.service.HelloService;
import com.github.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午9:28
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@Service
public class WorldServiceImpl implements WorldService {

    @Autowired
    private HelloService helloService;
    @Autowired
    private BalanceService balanceService;

    @Override
    public String hello(String name, int number) {
        Long id = helloService.getPrimaryKey(name);
        helloService.update(id, number);
        return id + ":" + balanceService.calBalance(name) * Constants.getRate();
    }
}
