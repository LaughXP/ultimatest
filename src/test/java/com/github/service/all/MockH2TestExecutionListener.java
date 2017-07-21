package com.github.service.all;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.annotation.JSONType;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.util.AopTestUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-21 上午10:38
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore({"javax.crypto.*","com.sun.*", "org.w3c.*","javax.xml.*", "org.xml.*",
        "javax.management.*","javax.net.ssl.*"})
@ContextConfiguration(locations = {"classpath:spring-jdbc.xml"})
@TestExecutionListeners({MockH2TestExecutionListener.class,DbUnitTestExecutionListener.class})
@DbUnitConfiguration(databaseConnection  = {MockH2TestExecutionListener.DATA_SOURCE})
public class MockH2TestExecutionListener extends DependencyInjectionTestExecutionListener {

    //spring-jdbc中配置的数据源
    public static final String DATA_SOURCE = "dataSource";

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        String[] databaseConnectionBeanNames = null;
        DbUnitConfiguration configuration = testContext.getTestClass().getAnnotation(DbUnitConfiguration.class);
        if (configuration != null) {
            databaseConnectionBeanNames = configuration.databaseConnection();
        }
        JSONType suppressWarnings = testContext.getTestClass().getAnnotation(JSONType.class);
        String[] tableName = suppressWarnings.orders();
        DruidDataSource newDruidDataSource = dicDataSource();
        List<String> tableSql = null;
        if(databaseConnectionBeanNames != null) {
            for (int i = 0; i < databaseConnectionBeanNames.length; i++) {
                String dataSourceBeanName = databaseConnectionBeanNames[i];
                GenericApplicationContext applicationContext = (GenericApplicationContext)testContext.getApplicationContext();
                DruidDataSource oldDruidDataSource = (DruidDataSource)applicationContext.getBean(dataSourceBeanName);
                if(CollectionUtils.isEmpty(tableSql)) {
                    tableSql = genTableSql(tableName,oldDruidDataSource);
                }
                DefaultListableBeanFactory beanFactory = applicationContext.getDefaultListableBeanFactory();
                DataSourceTransactionManager dataSourceTransactionManager =  (DataSourceTransactionManager)beanFactory.getSingleton("transactionManager");
                dataSourceTransactionManager.setDataSource(newDruidDataSource);

                beanFactory.destroySingleton(dataSourceBeanName);
                beanFactory.registerSingleton(dataSourceBeanName,newDruidDataSource);
            }
            createTable(tableSql, newDruidDataSource);
        }
        super.prepareTestInstance(testContext);
        getTarget(testContext);
    }

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
        druidDataSource.setFilters("stat,log4j");
        druidDataSource.setKeepAlive(true);
        return druidDataSource;
    }

    private List<String> genTableSql(String[] tableName, DataSource dataSource) throws Exception {
        Connection connection = dataSource.getConnection();
        Statement stat = connection.createStatement();
        List<String> tableSqlAll = new ArrayList<String>();
        for(String single : tableName) {
            stat.execute("show create table " + single);
            ResultSet rs = stat.getResultSet();
            String tableSql = null;
            while (rs.next()) {
                tableSql = rs.getString(rs.getMetaData().getColumnName(2));
            }
            //格式化到h2能识别的sql
            tableSql = StringUtils.replace(tableSql, "ON UPDATE CURRENT_TIMESTAMP ", "");
            String[] splitArray = StringUtils.split(tableSql,"\n");
            Preconditions.checkNotNull(splitArray);
            //重命名key,如果是多张表，相同的key会报重名错误，在原来的key名字前面加上表名字
            String keySplit = "  KEY `";
            String[] finalSplitArray = new String[splitArray.length];
            for(int i =0;i<splitArray.length;i++) {
                String split = splitArray[i];
                if(StringUtils.startsWith(split, keySplit)) {
                    String[] tmp = StringUtils.splitByWholeSeparator(split, keySplit);
                    Preconditions.checkNotNull(tmp);
                    finalSplitArray[i] =  Joiner.on(single).join(keySplit,tmp[0]);
                } else {
                    finalSplitArray[i] = splitArray[i];
                }
            }
            tableSql = Joiner.on("\n").join(ArrayUtils.subarray(finalSplitArray,0, finalSplitArray.length -1));
            tableSql += ")AUTO_INCREMENT=1;";
            tableSqlAll.add(tableSql);
            rs.close();
        }
        stat.close();
        return tableSqlAll;
    }

    private void createTable(List<String> tableSqlAll,DataSource dataSource) throws Exception {
        Connection connection = dataSource.getConnection();
        for(String tableSql : tableSqlAll) {
            Statement stat = connection.createStatement();
            stat.execute(tableSql);
            stat.close();
        }
        connection.close();
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
