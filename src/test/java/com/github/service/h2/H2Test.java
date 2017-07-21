package com.github.service.h2;

import com.alibaba.fastjson.annotation.JSONType;
import com.github.service.WorldService;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-21 上午10:11
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
//该注解是表名
@JSONType(orders = {"hello"})
public class H2Test extends H2TestExecutionListener {

    @Autowired
    private WorldService worldService;

    @Test
    @DatabaseSetup(value = "classpath:hello_beg.xml", connection = H2TestExecutionListener.DATA_SOURCE, type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(value="classpath:hello_end.xml",connection = H2TestExecutionListener.DATA_SOURCE,assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void hello() {
        String name = "xx";
        int number = 100;
        worldService.hello(name, number);
    }

}
