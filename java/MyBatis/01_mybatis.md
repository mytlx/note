### #{}和${}

* \#{}表示一个占位符号，通过#{}可以实现preparedStatement向占位符中设置值，自动进行java类型和jdbc类型转换。
  * \#{}可以有效防止sql注入。
  * \#{}可以接收简单类型值或pojo属性值。
  * 如果parameterType传输单个简单类型值，#{}括号中可以是value或其它名称。

* \${}表示拼接sql串，通过\${}可以将parameterType传入的内容拼接在sql中且不进行jdbc类型转换
  * \${}可以接收简单类型值或pojo属性值
  * 如果parameterType传输单个简单类型值，\${}括号中只能是value。

### parameterType和resultType

* parameterType：指定输入参数类型，mybatis通过ognl从输入对象中获取参数值拼接在sql中。
* resultType：指定输出结果类型，mybatis将sql查询结果的一行记录数据映射为resultType指定类型的对象。如果有多条数据，则分别进行映射，并把对象放到容器List中

### selectOne和selectList

* ѕеlесtОnе：查询一条记录，如果使用selectOne查询多条记录则抛出异常

```java
org.apache.ibatis.exceptions.TooManyResultsException: Expectedone result (or null) to be returned by select0ne(), but found: 3
      at
org.apache.ibatis.session.defaults.DefaultSqlSession.selectOne(DefaultSqlSession.java:70)
```

* ѕеlесtLіѕt：可以查询一条或多条记录



## SqlMapConfig.xml配置文件

### 1. 配置内容

SqlMapConfig.xml中配置的内容和顺序如下：

* **properties（属性）**
* settings（全局配置参数）
* **typeAliases（类型别名）**
* typeHandlers（类型处理器）
* objectFactory（对象工厂）
* plugins（插件）
* environments（环境集合属性对象）
  * environment（环境子属性对象）
    * transactionManager（事务管理）
    * DataSource（数据源）
* **mappers（映射器）**

### 2. properties（属性）

SqlMapConfig.xml可以引用java属性文件中的配置信息如下：

定义jdbc.properties文件：

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/mybatis?useSSL=false&serverTimezone=GMT
jdbc.username=root
jdbc.password=8372
```

SqlMapConfig.xml引用如下：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTDConfig 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--用resource加载外部配置文件-->
    <properties resource="jdbc.properties">
        <!--在properties内部用property定义属性-->
        <!--如果外部文件配置了该属性，那么该属性被外部配置文件覆盖-->
        <property name="jdbc.username" value="root123"/>
        <property name="jdbc.password" value="root123"/>
    </properties>
	
    <environments default="development">
        <environment id="development">
            <!--使用jdbc事务管理-->
            <transactionManager type="JDBC"/>
            <!--数据库连接池-->
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>

    <!--加载配置文件-->
    <mappers>
        <mapper resource="sqlmap/User.xml"/>
        <mapper resource="mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```

* MyBatis将按照下面的顺序来加载属性：
  * 在properties元素体内定义的属性首先被读取
  * 然后会读取properties元素中国resource或url加载的属性，它会**覆盖已读取的同名属性**

### 3. typeAliases（类型别名）

#### 3.1 mybatis支持别名：

| 别名       | 映射的类型 |
| ---------- | ---------- |
| \_byte     | byte       |
| \_long     | long       |
| \_short    | short      |
| \_int      | int        |
| \_integer  | int        |
| \_double   | double     |
| \_float    | float      |
| \_boolean  | boolean    |
| string     | String     |
| byte       | Byte       |
| long       | Long       |
| short      | Short      |
| int        | Integer    |
| integer    | Integer    |
| double     | Double     |
| float      | Float      |
| boolean    | Boolean    |
| date       | Date       |
| decimal    | BigDecimal |
| bigdecimal | BigDecimal |
| map        | Map        |

#### 3.2 自定义别名：

```xml
<typeAliases>
    <!--单个别名定义-->
    <typeAlias alias="user" type="com.mytlx.User" />
    <!--批量别名定义，扫描整个包下的类，别名为类型（大小写不敏感）-->
    <package name="com.mytlx"/>
    <package name="com.mytlx.mapper"/>
    <package name="com.mytlx.dao"/>
</typeAliases>
```

在`mapper.xml`配置文件中，就可以使用设置的别名了。

别名大小写不敏感。

如下面的resultType：

```xml
<select id="queryUserById" parameterType="int" resultType="UsEr">
    select *
    from user
    where id = #{id}
</select>
```



### 4. mappers（映射器）

Mapper配置的几种方法：

#### 4.1 \<mapper resource=" "/\>

使用相对类路径的资源。

```xml
<mapper resource="sqlmap/User.xml" />
```

#### 4.2 \<mapper class=" "/\>

使用mapper接口类路径。

```xml
<mapper class="com.mytlx.mapper.UserMapper" />
```

注：此方法要求mapper接口名称和mapper映射文件名称相同，且放在同一个目录中。

#### 4.3 \<package name=" " />

注册指定包下的所有mapper接口。

```xml
<package name="com.mytlx.mapper" />
```

注：此方法要求mapper接口名称和mapper映射文件名称相同，且放在同一个目录中。







## Mapper XML 文件

SQL 映射文件有很少的几个顶级元素（按照它们应该被定义的顺序）：

- `cache` – 给定命名空间的缓存配置。
- `cache-ref` – 其他命名空间缓存配置的引用。
- `resultMap` – 是最复杂也是最强大的元素，用来描述如何从数据库结果集中来加载对象。
- `sql` – 可被其他语句引用的可重用语句块。
- `insert` – 映射插入语句
- `update` – 映射更新语句
- `delete` – 映射删除语句
- `select` – 映射查询语句



### 1. mapper

```xml
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





### 2. select

#### 根据id查询用户

```xml
<!--根据用户id查询用户-->
<select id="queryUserById" parameterType="int" resultType="com.mytlx.User">
    select *
    from user
    where id = #{id}
</select>
```

#### 根据用户名模糊查询

```xml
<!--根据用户名实现模糊查询-->
<!--方法一：-->
<!--如果返回多个结果，mybatis会自动把返回的结果放在list容器中-->
<!--运行时传入参数：queryUserByUsername("%小%") -->
<select id="queryUserByUsername1" parameterType="string" resultType="com.mytlx.User">
    select *
    from user
    where username like #{username}
</select>
<!--方法二：-->
<!--如果传入的参数是简单数据类型，${}里面必须写value-->
<!--运行时传入参数：queryUserByUsername("小") -->
<select id="queryUserByUsername2" parameterType="string" resultType="com.mytlx.User">
    select *
    from user
    where username like '%${value}%'
</select>
```



## 输入映射和输出映射

### 1. parameterType（输入类型）

#### 1.1 传递简单类型

使用#{}占位符，或者\${}进行sql拼接

```xml
<!--根据用户id查询用户-->
<select id="queryUserById" parameterType="int" resultType="com.mytlx.User">
    select *
    from user
    where id = #{id}
</select>
```

#### 1.2 传递pojo对象

使用ognl表达式解析对象字段的值，`#{}` 或 `${}`括号中的值为pojo属性值

```xml
<!--更新用户-->
<update id="updateUserById" parameterType="com.mytlx.User">
    update user
    set username = #{username}
    where id = #{id}
</update>
```

#### 1.3 传递pojo包装对象

包装对象：pojo类中的一个属性是另一个pojo

```xml
<!--1. resultType:如果要返回数据集合，只需设定为每一个元素的数据类型-->
<!--2. 包装的pojo取值通过 "."来获取-->
<select id="queryUserByQueryVo" parameterType="queryVo" resultType="User">
    <!-- SELECT * FROM USER WHERE username LIKE #{name} -->
    SELECT * FROM USER WHERE username LIKE '%${user.username}%'
</select>
```



### 2. resultType（输出类型）

#### 2.1 输出简单类型

```xml
<!--查询用户表数据条数-->
<select id="queryUserCount" resultType="int">
    select count(*)
    from user
</select>
```

#### 2.2 输出pojo对象

```xml
<select id="queryUserById" parameterType="int" resultType="com.mytlx.User">
    select *
    from user
    where id = #{id}
</select>
```

#### 2.3 输出pojo列表

```xml
<select id="queryUserByUsername1" parameterType="string" resultType="com.mytlx.User">
    select *
    from user
    where username like #{username}
</select>
```



### 3. resultMap

resultType可以指定将结果映射为pojo，但需要pojo的属性名和sql查询到的列名一致方可映射成功。

如果sql查询字段名和pojo属性名不一致，可以通过resultMap将字段名和属性名做一个对应关系，resultMap实质上还需要将查询结果映射到pojo对象中。

resultMap可以实现将查询结果映射为复杂类型的pojo，比如在查询结果映射对象中包括pojo和list实现一对一查询和一对多查询。

```xml
<!--resultMap最终还是要将结果映射到pojo上，type就是指定映射到哪个pojo上-->
<!--id：设置ResultMap的id-->
<resultMap id="orderResultMap" type="order">
    <!--定义主键，非常重要，如果是多个字段，则定义多个id-->
    <!--property：主键在pojo中的属性名-->
    <!--column：主键在数据库中的列名-->
    <id property="id" column="id"/>

    <!--定义普通属性-->
    <result property="userId" column="user_id"/>
    <result property="number" column="number"/>
    <result property="createTime" column="createtime"/>
    <result property="note" column="note"/>
</resultMap>

<!--查询所有的订单数据-->
<select id="queryOrderAll" resultMap="orderResultMap">
    select id, user_id, number, createtime, note
    from `order`
</select>
```





## 关联查询

### 1. 两种一对一查询的方法：

#### 1.1 resultType

改造pojo，新建pojo名为OrderUser继承自Order，自然包含了order的所有字段，再添加user中的字段即可。

```java
public class OrderUser extends Order {
    private String username;
    private String address;
}
```

```xml
<!-- 一对一关联查询，使用resultType -->
<select id="queryOrderUser" resultType="orderuser">
    SELECT o.id, o.user_id userId, o.number, o.createtime, o.note, u.username, u.address
    FROM `order` o
    LEFT JOIN user u ON u.id = o.user_id
</select>
```

#### 1.2 resultMap

改造order，在order中添加user属性。

```java
public class Order {
    private int id;
    private Integer userId;
    private String number;
    private Date createTime;
    private String note;
    
	// 添加user属性
    private User user;
}
```

```xml
<resultMap id="orderUserResultMap" type="order">
    <!--定义主键，非常重要，如果是多个字段，则定义多个id-->
    <!--property：主键在pojo中的属性名-->
    <!--column：主键在数据库中的列名-->
    <id property="id" column="id"/>

    <!--定义普通属性-->
    <result property="userId" column="user_id"/>
    <result property="number" column="number"/>
    <result property="createTime" column="createtime"/>
    <result property="note" column="note"/>

    <!--association：配置一对一关联-->
    <!--property：绑定的用户属性-->
    <!--javaType：属性数据类型，支持别名-->
    <association property="user" javaType="user">
        <!--id：声明主键，表示user_id是关联查询对象的唯一标识-->
        <id property="id" column="user_id"/>

        <result property="username" column="username"/>
        <result property="address" column="address"/>
        <result property="sex" column="sex"/>
    </association>
</resultMap>

<!-- 一对一关联查询-使用resultMap -->
<select id="queryOrderUser2" resultMap="orderUserResultMap">
    SELECT o.id,
    o.user_id,
    o.number,
    o.createtime,
    o.note,
    u.username,
    u.address,
    u.sex
    FROM `order` o
    LEFT JOIN `user` u ON u.id = o.user_id
</select>
```



### 一对多关联查询

修改pojo，user类中添加`List<Order> orders;`属性

```java
public class User {
    private int id;
    private String username;
    private Date birthday;
    private String sex;
    private String address;

    private List<Order> orders;
}
```

```xml
<resultMap id="userOrderResultMap" type="user">
    <!--定义主键，非常重要，如果是多个字段，则定义多个id-->
    <!--property：主键在pojo中的属性名-->
    <!--column：主键在数据库中的列名-->
    <id property="id" column="id"/>

    <!--定义普通属性-->
    <result property="username" column="username"/>
    <result property="birthday" column="birthday"/>
    <result property="sex" column="sex"/>
    <result property="address" column="address"/>

    <!--collection：配置一对多的关系-->
    <!--property：绑定的用户属性-->
    <!--javaType：属性数据类型，支持别名-->
    <collection property="orders" javaType="list" ofType="order">
        <!--id：声明主键，表示user_id是关联查询对象的唯一标识-->
        <id property="id" column="oid"/>

        <result property="number" column="username"/>
        <result property="createTime" column="createtime"/>
        <result property="note" column="note"/>
    </collection>
</resultMap>

<!-- 一对多关联，查询订单同时查询该用户下的订单 -->
<select id="queryUserOrder" resultMap="userOrderResultMap">
    SELECT u.id,
    u.username,
    u.birthday,
    u.sex,
    u.address,
    o.id oid,
    o.number,
    o.createtime,
    o.note
    FROM `user` u
    LEFT JOIN `order` o ON u.id = o.user_id
</select>
```



## 动态sql

### 1. if标签

```xml
<!--根据条件查询用户-->
<select id="queryUserByWhere" parameterType="user" resultType="user">
    <!--select id, username, birthday, sex, address-->
    <!--from user-->
    <!--where sex = #{sex}-->
    <!--and username like '%${username}%'-->
    select
    <include refid="userFields"/>	<!--sql片段-->
    from user
    where 1 = 1
    <if test="sex != null and sex != ''">
        and sex = #{sex}
    </if>
    <if test="username != null and username != ''">
        and username like '%${username}%'
    </if>
</select>
```

### 2. sql片段

```xml
<!--声明sql片段-->
<sql id="userFields">
    id, username, birthday, sex, address
</sql>
```

```xml
<sql id="userColumns"> 
    ${alias}.id,${alias}.username,${alias}.password 
</sql>
```

```xml
<select id="selectUsers" resultType="map">
  select
    <include refid="userColumns"><property name="alias" value="t1"/></include>,
    <include refid="userColumns"><property name="alias" value="t2"/></include>
  from some_table t1
    cross join some_table t2
</select>
```

### 3. where标签

```xml
<!--根据条件查询用户-->
<select id="queryUserByWhere" parameterType="user" resultType="user">
    <!--select id, username, birthday, sex, address-->
    <!--from user-->
    <!--where sex = #{sex}-->
    <!--and username like '%${username}%'-->
    select
    <include refid="userFields"/>	<!--sql片段-->
    from user
    <!--where标签可以自动添加where，同时处理sql语句中第一个and关键字-->
    <where>
        <if test="sex != null and sex != ''">
            and sex = #{sex}
        </if>
        <if test="username != null and username != ''">
            and username like '%${username}%'
        </if>
    </where>
</select>
```

### 4. foreach标签

```xml
<!--根据ids查询用户-->
<select id="queryUserByIds" resultType="user" parameterType="queryVo">
    select * from user
    <where>
        <!--foreach标签，进行遍历-->
        <!--collection：遍历的集合，这里是QueryVo的ids属性-->
        <!--item：遍历的项目，可以随便写，但是和后面#{}里面要一致-->
        <!--open：在前面添加的sql片段-->
        <!--close：在结尾处添加的sql片段-->
        <!--separator：指定遍历的元素之间使用的分隔符-->
        <foreach collection="ids" item="item" open="id IN (" close=")" separator=",">
            #{item}
        </foreach>
    </where>
</select>
```











