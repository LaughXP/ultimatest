package com.github.service.mock;

import com.github.common.Constants;
import com.github.service.BalanceService;
import com.github.service.HelloService;
import com.github.service.WorldService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-20 下午4:25
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PrepareForTest({Constants.class})
@PowerMockIgnore({"javax.crypto.*","com.sun.*", "org.w3c.*","javax.xml.*", "org.xml.*",
        "javax.management.*","javax.net.ssl.*"})
@ContextConfiguration(locations = {"classpath:spring-jdbc.xml"})
@TestExecutionListeners({MockProxyTestExecutionListener.class})
public class SpyOneTest {

    @Mock
    private HelloService helloService;
    @Spy
    @Autowired
    private BalanceService balanceService;

    //等待测试的类
    @Autowired
    @InjectMocks
    private WorldService worldService;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Constants.class);
        PowerMockito.when(Constants.getRate()).thenReturn(2);
    }

    @Test
    public void hello() throws Exception {
        long id = 1;
        int balance = 10;
        Mockito.when(helloService.getPrimaryKey(Mockito.anyString())).thenReturn(id);
        Mockito.doNothing().when(helloService).update(Mockito.anyLong(), Mockito.anyInt());
//        Mockito.when(balanceService.calBalance(Mockito.anyString())).thenReturn(balance);
        Mockito.doReturn(balance).when(balanceService).calBalance(Mockito.anyString());

        String res = worldService.hello("34122290876", 23456);

        Mockito.verify(helloService).getPrimaryKey(Mockito.anyString());
        Mockito.verify(helloService).update(Mockito.anyLong(), Mockito.anyInt());
        Mockito.verify(balanceService).calBalance(Mockito.anyString());

        MatcherAssert.assertThat(res, Matchers.equalTo(id + ":" + balance * Constants.getRate()));
    }

}