package com.github.service.all;

import com.alibaba.fastjson.annotation.JSONType;
import com.github.common.Constants;
import com.github.dubbo.DubboEntity;
import com.github.dubbo.DubboService;
import com.github.service.BalanceService;
import com.github.service.HelloService;
import com.github.service.WorldService;
import com.github.service.h2.H2TestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-21 上午10:11
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
//该注解是表名
@JSONType(orders = {"hello"})
@PrepareForTest({Constants.class})
public class AllTest extends MockH2TestExecutionListener {

    @Spy
    @Autowired
    private HelloService helloService;
    @Mock
    private DubboService dubboService;
    @Spy
    @Autowired
    private BalanceService balanceService;

    @Autowired
    @InjectMocks
    private WorldService worldService;

    @Before
    public void setUp() throws Exception {
        ReflectionTestUtils.setField(AopTestUtils.getTargetObject(balanceService), "dubboService", dubboService);
        //Mock静态变量
        PowerMockito.mockStatic(Constants.class);
        PowerMockito.when(Constants.getRate()).thenReturn(2);
    }

    @Test
    @DatabaseSetup(value = "classpath:hello_beg.xml", connection = H2TestExecutionListener.DATA_SOURCE, type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(value="classpath:hello_end.xml",connection = H2TestExecutionListener.DATA_SOURCE,assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void hello() {
        String name = "xx";
        int number = 1000;
        long id = 10;
        int balance = 10;

        DubboEntity dto = DubboEntity.builder().balance(balance).build();
        Mockito.doReturn(id).when(helloService).getPrimaryKey(Mockito.anyString());
//        Mockito.doNothing().when(helloService).update(Mockito.anyLong(), Mockito.anyInt());
        Mockito.when(dubboService.getBalance(Mockito.anyString())).thenReturn(dto);

        String res = worldService.hello(name, number);

        Mockito.verify(helloService).getPrimaryKey(Mockito.anyString());
//        Mockito.verify(helloService).update(Mockito.anyLong(), Mockito.anyInt());
        Mockito.verify(dubboService).getBalance(Mockito.anyString());

        MatcherAssert.assertThat(res, Matchers.equalTo(id + ":" + balance * Constants.getRate()));
    }

}
