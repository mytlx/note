# 日志

java开发过程中经常需要打印日志信息，往往会在每个类的第一行加上形如以下代码：

```java
protected static final Logger logger = LoggerFactory.getLogger(XXX.class);
```

**目的：**使用指定的类XXX初始化日志对象，方便在日志输出的时候，可以打印出日志信息所属的类。

**示例：**

```java
protected static final Logger logger = LoggerFactory.getLogger(XYZ.class);
logger.debug("hello world");

// 输出：*XYZ:hello world
```

![1557981051253](C:\Users\TLX\AppData\Local\Temp\1557981051253.png)

![1557981637777](C:\Users\TLX\AppData\Local\Temp\1557981637777.png)

![1557982332625](C:\Users\TLX\AppData\Local\Temp\1557982332625.png)



| 日志级别 | 描述                                               |
| -------- | -------------------------------------------------- |
| OFF      | 关闭：最高级别，不输出日志。                       |
| FATAL    | 致命：输出非常严重的可能会导致应用程序终止的错误。 |
| ERROR    | 错误：输出错误，但应用还能继续运行。               |
| WARN     | 警告：输出可能潜在的危险状况。                     |
| INFO     | 信息：输出应用运行过程的详细信息。                 |
| DEBUG    | 调试：输出更细致的对调试应用有用的信息。           |
| TRACE    | 跟踪：输出更细致的程序运行轨迹。                   |
| ALL      | 所有：输出所有级别信息。                           |

ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF



##### 1. 正确的定义日志

```java
private static final Logger LOG = LoggerFactory.getLogger(this.getClass());
```

通常一个类只有一个 LOG 对象，如果有父类可以将 LOG 定义在父类中。

日志变量类型定义为门面接口（如 slf4j 的 Logger），实现类可以是 `Log4j`、`Logback`等日志实现框架，不要把实现类定义为变量类型，否则日志切换不方便，也不符合抽象编程思想。	



##### 2、使用参数化形式`{}`占位，`[]` 进行参数隔离

```java
LOG.debug("Save order with order no：[{}], and order amount：[{}]");
```

这种可读性好，这样一看就知道`[]`里面是输出的动态参数，`{}`用来占位类似绑定变量，而且只有真正准备打印的时候才会处理参数，方便定位问题。

如果日志框架不支持参数化形式，且日志输出时不支持该日志级别时会导致对象冗余创建，浪费内存，此时就需要使用 `isXXEnabled` 判断，如：

```java
if(LOG.isDebugEnabled()){
    // 如果日志不支持参数化形式，debug又没开启，那字符串拼接就是无用的代码拼接，影响系统性能
    logger.debug("Save order with order no：" + orderNo + ", and order amount：" + orderAmount);
}
```

至少 `debug` 级别是需要开启判断的，线上日志级别至少应该是 `info` 以上的。

这里推荐大家用 `SLF4J` 的门面接口，可以用参数化形式输出日志，`debug` 级别也不必用 `if` 判断，简化代码。

##### 3、输出不同级别的日志

项目中最常用有日志级别是`ERROR`、`WARN`、`INFO`、`DEBUG`四种了，这四个都有怎样的应用场景呢。

- ERROR（错误）

一般用来记录程序中发生的任何异常错误信息（Throwable），或者是记录业务逻辑出错。

- WARN（警告）

一般用来记录一些用户输入参数错误、

- INFO（信息）

这个也是平时用的最低的，也是默认的日志级别，用来记录程序运行中的一些有用的信息。如程序运行开始、结束、耗时、重要参数等信息，需要注意有选择性的有意义的输出，到时候自己找问题看一堆日志却找不到关键日志就没意义了。

- DEBUG（调试）

这个级别一般记录一些运行中的中间参数信息，只允许在开发环境开启，选择性在测试环境开启。



### 几个错误的打日志方式

#### 1. 不要使用 `System.out.print..`

输出日志的时候只能通过日志框架来输出日志，而不能使用 `System.out.print..` 来打印日志，这个只会打印到 `tomcat` 控制台，而不会记录到日志文件中，不方便管理日志，如果通过服务形式启动把日志丢弃了那更是找不到日志了。

#### 2. 不要使用 `e.printStackTrace()`

首先来看看它的源码：

```java
public void printStackTrace() {
    
    printStackTrace(System.err);
}
```

它其实也是利用 `System.err` 输出到了 `tomcat` 控制台。

#### 3. 不要抛出异常后又输出日志

如捕获异常后又抛出了自定义业务异常，此时无需记录错误日志，由最终捕获方进行异常处理。不能又抛出异常，又打印错误日志，不然会造成重复输出日志。

```java
try {

    // ...
    
} catch (Exception e) {
    // 错误
    LOG.error("xxx", e);

    throw new RuntimeException();
}
```

#### 4. 不要使用具体的日志实现类

```java
InterfaceImpl interface = new InterfaceImpl();
```

这段代码大家都看得懂吧？应该面向接口的对象编程，而不是面向实现，这也是软件设计模式的原则，正确的做法应该是。

```java
Interface interface = new InterfaceImpl();
```

日志框架里面也是如此，上面也说了，日志有门面接口，有具体实现的实现框架，所以大家不要面向实现编程。

#### 5. 没有输出全部错误信息

看以下代码，这样不会记录详细的堆栈异常信息，只会记录错误基本描述信息，不利于排查问题。

```java
try {
    // ...
} catch (Exception e) {
    // 错误
    LOG.error('XX 发生异常', e.getMessage());
    
    // 正确
    LOG.error('XX 发生异常', e);
}
```

#### 6. 不要使用错误的日志级别

曾经在线上定位一个问题，同事自信地和我说：`明明输出了日志啊，为什么找不到...`，后来我去看了下他的代码，是这样的：

```java
try {
    // ...
} catch (Exception e) {
    // 错误
    LOG.info("XX 发生异常...", e);
}
```

大家看出了什么问题吗？用 `info` 记录 `error` 日志，日志输出到了 `info` 日志文件中了，同事拼命地在 `error` 错误日志文件里面找怎么能找到呢？

#### 7. 不要在千层循环中打印日志

这个是什么意思，如果你的框架使用了性能不高的 `Log4j` 框架，那就不要在上千个 `for`循环中打印日志，这样可能会拖垮你的应用程序，如果你的程序响应时间变慢，那要考虑是不是日志打印的过多了。

```java
for(int i=0; i<2000; i++){
    LOG.info("XX");
}
```

最好的办法是在循环中记录要点，在循环外面总结打印出来。

#### 8. 禁止在线上环境开启 `debug`

这是最后一点，也是最重要的一点。

一是因为项目本身 `debug` 日志太多，二是各种框架中也大量使用 `debug` 的日志，线上开启 `debug` 不久就会打满磁盘，影响业务系统的正常运行。