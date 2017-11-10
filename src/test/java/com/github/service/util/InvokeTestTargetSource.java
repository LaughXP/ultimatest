package com.github.service.util;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.util.AopTestUtils;

import java.lang.reflect.Field;

/**
 * @author yu.gao 2017-11-09 下午9:57
 */
public class InvokeTestTargetSource {

    public static void getTarget(final TestContext testContext) throws Exception {
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
