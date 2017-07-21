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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.AopTestUtils;

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
public class MockOneTest {

    //需要mock的类
    @Mock
    private HelloService helloService;
    @Mock
    private BalanceService balanceService;

    //等待测试的类
    @Autowired
    @InjectMocks
    private WorldService worldService;

    @Before
    public void setUp() throws Exception {
        /**
         * @InjectMocks不能作用于被spring代理的对象，必须要得到原始对象
         */
        worldService = AopTestUtils.getTargetObject(worldService);
        MockitoAnnotations.initMocks(this);
        //Mock静态变量
        PowerMockito.mockStatic(Constants.class);
        PowerMockito.when(Constants.getRate()).thenReturn(2);
    }

    @Test
    public void hello() throws Exception {
        long id = 1;
        int balance = 10;
        //一旦用了类似Mockito.anyString()这种模糊匹配，必须每个参数都要用这种模式
        Mockito.when(helloService.getPrimaryKey(Mockito.anyString())).thenReturn(id);
        Mockito.doNothing().when(helloService).update(Mockito.anyLong(), Mockito.anyInt());
        Mockito.when(balanceService.calBalance(Mockito.anyString())).thenReturn(balance);

        String res = worldService.hello("34122290876", 23456);

        Mockito.verify(helloService).getPrimaryKey(Mockito.anyString());
        Mockito.verify(helloService).update(Mockito.anyLong(), Mockito.anyInt());
        Mockito.verify(balanceService).calBalance(Mockito.anyString());

        MatcherAssert.assertThat(res, Matchers.equalTo(id + ":" + balance * Constants.getRate()));
    }

}