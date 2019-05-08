# Spring的AOP的基于AspectJ注解开发



## Spring的基于ApsectJ的注解的AOP开发

#### 1. 创建项目，引入jar包

#### 2. 引入配置文件

#### 3. 编写目标类并配置

```java
package demo01;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.5.8
 * Time: 21:51
 */
public class OrderDao {

    public void save() {
        System.out.println("==========save===========");
    }

    public void update() {
        System.out.println("==========update===========");
    }

    public String  delete() {
        System.out.println("==========delete===========");
        return "xiaoming";
    }

    public void find() {
        System.out.println("==========find===========");
    }
}
```

```xml
<!--配置目标类-->
<bean id="orderDao" class="demo01.OrderDao"/>
```

#### 4. 编写切面类并配置

#### 5. 使用注解的AOP对象目标类进行增强

* 在配置文件中打开注解的AOP开发

  ```xml
  <!--在配置文件中开启注解AOP开发-->
  <aop:aspectj-autoproxy/>
  ```

* 在切面类上使用注解

  ```java
  package demo01;
  
  import org.aspectj.lang.annotation.Aspect;
  import org.aspectj.lang.annotation.Before;
  
  /**
   * Created by IntelliJ IDEA.
   * User: TLX
   * Date: 2019.5.8
   * Time: 22:59
   */
  @Aspect
  public class MyAspectAnno {
  
      @Before(value = "execution(* demo01.OrderDao.save(..))")
      public void before() {
          System.out.println("=========前置增强============");
      }
  }
  ```

#### 6. 编写测试类

```java
package demo01;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.5.8
 * Time: 23:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestDemo01 {

    @Resource
    private OrderDao orderDao;

    @Test
    public void test01() {
        orderDao.save();
        orderDao.delete();
        orderDao.find();
        orderDao.update();
    }
}
```

* 完整配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/aop
            http://www.springframework.org/schema/aop/spring-aop.xsd
            http://www.springframework.org/schema/tx
            http://www.springframework.org/schema/tx/spring-tx.xsd">


    <!--在配置文件中开启注解AOP开发-->
    <aop:aspectj-autoproxy/>

    <!--配置目标类-->
    <bean id="orderDao" class="demo01.OrderDao"/>

    <!--配置切面类-->
    <bean id="myAspect" class="demo01.MyAspectAnno"/>


</beans>
```



## Spring的注解的AOP的通知类型

### 1.  @Before：前置通知

```java
@Before(value = "execution(* demo01.OrderDao.save(..))")
public void before() {
    System.out.println("=========前置增强============");
}
```

### 2. @AfterReturning：后置通知

```java
@AfterReturning(value = "execution(* demo01.OrderDao.delete(..))", returning = "result")
public void afterRunning(Object result) {
    System.out.println("=========后置增强=========" + result);
}
```

### 3. @Around：环绕通知

```java
@Around(value = "execution(* demo01.OrderDao.update(..))")
public Object around(ProceedingJoinPoint pjp) throws Throwable {
    System.out.println("=========环绕前增强=========" );
    Object proceed = pjp.proceed();
    System.out.println("=========环绕后增强=========");
    return proceed;
}
```

### 4. @AfterThrowing：异常抛出通知

```java
@AfterThrowing(value = "execution(* demo01.OrderDao.find(..))", throwing = "e")
public void afterThrowing(Throwable e) {
    System.out.println("=========异常抛出增强=========" + e.getMessage());
}
```

### 5. @After：最终通知

```java
@After(value = "execution(* demo01.OrderDao.find(..))")
public void after() {
    System.out.println("=========最终增强=========");
}
```



## Spring的注解的AOP的切入点的配置

```java
package demo01;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import javax.xml.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.5.8
 * Time: 22:59
 */
@Aspect
public class MyAspectAnno {

    // @Before(value = "execution(* demo01.OrderDao.save(..))")
    @Before(value = "MyAspectAnno.pointcut3()")
    public void before() {
        System.out.println("=========前置增强============");
    }

    // @AfterReturning(value = "execution(* demo01.OrderDao.delete(..))", returning = "result")
    @AfterReturning(value = "MyAspectAnno.pointcut2()", returning = "result")
    public void afterRunning(Object result) {
        System.out.println("=========后置增强=========" + result);
    }

    // @Around(value = "execution(* demo01.OrderDao.update(..))")
    @Around(value = "MyAspectAnno.pointcut4()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("=========环绕前增强=========" );
        Object proceed = pjp.proceed();
        System.out.println("=========环绕后增强=========");
        return proceed;
    }

    // @AfterThrowing(value = "execution(* demo01.OrderDao.find(..))", throwing = "e")
    @AfterThrowing(value = "MyAspectAnno.pointcut1()", throwing = "e")
    public void afterThrowing(Throwable e) {
        System.out.println("=========异常抛出增强=========" + e.getMessage());
    }

    // @After(value = "execution(* demo01.OrderDao.find(..))")
    @After(value = "MyAspectAnno.pointcut1()")
    public void after() {
        System.out.println("=========最终增强=========");
    }

    // 切入点配置
    @Pointcut(value = "execution(* demo01.OrderDao.find(..))")
    private void pointcut1() {}
    @Pointcut(value = "execution(* demo01.OrderDao.delete(..))")
    private void pointcut2() {}
    @Pointcut(value = "execution(* demo01.OrderDao.save(..))")
    private void pointcut3() {}
    @Pointcut(value = "execution(* demo01.OrderDao.update(..))")
    private void pointcut4() {}
}
```

