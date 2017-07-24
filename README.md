
# 单元测试概要
- Mock接口( 本地服务接口、RPC接口、http接口 )
- Mock数据库
- 调用过程校验
- 执行结果校验

**所有测试过程全部建立在与spring集成的基础上**

# 工具
### Junit
- 回归测试框架

### Mockito
-  简介<br>
测试案例,A调用B和C
```
A -> B
A -> C
```
mock案例,测试时A调用mock后的对象
```
A -> B_mocked
A -> C_mocked
```
- 步骤<br>
分三步走，第一步mock对象，第二步打桩(stub)，第三步验证(verify)
- 例子
```
@Test  
public void test(){  
    //mock对象
    List<String> list = Mockito.mock(List.class);
    //打桩
    when(list.get(Mockito.anyInt())).thenReturn("hello","world");  

    String result = list.get(0)+list.get(1);  
    //验证
    verify(list,Mockito.times(2)).get(Mockito.anyInt());  
    MatcherAssert.assertThat(result, Matchers.equalTo("helloworld"));
}
```
- mock和spy
  - mock的类的所有方法都会被mock
  - spy的类的方法只有被打桩的方法才会进行mock，没被打桩的方法会调用实际方法
  - spy的类使用when()会调用实际方法并返回mock的值
  - spy的类使用doReturn()才会进行mock，不调用实际方法并返回mock的值
  - 被@Spy注解修饰的变量必须要有初始值
- 缺点<br>
因为mockito是使用bytebuddy生成子类代理来mock原始对象，所以不可以实现对静态函数、私有函数、本地方法的模拟.

### PowerMock
- 简介<br>
PowerMock使用一个自定义类加载器和字节码操作来模拟静态方法，构造函数，final类和方法，私有方法
- 例子
```
@RunWith(PowerMockRunner.class)
@PrepareForTest(TestStatic.class)  
public class Test {

    @Test   
    public void test() throws Exception {  
      PowerMockito.mockStatic(TestStatic.class);
      PowerMockito.when(TestStatic.staticMethod()).thenReturn("value");
    }  
}
```
- 原理
  - 当某个测试方法被注解@PrepareForTest标注以后，在运行测试用例时，会创建一个新的org.powermock.core.classloader.MockClassLoader实例，然后加载该测试用例使用到的类（系统类除外）。
  - PowerMock会根据你的mock要求，去修改写在注解@PrepareForTest里的class文件（当前测试类会自动加入注解中），以满足特殊的mock需求。例如：去除final方法的final标识，在静态方法的最前面加入自己的虚拟实现等。
  - 如果需要mock的是系统类的final方法和静态方法，PowerMock不会直接修改系统类的class文件，而是修改调用系统类的class文件，以满足mock需求。

- 注意事项
  - java7需要增加jvm参数:-noverify
  - 遇到ClassNotFound错误，需要把对应的包名放到注解@PowerMockIgnore内(详见最后的项目代码示例)
### DBunit
- 简介<br>
通过预处理数据来初始化数据库数据，通过预期数据来校验实际测试结果，具体流程如下
```
建立数据库连接-> 备份表 -> 插入初始化数据
-> 执行测试-> 从数据库取实际结果-> 事先准备的期望结果
-> 断言 -> 回滚数据库 -> 关闭数据库连接
```
- 例子<br>
单独的dbunit使用起来过于麻烦，详情例子可以查看[dbunit官网](http://dbunit.sourceforge.net/faq.html)
- 与spring结合<br>
和spring结合，需要用到另外一个插件：[Spring Test DBUnit](https://springtestdbunit.github.io/spring-test-dbunit/),它提供了多个注解来方便测试，其中主要的三个注解是
```
@DbUnitConfiguration(databaseConnection  = {"dataSource"})
指定数据源
@DatabaseSetup(value="data.xml", connection=dataSource,
type=DatabaseOperation.CLEAN_INSERT)
插入初始化数据
@ExpectedDatabase(value="result.xml",connection=dataSource,
assertionMode= DatabaseAssertionMode.NON_STRICT_UNORDERED)
校验期望的数据
```

### H2
- 简介<br>
不依赖于其他外部数据库，在本机内存中模拟数据库，避免因为脏数据产生的测试问题
- 劣势<br>
不支持mysql的函数，只支持sql标准函数，具体可以去[h2官网](http://www.h2database.com/html/main.html)查看

### Spring Test
- 原理<br>
主要由三个接口构成：TestContextManager、TestContext、TestExcutionListener
  - TestContextManager
  每次测试用例启动都会被创建，并且管理一个TestContext。执行与管理TestExcutionListener集合
  - TestContext
  负责持有一个当前测试的上下文，包含测试类的基本信息，加载的spring上下文
  - TestExcutionListener
  在单元测试开始或者结束的时候执行，可以自定义自己的监听

# 集成

### Mockito+PowerMock+Junit4+Spring集成
- 不能mock被spring代理的对象，比如加了切面的类(例如事务切面)
- 被代理的类需要用AopTestUtils.getTargetObject()获得原始类
- 多层mock测试的只能@Mock最深层的类,中间类需要用@Spy
- @Spy修饰的属性必须要有初始值

### Dbunit+H2+Junit4+Spring集成
- 使用h2在spring运行时替换对应的dataSource
- 注意h2的语法

### 终极集成
- 自定义MockH2TestExecutionListener并继承TestExcutionListener
- 集成com.github.springtestdbunit.DbUnitTestExecutionListener
- 并且按照以下顺序引入TestExcutionListener
```
@TestExecutionListeners({MockH2TestExecutionListener.class,DbUnitTestExecutionListener.class})
```
### 源代码
[项目代码](https://github.com/LaughXP/testWithDbunitAndH2)
