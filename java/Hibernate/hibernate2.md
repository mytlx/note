## 持久化类的编写规则

* 持久化：将内存中的一个对象持久化到数据库中的过程，Hibernate框架就是用来进行持久化的框架。

* 持久化类：一个Java对象与数据库的表建立了映射关系，那么这个类在Hibernate中称为持久化类。
  * 持久化类 = Java类 + 映射文件

### 编写规则

* 对持久化类提供一个**无参数的构造方法**
  * Hibernate底层需要使用反射生成实例
* **属性需要私有，对私有属性提供public的get和set方法**
  * Hibernate中获取，设置对象的值
* 对持久化类提供一个**唯一标识OID与数据库主键对应**
  * Java中通过对象的地址区分是否是同哦一个对象，数据库中通过主键确定是否是同一个记录，**在Hibernate中通过持久化类的OID属性区分是否是同一个对象**
* 持久化类中属性尽量使用**包装类类型**
  * 基本数据类型默认值是0, 0会有很多的歧义。包装类类型的默认值是null
* 持久化类**不用使用final进行修饰**
  * 延迟加载本身是Hibernate一个优化的手段，返回的是一个代理对象（javassist可以对没有实现接口的类产生代理 --- 使用了非常底层字节码增强技术，继承这个类进行代理）。如果不能被继承，不能产生代理对象，延迟加载也就失效，则load方法和get方法一致



## 主键生成策略

### 主键的分类

#### 自然主键

* 主键本身就是表中的一个字段（实体中的一个具体的属性）
  * 创建一个人员表，人员都会有一个身份证号（唯一的不可重复的），使用了身份证号作为主键，这种主键称为自然主键

#### 代理主键

* 主键的本身不是表中必须的一个字段（不是实体中的某个具体的属性）
  * 创建一个人员表，没有使用人员中的身份证号，用了一个与这个表不相关的字段ID，（PNO），这种主键称为是代理主键
* **在实际开发中，尽量使用代理主键**
  * 一旦自然主键参与到业务逻辑中，后期可能需要修改源代码
  * 好的程序设计满足开闭原则

### 主键的生成策略

#### Hibernate的主键生成策略

在实际开发中一般不允许用户手动设置主键，一般将主键交给数据库，手动编写程序进行设置，在Hibernate中为了减少程序编写，提供了很多种主键的生成策略。

* **increment：**hibernate中提供的自动增长机制，适用short、int、long类型的主键，在单线程中使用
  * 首先发送一条语句，`select max（id） from table；`，然后让 id+1，作为下一条记录的主键
* **identity：** 适用short、int、long类型的主键，使用的是数据库底层的自动增长机制，使用于有自动增长机制数据库（MySQL，MSSQL），Oracle没有自动增长
* **sequence：** 适用short、int、long类型的主键，采用的是序列的方式，Oracle支持序列，MySQL不能使用sequence
* **uuid：** 适用于字符串类型主键，使用hibernate中的随机方式生成字符串主键
* **native：** 本地策略，可以在identity和sequence之间进行自动切换
* **assigned：** hibernate放弃外键的管理，需要通过手动编写程序或者用户自己设置
* **foreign：** 外部的，一对一的一种关联映射的情况下使用（了解）



## 持久化类的三种状态

### 持久化类的三种状态

Hibernate是持久层框架，通过持久化类完成ORM操作。Hibernate为了更好的管理持久化类，将持久化类分成三种状态。

#### 瞬时态（Transient）

* 这种对象没有唯一的标识OID，没有被session管理，称为是瞬时态对象

#### 持久态（persistent）

* 这种对象有唯一标识OID，被session管理，称为是持久态对象
  * 持久化类的持久态对象，可以自动更新数据库

#### 脱管态（detached）

* 这种对象有唯一标识OID，没有被session管理，称为脱管态对象



### 持久化类的状态转换

* 瞬时态对象
  * 获得： `Customer customer = new Customer（）；`
  * 状态转换
    * 瞬时 -> 持久
      * save（Object obj）、saveOrUpdate(Object obj)
    * 瞬时 -> 托管
      * customer.setCust_id(1L)
* 持久态对象
  * 获得
    * get()、load()、find()、iterate()
    * `Customer customer = session.get(Customer.class, 1L);`
  * 状态转换
    * 持久 -> 瞬时
      * delete()
    * 持久 -> 脱管
      * close()、clear()、evict(Object obj)
* 脱管态对象
  * 获得
    * `Customer customer = new Customer();`
    * `customer.setCust_id(1L);`
  * 状态转换
    * 脱管 -> 持久
      * update()、saveOrUpdate()
    * 脱管 -> 瞬时
      * customer.setCust_id(null);

### 持久态对象特性

#### 持久化类持久态对象自动更新数据库

```java
@Test
// 持久态对象自动更新数据库
public void demo2(){
    Session session = HibernateUtils.openSession();
    Tranction tranction = session.beginTranction();
    
    // 获得持久态对象
    Customer customer = session.get(Customer.class, 1L);
    customer.setCust_name("小明");
    // session.update(customer);
    
    tranction.commit();
    session.close();
}
```



## Hibernate缓存

缓存：一种优化的方式，将数据存入到内存中，使用的时候直接从缓存中获取，不用通过存储源。

Hibernate框架中提供了优化手段：缓存、抓取策略

* Hibernate中提供了两种缓存机制
  * 一级缓存：Session级别的缓存
    * 生命周期与Session一致
    * 由Session中的一系列的java集合构成
    * **自带的不可卸载的**
  * 二级缓存：SessionFactory级别的缓存
    * 需要配置
    * 基本不用

### 证明一级缓存存在

```java
@Test
public void test5() {
    Session session = HibernateUtils.openSession();
    Transaction transaction = session.beginTransaction();

    CstCustomerDao cst1 = session.get(CstCustomerDao.class, 1L);    // 发送sql语句
    System.out.println("cst1 = " + cst1);

    CstCustomerDao cst2 = session.get(CstCustomerDao.class, 1L);    // 不发送sql语句
    System.out.println("cst2 = " + cst2);

    System.out.println(cst1 == cst2);   // true

    transaction.commit();
    session.close();
}
```

```java
@Test
public void test6() {
    Session session = HibernateUtils.openSession();
    Transaction transaction = session.beginTransaction();

    CstCustomerDao cst = new CstCustomerDao();
    cst.setCustName("小明");
    Serializable id = session.save(cst);

    CstCustomerDao dao = session.get(CstCustomerDao.class, id); // 不发送sql语句
    System.out.println("dao = " + dao);

    transaction.commit();
    session.close();
}
```

### 一级缓存的结构

