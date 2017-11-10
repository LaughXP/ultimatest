package com.github.service.h2;

import com.github.service.BaseTestExecutionListener;
import org.springframework.test.context.TestContext;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-05-12 上午11:17
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */

public class H2TestExecutionListener extends BaseTestExecutionListener {

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        super.prepareTestInstance(testContext);
    }
}