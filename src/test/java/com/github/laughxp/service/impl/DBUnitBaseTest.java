package com.github.laughxp.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.util.FileCopyUtils;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-05-12 上午11:17
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/*.xml"})
@TestExecutionListeners({
        DBUnitBaseTest.class,
		DirtiesContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection  = {"testDataSource"})
public class DBUnitBaseTest extends DbUnitTestExecutionListener {

    public DruidDataSource dicDataSource() throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("org.h2.Driver");
        druidDataSource.setUrl("jdbc:h2:mem:charge_center;MODE=MYSQL");
        druidDataSource.setUsername("sa");
        druidDataSource.setInitialSize(3);
        druidDataSource.setMinIdle(3);
        druidDataSource.setMaxActive(20);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setMaxEvictableIdleTimeMillis(3000000);
        druidDataSource.setValidationQuery("SELECT 'x'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        druidDataSource.setFilters("stat");
        druidDataSource.setKeepAlive(true);
        return druidDataSource;
    }

	private void createTable(String tableSql,DataSource dataSource) throws Exception {
        Resource resource = new ClassPathResource(tableSql);
        String tableSqlFromFile = FileCopyUtils.copyToString(new FileReader(resource.getFile()));
		Statement stat = dataSource.getConnection().createStatement();
		stat.execute(tableSqlFromFile);
		stat.close();
	}

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        String[] databaseConnectionBeanNames = null;
        DbUnitConfiguration configuration = testContext.getTestClass().getAnnotation(DbUnitConfiguration.class);
        if (configuration != null) {
            databaseConnectionBeanNames = configuration.databaseConnection();
        }
        Component component = testContext.getTestClass().getAnnotation(Component.class);
        String tableSql = component.value();
        if(databaseConnectionBeanNames != null) {
            for (String dataSourceBeanName : databaseConnectionBeanNames) {
                DruidDataSource druidDataSource = dicDataSource();
                GenericApplicationContext applicationContext = (GenericApplicationContext)testContext.getApplicationContext();
                DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();

                DataSourceTransactionManager dataSourceTransactionManager =  (DataSourceTransactionManager)beanFactory.getSingleton("testTransactionManager");
                dataSourceTransactionManager.setDataSource(druidDataSource);

                beanFactory.destroySingleton(dataSourceBeanName);
                beanFactory.registerSingleton(dataSourceBeanName,druidDataSource);
                createTable(tableSql, druidDataSource);
            }
        }
        super.prepareTestInstance(testContext);
    }

}