package com.github.service.mock;

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
        getTarget(testContext);
    }

    private void getTarget(final TestContext testContext) throws Exception {
        Object testClass = testContext.getTestInstance();
        Field[] fields = AopTestUtils.getTargetObject(testClass).getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Spy spy = field.getAnnotation(Spy.class);
            InjectMocks injectMocks = field.getAnnotation(InjectMocks.class);
            Autowired autowired = field.getAnnotation(Autowired.class);
            if( (spy != null && autowired != null) || (injectMocks != null && autowired != null)) {
                field.set(testClass,AopTestUtils.getTargetObject(field.get(testClass)));
            }
        }
        MockitoAnnotations.initMocks(testClass);
    }
}
