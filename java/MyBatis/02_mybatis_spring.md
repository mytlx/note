# mybatis整合spring

## 1. 整合思路

* SqlSessionFactory对象应该放到spring容器中作为单例存在
* 传统dao的开发方式中，应该从spring容器中获得sqlSession对象
* Mapper代理形式中，应该从spring容器中直接获得mapper的代理对象
* 数据库的连接以及数据库连接池事务管理都交给spring容器来完成



## 2. 整合需要的jar包

* spring的jar包
* mybatis的jar包
* spring+mybatis的整合包
* mysql的数据库驱动jar包
* 数据库连接池的jar包

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.mytlx</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <!-- Spring依赖 -->
        <!-- 1.Spring核心依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <!-- 2.Spring dao依赖 -->
        <!-- spring-jdbc包括了一些如jdbcTemplate的工具类 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <!-- 3.Spring web依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>
        <!-- 4.Spring test依赖：方便做单元测试和集成测试 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>4.3.7.RELEASE</version>
        </dependency>

        <!--连接池-->
        <dependency>
            <groupId>commons-dbcp</groupId>
            <artifactId>commons-dbcp</artifactId>
            <version>1.4</version>
        </dependency>
        <dependency>
            <groupId>commons-pool</groupId>
            <artifactId>commons-pool</artifactId>
            <version>1.6</version>
        </dependency>

        <!--单元测试-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.3</version>
            <scope>test</scope>
        </dependency>

        <!--mybatis需要的依赖包-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.6</version>
        </dependency>
        <dependency>
            <groupId>asm</groupId>
            <artifactId>asm</artifactId>
            <version>3.3.1</version>
        </dependency>

        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>2.2.2</version>
        </dependency>

        <dependency>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
            <version>1.1.1</version>
        </dependency>
        <dependency>
            <groupId>org.javassist</groupId>
            <artifactId>javassist</artifactId>
            <version>3.17.1-GA</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.13</version>
        </dependency>

        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.7</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.5</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.5</version>
            <scope>test</scope>
        </dependency>

        <!--整合包-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.3.2</version>
        </dependency>

    </dependencies>

    <!--java和resources下的任何目录下的配置文件都可以被读取-->
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                </includes>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>9</source>
                    <target>9</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```



## 3. 整合步骤

### 3.1 配置文件

* mybatis的配置文件
* spring的配置文件
  * 数据库连接及连接池
  * 事务管理（暂时可以不配置）
  * sqlSessionFactory对象，配置到spring容器中
  * mapper代理对象或者是dao实现类配置到spring容器中

#### 3.1.1 SqlMapConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>


</configuration>
```

#### 3.1.2 applicationContext.xml

* SqlSessionFactoryBean属于mybatis-spring这个jar包

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context
            http://www.springframework.org/schema/context/spring-context.xsd">


    <!--加载配置文件-->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!--配置DBCP连接池-->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
        <!--连接池的最大数据库连接数-->
        <property name="maxActive" value="10"/>
        <!--最大空闲数-->
        <property name="maxIdle" value="5"/>
    </bean>


    <!--配置SqlSessionFactory-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--配置mybatis核心配置文件-->
        <property name="configLocation" value="classpath:SqlMapConfig.xml"/>
        <!--配置数据源-->
        <property name="dataSource" ref="dataSource"/>
        <!--配置别名包扫描-->
        <property name="typeAliasesPackage" value="com.mytlx"/>
    </bean>

</beans>
```

#### 3.1.3 jdbc.properties

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis?useSSL=false&serverTimezone=GMT
jdbc.username=root
jdbc.password=8372
```

#### 3.1.4 log4j.properties

```properties
# Global logging configuration
log4j.rootLogger=DEBUG, stdout

# Console output..
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.ConversionPattern=%5p [%t] - %m%n
```



## 4. Dao的开发

两种dao的实现方式：

1. 原始dao的开发方式
2. 使用Mapper代理形式开发方式
   1. 直接配置Mapper代理
   2. 使用扫描包配置Mapper代理

功能：

1. 实现根据用户id查询
2. 实现根据用户名模糊查询
3. 添加用户



### 4.1 创建pojo

```java
package com.mytlx;

import java.util.Date;

/**
 * @author TLX
 * @date 2019.5.29
 * @time 22:44
 */
public class User {
    private int id;
    private String username;
    private Date birthday;
    private String sex;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
```

### 4.2 传统dao的开发方式

原始的Dao开发接口+实现类来完成。

需要dao实现类继承SqlSessionDaoSupport类。

#### 4.2.1 实现Mapper.xml

User.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--命名空间，用于隔离sql-->
<mapper namespace="test">
    <!--id：statement的id或者sql的id-->
    <!--parameterType：输入参数的类型-->
    <!--resultType：输出结果的类型，应该写pojo的全路径-->
    <!--#{}：输入参数的占位符-->
    <select id="queryUserById" parameterType="int" resultType="com.mytlx.User">
        select *
        from user
        where id = #{id}
    </select>

    <!--根据用户名实现模糊查询-->
    <!--方法一：-->
    <!--如果返回多个结果，mybatis会自动把返回的结果放在list容器中-->
    <select id="queryUserByUsername1" parameterType="string" resultType="com.mytlx.User">
        select *
        from user
        where username like #{username}
    </select>
    <!--方法二：-->
    <!--如果传入的参数是简单数据类型，${}里面必须写value-->
    <select id="queryUserByUsername2" parameterType="string" resultType="com.mytlx.User">
        select *
        from user
        where username like '%${value }%'
    </select>

    <!--保存用户-->
    <insert id="saveUser" parameterType="com.mytlx.User">
        <!-- selectKey：标签实现主键返回-->
        <!--keyColumn：主键对应的表中的哪一列-->
        <!--keyProperty：主键对应的pojo的哪一个属性-->
        <!--order：设置在执行insert语句前执行查询id的sql，还是在执行insert语句之后执行查询id的sql-->
        <selectKey keyColumn="id" keyProperty="id" order="AFTER" resultType="int">
            select LAST_INSERT_ID()
        </selectKey>

        insert into user (username, birthday, sex, address)
        values (#{username}, #{birthday}, #{sex}, #{address})
    </insert>

</mapper>
```

#### 4.2.2 加载Mapper.xml

SqlMapConfig

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>
    <mappers>
        <mapper resource="sqlmap/User.xml"/>
    </mappers>

</configuration>
```

#### 4.2.3 实现UserDao接口

```java
package com.mytlx.dao;

import com.mytlx.User;

import java.util.List;

/**
 * @author TLX
 * @date 2019.5.30
 * @time 15:01
 */
public interface UserDao {

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    User queryUserById(int id);

    /**
     * 根据用户名模糊查询用户
     *
     * @param username
     * @return
     */
    List<User> queryUserByUsername(String username);

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(User user);

}
```

#### 4.2.4 实现UserDaoImpl实现类

* 必须继承SqlSessionDaoSupport类
* SqlSessionDaoSupport提供getSqlSession()方法来获取SqlSession

```java
package com.mytlx.dao;

import com.mysql.cj.xdevapi.SessionFactory;
import com.mytlx.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

/**
 * @author TLX
 * @date 2019.5.30
 * @time 15:03
 */
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public User queryUserById(int id) {
        // 创建sqlSession
        SqlSession sqlSession = super.getSqlSession();
        // 执行查询逻辑
        return sqlSession.selectOne("queryUserById", id);

        // 不要关闭sqlSession
        // sqlSession.close();
    }

    /**
     * 根据用户名模糊查询用户
     *
     * @param username
     * @return
     */
    @Override
    public List<User> queryUserByUsername(String username) {
        // 创建sqlSession
        SqlSession sqlSession = super.getSqlSession();

        // 执行查询逻辑
        return sqlSession.selectList("queryUserByUsername2", username);
    }

    /**
     * 保存用户
     *
     * @param user
     */
    @Override
    public void saveUser(User user) {
        // 创建sqlSession
        SqlSession sqlSession = super.getSqlSession();

        // 执行保存逻辑
        sqlSession.insert("saveUser", user);

        // 不用提交事务，事务由spring管理
        // sqlSession.commit();

        // 不要释放资源
        // sqlSession.close();
    }
}
```

#### 4.2.5 在applicationContext.xml中配置dao

```xml
<!--原始方式开发dao，配置dao到spring中-->
<bean id="userDao" class="com.mytlx.dao.UserDaoImpl">
    <!--配置sqlSessionFactory-->
    <property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>
```

#### 4.2.6 生成测试方法

可以直接在UserDao接口上按`alt+Enter`直接生成。

```java
package com.mytlx.dao;

import com.mytlx.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author TLX
 * @date 2019.5.31
 * @time 20:11
 */
public class UserDaoTest {
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        this.applicationContext = new
                ClassPathXmlApplicationContext("classpath:applicationContext.xml");

    }

    @Test
    public void queryUserById() {
        // 获取userDao
        UserDao bean = this.applicationContext.getBean(UserDao.class);
        User user = bean.queryUserById(1);
        System.out.println(user);
    }

    @Test
    public void queryUserByUsername() {
        // 获取userDao
        UserDao bean = this.applicationContext.getBean(UserDao.class);
        List<User> list = bean.queryUserByUsername("小");
        list.forEach(System.out::println);
    }

    @Test
    public void saveUser() {
        // 获取userDao
        UserDao bean = this.applicationContext.getBean(UserDao.class);
        User user = new User();
        user.setUsername("李四");
        user.setSex("1");
        user.setBirthday(new Date());
        user.setAddress("shanghai");

        bean.saveUser(user);
        System.out.println("user = " + user);
    }
}
```



### 4.3 Mapper代理形式开发dao

#### 4.3.1 实现Mapper.xml

UserMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--namespace：命令空间，用于隔离sql-->
<!--还有一个重要作用，使用动态代理开发Dao-->
<!--1. namespace必须和Mapper接口类路径一致-->
<mapper namespace="com.mytlx.mapper.UserMapper">
    <!--2. id必须和Mapper接口方法名一致-->
    <!--3. parameterType必须和接口方法参数类型一致-->
    <!--4. resultType必须和接口方法返回值一致-->
    <!--根据用户id查询用户-->
    <select id="queryUserById" parameterType="int" resultType="com.mytlx.User">
        select *
        from user
        where id = #{id}
    </select>
    <!--根据用户id查询用户-->
    <select id="queryUserByUsername" parameterType="string" resultType="com.mytlx.User">
        select *
        from user
        where username like '%${value}%'
    </select>
    <!--保存用户-->
    <insert id="saveUser" parameterType="com.mytlx.User">
        <selectKey keyProperty="id" keyColumn="id" order="AFTER" resultType="int">
            select last_insert_id()
        </selectKey>
        insert into user(username, birthday, sex, address)
        VALUES (#{username},#{birthday},#{sex},#{address})
    </insert>
</mapper>
```

#### 4.3.2 实现UserMapper接口

```java
package com.mytlx.mapper;

import com.mytlx.User;

import java.util.List;

/**
 * @author TLX
 * @date 2019.5.30
 * @time 21:50
 */
public interface UserMapper {
    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    User queryUserById(int id);

    /**
     * 根据用户名模糊查询
     *
     * @param username
     * @return
     */
    List<User> queryUserByUsername(String username);

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(User user);
}
```

#### 4.3.3 配置Mapper

##### 1. 单个接口配置MapperFactoryBean

applicationContext.xml添加

```xml
<!-- 动态代理Dao开发，第一种方式：MapperFactoryBean -->
<bean id="baseMapper" class="org.mybatis.spring.mapper.MapperFactoryBean" abstract="true" lazy-init="true">
    <property name="sqlSessionFactory" ref="sqlSessionFactory" />
</bean>
<!-- 用户动态代理扫描 -->
<bean parent="baseMapper">
    <property name="mapperInterface" value="com.mytlx.mapper.UserMapper" />
</bean>
```

##### 2. 配置包扫描器

applicationContext.xml添加

```xml
<!-- 动态代理Dao开发，第二种方式：包扫描器(推荐使用) -->
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <!-- basePackage：配置映射包装扫描，多个包时用","或";"分隔 -->
    <property name="basePackage" value="com.mytlx.mapper" />
</bean>
```

每个mapper代理对象的id就是类名，首字母小写

#### 4.3.4 生成测试方法

```java
package com.mytlx.mapper;

import com.mytlx.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author TLX
 * @date 2019.5.31
 * @time 22:20
 */
public class UserMapperTest {
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        this.applicationContext = new
                ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    @Test
    public void queryUserById() {
        // 获取mapper
        UserMapper bean = this.applicationContext.getBean(UserMapper.class);

        User user = bean.queryUserById(1);
        System.out.println(user);
    }

    @Test
    public void queryUserByUsername() {
        // 获取mapper
        UserMapper bean = this.applicationContext.getBean(UserMapper.class);

        List<User> list = bean.queryUserByUsername("小");
        list.forEach(System.out::println);
    }

    @Test
    public void saveUser() {
        // 获取mapper
        UserMapper bean = this.applicationContext.getBean(UserMapper.class);

        User user = new User();
        user.setUsername("李四");
        user.setSex("1");
        user.setBirthday(new Date());
        user.setAddress("shanghai");

        bean.saveUser(user);
        System.out.println("user = " + user);
    }
}
```

