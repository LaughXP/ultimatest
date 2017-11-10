package com.github.service.all;

import com.github.service.BaseTestExecutionListener;
import com.github.service.util.InvokeTestTargetSource;
import org.springframework.test.context.TestContext;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-21 上午10:38
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
public class MockH2TestExecutionListener extends BaseTestExecutionListener{


    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        super.prepareTestInstance(testContext);
        InvokeTestTargetSource.getTarget(testContext);
    }
}
