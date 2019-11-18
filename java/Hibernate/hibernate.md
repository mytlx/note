* ORM：object relation mapping 对象关系映射	

  将一个java中的对象与关系型数据库中的表建一种映射关系，从而操作对象就可以操作数据库中的表。



## IDEA创建Hibernate

1. 勾选download可以自动下载hibernate所需的jar包，勾选中间项可以自动生成cfg配置文件

![create](https://i.imgur.com/Nf3rcfn.png)



2. 添加mysql驱动包

   ![mysql_driver](https://i.imgur.com/u78qWap.png)

3. idea中连接数据库

4. 配置核心配置文件

   - 如果创建项目时没勾选自动生成cfg，可以通过以下方法生成，**注意要切换到src目录**

     ![cfg](https://i.imgur.com/YPUwiXS.png)

   - 重要配置如下

     ```xml
     <?xml version='1.0' encoding='utf-8'?>
     <!DOCTYPE hibernate-configuration PUBLIC
             "-//Hibernate/Hibernate Configuration DTD//EN"
             "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
     <hibernate-configuration>
         <session-factory>
             <!-- 配置数据库信息 -->
             <property name="connection.url">jdbc:mysql://localhost:3306/hibernate01?useSSL=false&amp;serverTimezone=GMT</property>
             <property name="connection.driver_class">com.mysql.jdbc.Driver</property>
             <!-- 用户名密码要在mapping前面添加，不然会报错 -->
             <property name="connection.username">root</property>
             <property name="connection.password">8372</property>
     
             <!-- 配置数据库方言 -->
             <property name="dialect">org.hibernate.dialect.MySQLDialect</property>
     
             <!-- 可选 -->
             <property name="show_sql">true</property>
             <property name="format_sql">true</property>
             
             <!-- 映射关系，后续步骤可自动生成 -->
             <mapping resource="dao/CstCustomerDao.hbm.xml"/>
             <mapping class="dao.CstCustomerDao"/>
     
             <!-- DB schema will be updated if needed -->
             <!-- <property name="hbm2ddl.auto">update</property> -->
         </session-factory>
     </hibernate-configuration>
     ```

5. Persistence活动窗口中，自动生成mapping

   ![generate](https://i.imgur.com/WqwyglV.png)

6. 根据图中选项，生成实体类和关系

   ![generate2](https://i.imgur.com/S1fs1CB.png)

7. catalog配置为和schema同名即可，即数据库名

   ![catalog](https://i.imgur.com/Jcla8v7.png)

8. 编写测试类

   ```java
   public class test01 {
   
       @Test
       public void demo01() {
           // 1. 加载Hibernate的核心配置文件
           Configuration configuration = new Configuration().configure();
           // 2. 创建一个SessionFactory对象，类似于JDBC中连接池
           SessionFactory sessionFactory = configuration.buildSessionFactory();
           // 3. 通过SessionFactory获取Session对象，类似于JDBC中Connection
           Session session = sessionFactory.openSession();
           // 4. 手动开启事务
           Transaction transaction = session.beginTransaction();
   
           // 5. 编写代码
           CstCustomerDao cstCustomerDao = new CstCustomerDao();
           // cstCustomerDao.setCustId(1);
           cstCustomerDao.setCustName("Mary");
           session.save(cstCustomerDao);
   
           // 6. 事务提交
           transaction.commit();
           // 7. 资源释放
           session.close();
       }
   }
   ```

9. 测试成功

   ![test01](https://i.imgur.com/QSYAIbn.png)

   ![test01_2](https://i.imgur.com/q8wKQGc.png)





## Hibernate的映射配置

* class标签
  * 用来建立类与表的映射关系
  * 属性：
    * name：类的全路径
    * table：表名（表名和类名一致，表名可以省略）
    * catalog | schema：数据库名
* id标签
  * 用来建立类中的属性与表中的主键的对应关系
  * 属性：
    * name：类中属性名
    * column：表中字段名（类中的属性名和表中的字段名一致，column可以省略）
    * length：长度
    * type：类型
* property标签
  * 用来建立类中的普通属性与表中字段的对应关系
  * 属性：
    * name：类中的属性名
    * column：表中字段名
    * length：长度
    * type：类型
    * not null：设置非空
    * unique：设置唯一



## Hibernate的核心配置

### 核心配置方式

* 属性文件
  * hibernate.properties
    * hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver
    * ...
    * hibernate.show_sql=true
  * **属性文件的方式不能引入映射文件（手动编写代码加载映射文件）**
* **xml文件**
  * **hibernate.cfg.xml**



### 核心配置

* 必须配置

  * 连接数据库的基本参数
    * 驱动类
    * url路径
    * 用户名
    * 密码
  * 方言

* 可选配置

  * 显示SQL：hibernate.show_sql
  * 格式化SQL：hibernate.format_sql
  * 自动建表：hibernate.hbm2ddl.auto
    * none：不使用hibernate建表
    * create：如果数据库中已经有表，删除原有表，重新创建；如果没有表，新建表（用于测试）
    * create-drop：如果数据库中已经有表，删除原有表，执行操作，删除这个表；如果没有表，新建一个，使用完了删除该表（用于测试）
    * **update：**如果数据库中有表，使用原有表；如果没有表，创建新表（更新表结构）
    * **validate：**如果没有表，不会创建表，只会使用数据库中原有的表（校验映射和表结构）

* 映射文件引入

  * 引入映射文件的位置

    ```xml
    <mapping resource="dao/CstCustomerDao.hbm.xml"/>
    ```



## Hibernate核心API

### Configuration：Hibernate的配置对象

>  Configuration类的作用是对hibernate进行配置，以及对它进行启动。在Hibernate的启动过程中，Configuration类的实例首先定位映射文档的位置，读取这些配置，然后创建一个SessionFactory对象。虽然Configuration类在整个Hibernate项目中只扮演一个很小的角色，但它是启动Hibernate时所遇到的第一个对象。

* 作用：

  * 加载核心配置文件

    * hibernate.properties

      ```java
      Configuration cfg = new Configuration();
      ```

    * **hibernate.cfg.xml**

      ```java
      Configuration cfg = new Configuration().configure();
      ```

  * 加载映射文件

    * 手动加载映射文件

      ```java
      configuration.addResource("dao/CstCustomerDao.hbm.xml");
      ```



### SessionFactory：Session工厂

> SessionFactory接口负责初始化Hibernate。它充当数据存储源的代理，并负责创建Session对象。这里用到了工厂模式。需要注意的是SessionFactory并不是轻量级的，因为一般情况下，一个项目通常只需要一个SessionFactory就够，当需要操作多个数据库时，可以为每个数据库指定一个SessionFactory。

SessionFactory内部维护了Hibernate的连接池和二级缓存，是线程安全的对象，一个项目创建一个对象即可。

* 配置连接池（了解）

  ```xml
  <!-- 配置C3P0连接池 -->
  		<property name="connection.provider_class">org.hibernate.connection.C3P0ConnectionProvider</property>
  		<!--在连接池中可用的数据库连接的最少数目 -->
  		<property name="c3p0.min_size">5</property>
  		<!--在连接池中所有数据库连接的最大数目  -->
  		<property name="c3p0.max_size">20</property>
  		<!--设定数据库连接的过期时间,以秒为单位,
  		如果连接池中的某个数据库连接处于空闲状态的时间超过了timeout时间,就会从连接池中清除 -->
  		<property name="c3p0.timeout">120</property>
  		 <!--每3000秒检查所有连接池中的空闲连接 以秒为单位-->
  		<property name="c3p0.idle_test_period">3000</property>
  
  ```

* 抽取工具类

  ```java
  public class HibernateUtils {
      private static final Configuration cfg;
      private static final SessionFactory sf;
  
      static {
          cfg = new Configuration().configure();
          sf = cfg.buildSessionFactory();
      }
  
      public static Session openSession() {
          return sf.openSession();
      }
  }
  ```



### Session：类似Connection对象是连接对象

> Session接口负责执行持久化对象的CRUD操作，但需要注意的是Session对象是非线程安全的。同时，Hibernate的session不同于JSP应用中的HTTPSession。这里当使用session这个术语时，其实指的是Hibernate中的session，而以后会将HTTPSession对象称为用户session。

Session代表的是Hibernate与数据库的链接对象。不是线程安全的。与数据库交互桥梁。

* session中的API

  * 保存方法：

    * Serializable save(Object obj);

  * **查询方法**

    * T get(Class c,Serializable id);
    * T load(Class c,Serializable id);

  * 修改方法

    * void update(Object obj);

      ```java
      @Test
      public void test3() {
          Session session = HibernateUtils.openSession();
          Transaction transaction = session.beginTransaction();
      
          // 直接创建对象，进行修改
          CstCustomerDao dao = new CstCustomerDao();
          dao.setCustId(1L);
          dao.setCustName("update");
          session.update(dao);
      
          // 先查询，再修改（推荐）
          CstCustomerDao dao1 = session.get(CstCustomerDao.class, 1L);
          dao1.setCustName("get_update");
          session.update(dao1);
      
          transaction.commit();
          session.close();
      }
      ```

  * 删除方法

    * void delete(Object obj);

      ```java
      @Test
      public void test4() {
      Session session = HibernateUtils.openSession();
      Transaction transaction = session.beginTransaction();
      
      // 直接创建对象，删除
      CstCustomerDao dao = new CstCustomerDao();
      dao.setCustId(1L);
      session.delete(dao);
      
      // 先查询，再删除（推荐） --级联删除
      CstCustomerDao dao1 = session.get(CstCustomerDao.class, 1L);
      session.delete(dao1);
      
      transaction.commit();
      session.close();
      }
      ```

  * 保存或更新

    * oid saveOrUpdate(Object obj)；

  * 查询所有

---

**get方法和load方法的区别：**

get方法：

* 采用的是立即加载，执行到这行代码的时候，就会马上发送SQL语句去查询
* 查询后返回的是真实对象本身
* 查询一个找不到的对象的时候，返回null

load方法：

* 采用的是延迟加载（lazy懒加载），执行到这行代码的时候，不会发送SQL语句，当真正使用这个对象的时候才会发送SQL语句
* 查询后返回的是代理对象，javassist-3.24.0-GA.jar 利用javassist技术产生的代理
* 查询一个找不到的对象的时候，返回ObjectNotFoundException

---



### Transaction：事务对象

Hibernate中管理事务的对象。

* commit();

* rollback();





