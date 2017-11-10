package com.github.dubbo;

import org.springframework.stereotype.Service;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午9:19
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@Service
public class DubboServiceImpl implements DubboService {

    public DubboEntity getBalance(String name) {
        System.out.println("invoke DubboServiceImpl.getBalance");
        return DubboEntity.newBuilder().balance(500).build();
    }
}
