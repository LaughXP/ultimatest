package com.github.service.mock;

import com.github.common.Constants;
import com.github.dubbo.DubboEntity;
import com.github.dubbo.DubboService;
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
import org.springframework.test.util.AopTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

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
public class MockTwoTest {

    //需要mock的类
    @Mock
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

    /**
     * mock测试对象属性的属性
     * @throws Exception
     */
    @Test
    public void hello() throws Exception {
        long id = 1;
        int balance = 10;
        DubboEntity dto = DubboEntity.newBuilder().balance(balance).build();
        //一旦用了类似Mockito.anyString()这种模糊匹配，必须每个参数都要用这种模式
        Mockito.when(helloService.getPrimaryKey(Mockito.anyString())).thenReturn(id);
        Mockito.doNothing().when(helloService).update(Mockito.anyLong(), Mockito.anyInt());
        Mockito.when(dubboService.getBalance(Mockito.anyString())).thenReturn(dto);

        //这里因为paymentService是被spy的，只有被打桩的方法才会进行mock，因为paymentService.getBalance没被打桩，所以该方法会被实际调用
        String res = worldService.hello("34122290876", 23456);

        Mockito.verify(helloService).getPrimaryKey(Mockito.anyString());
        Mockito.verify(helloService).update(Mockito.anyLong(), Mockito.anyInt());
        Mockito.verify(dubboService).getBalance(Mockito.anyString());

        MatcherAssert.assertThat(res, Matchers.equalTo(id + ":" + balance * Constants.getRate()));
    }

}