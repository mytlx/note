### 不存在@Resource注解等，包括什么@PostConstruct

在project structure中add library，from maven，搜索：javax.annotation：jsr250-api添加过后即可。



### Checks autowiring problems in a bean class错误

```java
package demo03;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.4.21
 * Time: 20:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Configuration("classpath:applicationContext.xml")
public class Test01 {

    @Resource(name="productDao")
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

@Configuration改成@ContextConfiguration



### \=\=\=\=\=\=\=\=\=\=\=\=\=\=

```java
Caused by: java.lang.ClassNotFoundException: org.aspectj.lang.JoinPoint
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name
```

缺少aspectjweaver.jar和aspectjrtweaver.jar包 