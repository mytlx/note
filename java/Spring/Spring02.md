# Spring的IOC的注解开发

## Spring的IOC的注解开发的入门

### 创建web项目，引入jar包

* 在Spring4的版本中，除了引入基本你的开发包外，还需要引入aop的包



### 引入Spring的配置文件

* 在src下创建applicationContext.xml
  * 引入约束：使用注解开发引入context约束

### 创建接口和实现类

#### 开启Spring组件扫描

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        http://www.springframework.org/schema/context/spring-context.xsd">

    <!--Spring的IOC注解入门-->
    <!--使用IOC注解开发，配置扫描组件（哪些包下的类使用注解）-->
    <context:component-scan base-package="demo01"/>

</beans>
```

### 在类上添加注解

```java
@Component("userDao")
public class UserDaoImpl implements UserDao {
    @Override
    public void save() {
        System.out.println("==========save===========");
    }
}
```

### 注解方式设置属性的值

* 注解方式：使用注解方式，可以没有set方法
  * 属性如果有set方法，需要将属性注入的注解添加到set方法
  * 属性如果没有set方法，需要将属性注入的注解添加属性上

```java
@Component("userDao")
public class UserDaoImpl implements UserDao {
    @Value("xiaoming")
    private String name;

    // @Value("xiaoming")
    // public void setName(String name) {
    //     this.name = name;
    // }

    @Override
    public void save() {
        System.out.println("==========save===========");
    }
}
```



## Spring的IOC的注解开发的详解

### @Component：组件

* 修饰一个类，将这个类交给Spring管理
* 这个注解有三个衍生注解（功能类似），修饰类
  * **@Controller：web层**
  * **@Servcie：service层**
  * **@Respository：dao层**

### 属性注入的注解

* 普通属性：
  * @Value：设置普通属性的值
* 对象类型的属性：
  * @Autowired：设置对象类型的属性的值，但是按照类型完成属性注入。
    * 习惯上是按照名称完成属性注入，必须让@Autowired注解和@Qualifier注解一起使用完成按照名称注入。
  * **@Resource：完成对象类型的属性的注入，按照名称完成属性注入。**

### Bean的其他注解

* 生命周期相关的注解（了解）
  * @PostConstruct：初始化方法
  * @PreDestory：销毁方法
* Bean作用范围的注解
  * **@Scope：作用范围**
    * **singleton：默认单例**
    * **prototype：多例**
    * request
    * session
    * globalsession



## IOC的XML和注解开发比较

### XML和注解的比较

* 适用场景
  * XML：可以适用任何场景
    * 结构清晰，维护方便
  * 注解：有些地方用不了，这个类不是自己提供的
    * 开发方便

### XML和注解整合开发

* XML管理Bean，注解完成属性注入（了解）



# Spring的AOP的XML开发

## AOP的概述

### 什么是AOP

AOP：面向切面编程，AOP是OOP的扩展和延伸，解决OOP开发遇到问题。



### Spring底层的AOP实现原理

* 动态代理
  * JDK动态代理：只能对实现了接口的类产生代理
  * Cglib动态代理 （类似于javassist第三方代理技术）：对没有实现接口的类产生代理对象，生成子类对象



## Spring的AOP底层实现（了解）

### JDK动态代理

```java
package demo01;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.4.20
 * Time: 21:43
 */
public class jdkProxy implements InvocationHandler {

    private UserDao userDao;

    public jdkProxy(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDao createProxy() {
        UserDao userDaoProxy = (UserDao) Proxy.newProxyInstance(userDao.getClass().getClassLoader(),
                userDao.getClass().getInterfaces(), this);
        return userDaoProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("save".equals(method.getName())) {
            System.out.println("===============privilege=============");
            return method.invoke(userDao, args);
        }
        return method.invoke(userDao, args);
    }
}
```



### Cglib动态代理

* Cglib：第三方开源代码生成类库，动态添加类的属性和方法。

```java
package demo02;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.4.20
 * Time: 22:03
 */
public class CglibProxy implements MethodInterceptor {
    private CustomeDao customeDao;

    public CglibProxy(CustomeDao customeDao) {
        this.customeDao = customeDao;
    }

    public CustomeDao createProxy() {
        // 1. 创建Cglib的核心类对象
        Enhancer enhancer = new Enhancer();

        // 2. 设置父类
        enhancer.setSuperclass(customeDao.getClass());

        // 3. 设置回调，类似于InvocationHandler对象
        enhancer.setCallback(this);

        // 4. 创建代理对象
        return (CustomeDao) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if ("save".equals(method.getName())) {
            System.out.println("=======privilege==========");
            return methodProxy.invokeSuper(o, objects);
        }
        return methodProxy.invokeSuper(o, objects);
    }
}
```



## Spring的AOP的开发（AspectJ的XML的方式）

### Spring的AOP简介

* AOP思想最早是由AOP联盟组织提出的。

* Spring是使用这种思想最好的框架。
  * Spring的AOP有自己实现的方式（非常繁琐）
  * AspectJ是一个AOP的框架，Spring引入AspectJ作为自身的AOP开发

* Spring两套AOP开发方式
  * Spring传统方式（弃用）
  * Spring基于AspectJ的AOP开发

### AOP开发中的相关术语

* Joinpoint：连接点，可以被拦截到的点

  * 增删改查的方法可以被增强，这些方法可以称为连接点

* Pointcut：切入点，真正被拦截到的点

  * 在实际开发中，只有save进行方法的增强，save称为切入点

* Advice：通知，增强

  * 方法层面的增强
  * 现在对save方法进行权限校验，权限校验的方法称为通知

* Introduction：引介

  * 类层面的增强

* Target：被增强的对象

  * 对UserDao增强，UserDao称为目标

* Weaving：织入，将通知（Advice）应用到目标（Target）的过程

  * 将权限校验方法应用到UserDao的save方法上的过程

* Proxy：代理对象

* Aspect：切面，多个通知和多个切入点组合


## Spring的AOP的入门（AspectJ的XML的方式）

### 创建web项目，引入jar包

* 基本开发包
* aop开发的相关jar包

### 引入Spring的配置文件

* 引入aop的约束

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop.xsd">

</beans>
```

### 编写测试类

* Spring整合Junit单元测试
  * 需要`spring-test-4.3.18.RELEASE.jar`

```java
package demo03;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.4.21
 * Time: 20:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test01 {

    @Resource(name = "productDao")
    private ProductDao productDao;

    @Test
    public void test01() {
        productDao.delete();
        productDao.find();
        productDao.save();
        productDao.update();
    }
}
```

### 编写一个切面类

* 编写切面

  ```java
  public class MyAspectXML {
      public void checkPri() {
          System.out.println("========privilege========");
      }
  }
  ```

* 将切面类交给Spring

  ```xml
  <!--将切面交给Spring管理-->
  <bean id="myAspect" class="demo03.MyAspectXML"/>
  ```

### 通过AOP的配置实现

```xml
<!--通过AOP的配置完成对目标类产生代理-->
<aop:config>
    <!--表达式配置哪些类的哪些方法需要增强-->
    <aop:pointcut id="pointcut1" expression="execution(* demo03.ProductDaoImpl.save(..))"/>

    <!--配置切面-->
    <aop:aspect ref="myAspect">
        <aop:before method="checkPri" pointcut-ref="pointcut1"/>
    </aop:aspect>
</aop:config>
```



## Spring中通知类型

### 前置通知：在目标方法执行之前进行操作

* 前置通知：获得切入点信息

```xml
<aop:aspect ref="myAspect">
    <aop:before method="checkPri" pointcut-ref="pointcut1"/>
</aop:aspect>
```

### 后置通知：在目标方法执行之后进行操作

* 后置通知：获得方法的返回值

```xml
<aop:aspect ref="myAspect">
    <!--前置通知-->
    <aop:before method="checkPri" pointcut-ref="pointcut1"/>
    <!--后置通知-->
    <aop:after-returning method="writeLog" pointcut-ref="pointcut2" returning="result"/>
</aop:aspect>
```

### 环绕通知：在目标方法执行之前和之后进行操作

* 环绕通知：可以阻止目标方法执行

```xml
<aop:aspect ref="myAspect">
    <!--前置通知-->
    <aop:before method="checkPri" pointcut-ref="pointcut1"/>
    <!--后置通知-->
    <aop:after-returning method="writeLog" pointcut-ref="pointcut2" returning="result"/>
    <!--环绕通知-->
    <aop:around method="around" pointcut-ref="pointcut3"/>
</aop:aspect>
```

### 异常抛出通知：在程序出现异常的时候，进行的操作

```xml
<aop:aspect ref="myAspect">
    <!--前置通知-->
    <aop:before method="checkPri" pointcut-ref="pointcut1"/>
    <!--后置通知-->
    <aop:after-returning method="writeLog" pointcut-ref="pointcut2" returning="result"/>
    <!--环绕通知-->
    <aop:around method="around" pointcut-ref="pointcut3"/>
    <!--异常抛出通知-->
    <aop:after-throwing method="afterThrowing" pointcut-ref="pointcut4" throwing="exp"/>
</aop:aspect>
```



### 最终通知：无论代码是否有异常，总是会执行

```xml
<aop:aspect ref="myAspect">
    <!--前置通知-->
    <aop:before method="checkPri" pointcut-ref="pointcut1"/>
    <!--后置通知-->
    <aop:after-returning method="writeLog" pointcut-ref="pointcut2" returning="result"/>
    <!--环绕通知-->
    <aop:around method="around" pointcut-ref="pointcut3"/>
    <!--异常抛出通知-->
    <aop:after-throwing method="afterThrowing" pointcut-ref="pointcut4" throwing="exp"/>
    <!--最终通知-->
    <aop:after method="after" pointcut-ref="pointcut4"/>
</aop:aspect>
```

### 引介通知（不用会）



## Spring切入点表达式写法

### 切入点表达式语法

* 基于execution的函数完成的
* 语法
  * [访问修饰符] 方法返回值 包名.类名.方法名（参数）
    * 除了参数，任何地方都可以用*代替
  * `public void demo.CustomerDao.save(..)`
  * `* *.*Dao.save(..)`
  * `* demo.CustomerDao+.save(..)`
    * 加号“+”代表当前类和其子类
  * `* demo..*.*(..)`
    * 这个包以及子包下的所有类所有方法































