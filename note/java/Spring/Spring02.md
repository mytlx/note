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

* 修饰一个类，将这个类叫给Spring管理
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

AOP思想最早是由AOP联盟组织提出的。

Spring是使用这种思想最好的框架。

* Spring的AOP有自己实现的方式（非常繁琐）
* AspectJ是一个AOP的框架，Spring引入AspectJ作为自身的AOP开发

Spring两套AOP开发方式

* Spring传统方式（弃用）
* Spring基于AspectJ的AOP开发

















































