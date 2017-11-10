package com.github.service.impl;

import com.github.dubbo.DubboEntity;
import com.github.dubbo.DubboService;
import com.github.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午9:27
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@Service("balanceService")
public class BalanceServiceImpl implements BalanceService {

    @Resource
    private DubboService dubboService;

    @Override
    public int calBalance(String name) {
        DubboEntity dubboEntity = dubboService.getBalance(name);
        return dubboEntity.getBalance();
    }
}
