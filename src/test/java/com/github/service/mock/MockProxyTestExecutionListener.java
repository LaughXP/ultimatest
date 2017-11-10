package com.github.service.mock;

import com.github.service.util.InvokeTestTargetSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.util.AopTestUtils;

import java.lang.reflect.Field;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-06-28 下午2:45
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
public class MockProxyTestExecutionListener extends DependencyInjectionTestExecutionListener {

    @Override
    public void prepareTestInstance(final TestContext testContext) throws Exception {
        super.prepareTestInstance(testContext);
        InvokeTestTargetSource.getTarget(testContext);
    }
}
