package com.github.service.h2;

import com.alibaba.fastjson.annotation.JSONType;
import com.github.Entity.Hello;
import com.github.dubbo.DubboEntity;
import com.github.service.HelloService;
import com.github.service.WorldService;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 创 建 人 :           yu.gao
 * 创建时间 :           2017-07-21 上午10:11
 * 功能描述 :           <P></p >
 * 变更记录 :           <P></p >
 */
//该注解是表名
@JSONType(orders = {"hello"})
public class H2Test extends H2TestExecutionListener {

    @Autowired
    private WorldService worldService;

    @Autowired
    private HelloService helloService;

    @Test
    @DatabaseSetup(value = "classpath:hello_beg.xml", connection = H2TestExecutionListener.DATA_SOURCE, type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(value="classpath:hello_end.xml",connection = H2TestExecutionListener.DATA_SOURCE,assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void hello() {
        String name = "xx";
        int number = 100;
        worldService.hello(name, number);
    }

    @Test
    @DatabaseSetup(value = "classpath:hello_beg.xml", connection = H2TestExecutionListener.DATA_SOURCE, type = DatabaseOperation.CLEAN_INSERT)
    public void testAll() throws Exception {
        List<Hello> result = Lists.newArrayList(
                Hello.newBuilder().id(10).name("xx").dubboEntity(DubboEntity.newBuilder().balance(100).stock("xxx").build()).build(),
                Hello.newBuilder().id(11).name("yy").dubboEntity(DubboEntity.newBuilder().balance(111).stock("yyy").build()).build(),
                Hello.newBuilder().id(12).name("zz").dubboEntity(DubboEntity.newBuilder().balance(122).stock("zzz").build()).build());
        List<Hello> targetHello = Lists.newArrayList(
                Hello.newBuilder().id(11).name("yy").dubboEntity(DubboEntity.newBuilder().balance(112).stock("yyy").build()).build(),
                Hello.newBuilder().id(15).name("xx").dubboEntity(DubboEntity.newBuilder().balance(155).stock("xxx").build()).build(),
                Hello.newBuilder().id(13).name("zz").dubboEntity(DubboEntity.newBuilder().balance(133).stock("zzz").build()).build());
        System.out.println(compare(result, targetHello));
    }

    public Table<Object, Object, Integer> compare(Object source, Object target) throws Exception {
        if(source == null || target == null) {
            return null;
        }
        Table<Object, Object, Integer> table = HashBasedTable.create();
        if(source instanceof Collection) {
            Collection sourceCollection = (Collection) source;
            Collection targetCollection = (Collection) target;
            int allSize = sourceCollection.size();
            Set<Object> alreadyEqualSet = new HashSet<Object>();
            for (Object oSource : sourceCollection) {
                boolean isEqual = false;
                Table<Object, Object, Integer> tmpTableOne = HashBasedTable.create();
                for(Object oTarget : targetCollection) {
                    Table<Object, Object, Integer> tmpTableTwo = compare(oSource, oTarget);
                    if( tmpTableTwo == null) {
                        allSize--;
                        isEqual = true;
                        alreadyEqualSet.add(oTarget);
                        break;
                    } else {
                        tmpTableOne.put(oSource, oTarget, tmpTableTwo.get(oSource, oTarget));
                    }
                }
                if(!isEqual) {
                    table.putAll(tmpTableOne);
                }
            }
            return 0 == allSize ? null : clearData(table, alreadyEqualSet);
        } else if(source instanceof Map) {
            return table;
        } else {
            if(!source.getClass().getName().equals(target.getClass().getName())) {
                return null;
            }
            Class clazz = source.getClass();
            Field[] fields = clazz.getDeclaredFields();
            if (fields == null || fields.length == 0) {
                return null;
            }
            Integer diffFields = 0;
            for (Field field : fields) {
                field.setAccessible(true);
                Object sourceVal = field.get(source);
                Object targetVal = field.get(target);
                if(isWrapClass(sourceVal.getClass()) && isWrapClass(targetVal.getClass())) {
                    if(!sourceVal.equals(targetVal)) {
                        diffFields++;
                    }
                } else {
                    Table<Object, Object, Integer> result = compare(field.get(source), field.get(target));
                    if(result != null) {
                        diffFields++;
                    }
                }
            }
            if(diffFields > 0) {
                table.put(source,target,diffFields);
                return table;
            } else {
                return null;
            }
        }
    }

    private Table<Object, Object, Integer> clearData(Table<Object, Object, Integer> table, Set<Object> alreadyEqualSet) {
        Table<Object, Object, Integer> alreadyCLearTable = HashBasedTable.create();
        Map<Object, Map<Object, Integer>> rowMap =table.rowMap();
        Set<Object> alreadyNotEqualSet = new HashSet<Object>();
        for(Map.Entry<Object, Map<Object, Integer>> entryRow : rowMap.entrySet()) {
            Map<Object, Integer> rowMapVal = entryRow.getValue();
            Integer max = Integer.MAX_VALUE;
            Map.Entry<Object, Integer> maxEntry = null;
            for(Map.Entry<Object, Integer> entry : rowMapVal.entrySet()) {
                if(entry.getValue() < max && !alreadyEqualSet.contains(entry.getKey()) && !alreadyNotEqualSet.contains(entry.getKey())) {
                    maxEntry = entry;
                }
            }
            if(maxEntry != null) {
                alreadyCLearTable.put(entryRow.getKey(), maxEntry.getKey(), maxEntry.getValue());
                alreadyNotEqualSet.add(maxEntry.getKey());
            }
        }
        return alreadyCLearTable;
    }

    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        Hello hello = Hello.newBuilder().id(11).name("yy").build();
        System.out.println(isWrapClass(hello.getClass()));
        System.out.println(isWrapClass(((Object)hello.getId()).getClass()));
    }
}