package com.github.laughxp.service.impl;

import com.github.laughxp.entity.Hello;
import com.github.laughxp.service.HelloService;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.Assert.*;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-06-08 下午4:26
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@Component("hello/hello.sql")
public class HelloServiceImplTest extends DBUnitBaseTest {

    @Autowired
    private HelloService helloService;

    @Test
    @DatabaseSetup(value = "classpath:hello/hello_update_setup.xml", connection = "testDataSource", type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(value="classpath:hello/hello_update_after.xml",connection = "testDataSource",assertionMode= DatabaseAssertionMode.NON_STRICT)
    public void update() throws Exception {
        Hello hello = new Hello();
        hello.setId(1L);
        helloService.update(hello);
    }

}