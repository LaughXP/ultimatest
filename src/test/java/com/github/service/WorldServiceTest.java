package com.github.service;

import com.github.service.WorldService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午9:39
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-jdbc.xml"})
public class WorldServiceTest {

    @Autowired
    private WorldService worldService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void hello() throws Exception {
        String res = worldService.hello("testName",100);
        System.out.println(res);
    }

}