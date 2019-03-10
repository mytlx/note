#  EL表达式



## 基本语法

### 1. EL关键字

一共有**16**个保留的关键字

|   and    |    or     |   not    | instanceof |
| :------: | :-------: | :------: | :--------: |
| **null** | **empty** | **true** | **false**  |
| **div**  |  **mod**  |  **gt**  |   **lt**   |
|  **eq**  |  **ge**   |  **ne**  |   **le**   |



### 2. 访问运算符"."和"[ ]"

1）当要存取的属性名称中包含一些特殊字符时，如"."或"-"等并非字母或数字的符号，就一定要使用"[ ]"

```jsp
${user.My-name}
```

上述是不正确的，应该改为：

```jsp
${user[“My-name”]}
```

2）"[ ]" 运算符比"."能更好的支持动态取值

```jsp
如果data是一个变量，${sessionScope.user[data]}，
若data的值为sex时，
那么上述例子等于：${sessionScope.user.sex}
```

**如果要动态取值时，要用"[ ]"方法进行，而"."无法做到动态取值。**

### 3. 算术运算

算术运算符的优先顺序如下。
1）括号：（）	
2）负号：-
3）乘、除、模：*，/（div），%（mod）
4）加、减：+、-

### 4. 关系运算

EL关系表达式的返回值为boolean值。
关系表达式的优先顺序低于算术运算，其中关系运算符之间的优先顺序如下。
1）<，>，<=，>=
2）==，！=

### 5. 逻辑运算

逻辑运算的优先顺序低于关系运算，其中逻辑运算符之间的优先顺序如下。
1）！（not）
2）&&（and）
3）||（or）

### 6. empty运算符

如下情况返回true。

 * 操作数值为null
 * 操作数是一个空容器（不包含任何元素的容器）
 * 空的数组
 * 长度为0的字符串

### 7. 自动类型转换

getAttribute()方法返回值的类型为Object。
EL表达式中自动类型转换的规则如下。
1）Object转换为数值

* 如果Object为boolean类型，出错。

* 如果Object == null， 返回0。

* 如果Object == “”， 返回0。

* 如果Object为字符串，且字符串可以转换为数值，则返回数值，否则会出错。

2）Object转换为String

	Object为数值型数据时，依据其值直接转换成字符串，如100.23转换为“100.23”。

* 如果Object == null， 返回长度为0的字符串：“ ”。

* 如果Object.toString()产生异常， 会出错，否则返回Object.toString()。

>	JSP会把${xxx}的内容都认为是EL表达式，里面的内容都会被计算，
>
>	如果只是想输出 ${xxx}的字符串的话，就需要转义
>
>	1. \ ${xxx}
>
>	2. ${'${'}xxx}



## 隐式对象

EL提供了11个隐式对象。

| 序号 | 隐含对象名称     | 描       述                                                  |
| ---- | ---------------- | ------------------------------------------------------------ |
| 1    | pageContext      | 对应于JSP页面中的pageContext对象（注意：取的是pageContext对象。） |
| 2    | pageScope        | 代表page域中用于保存属性的Map对象                            |
| 3    | requestScope     | 代表request域中用于保存属性的Map对象                         |
| 4    | sessionScope     | 代表session域中用于保存属性的Map对象                         |
| 5    | applicationScope | 代表application域中用于保存属性的Map对象                     |
| 6    | param            | 表示一个保存了所有请求参数的Map对象                          |
| 7    | paramValues      | 表示一个保存了所有请求参数的Map对象，它对于某个请求参数，返回的是一个string[] |
| 8    | header           | 表示一个保存了所有http请求头字段的Map对象，注意：如果头里面有“-” ，例Accept-Encoding，则要header[“Accept-Encoding”] |
| 9    | headerValues     | 表示一个保存了所有http请求头字段的Map对象，它对于某个请求参数，返回的是一个string[]数组。注意：如果头里面有“-” ，例Accept-Encoding，则要headerValues[“Accept-Encoding”] |
| 10   | cookie           | 表示一个保存了所有cookie的Map对象                            |
| 11   | initParam        | 表示一个保存了所有web应用初始化参数的map对象                 |

	



# JSP的标签

为了更好地实现业务代码与表示层页面代码的分离，推出了标签。

标签就是把一段具体业务的Java代码封装起来，然后以标记语言的形式在页面文件中对它进行调用。

目前标签有两种形式，一种是标准标签库，另一种是自定义标签。



## 标准标签库JSTL

JSTL需要两个jar包，**jstl.jar**和**standard.jar**

JSP页面如果需要使用标签库，需要在每个JSP文件的头部包含\<taglib>标签

```
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

<center><b>标准标签库的分类</b></center>

|     标记库名称     |                  URI                   | 前缀 |                     说明                     |
| :----------------: | :------------------------------------: | :--: | :------------------------------------------: |
|  核心标签（core）  |   http:.//java.sun.com/jsp/jstl/core   |  c   | 核心功能实现，包括变量管理、迭代和条件判断等 |
| 格式化标签（I18N） |   http:.//java.sun.com/jsp/jstl/fmt    | fmt  |             国际化，数据格式显示             |
|      SQL标签       |   http:.//java.sun.com/jsp/jstl/sql    | sql  |                  操作数据库                  |
|      XML标签       |   http:.//java.sun.com/jsp/jstl/xml    |  x   |                   操作XML                    |
|   JSTL函数（Fn）   | http:.//java.sun.com/jsp/jstl/function |  fn  |  常用函数库，包括Spring操作、集合类型操作等  |



### 核心标签

核心标签是最常用的JSTL标签。引用核心标签库的语法如下：

```
<%@ taglib prefix="c" 
           uri="http://java.sun.com/jsp/jstl/core" %>
```

| 标签           | 描述                                                         |
| :------------- | ------------------------------------------------------------ |
| \<c:out>       | 用于在JSP中显示数据，就像<%= ... >                           |
| \<c:set>       | 用于保存数据                                                 |
| \<c:remove>    | 用于删除数据                                                 |
| \<c:catch>     | 用来处理产生错误的异常状况，并且将错误信息储存起来           |
| \<c:if>        | 与我们在一般程序中用的if一样                                 |
| \<c:choose>    | 本身只当做<c:when>和<c:otherwise>的父标签                    |
| \<c:when>      | <c:choose>的子标签，用来判断条件是否成立                     |
| \<c:otherwise> | <c:choose>的子标签，接在<c:when>标签后，当<c:when>标签判断为false时被执行 |
| \<c:import>    | 检索一个绝对或相对 URL，然后将其内容暴露给页面               |
| \<c:forEach>   | 基础迭代标签，接受多种集合类型                               |
| \<c:forTokens> | 根据指定的分隔符来分隔内容并迭代输出                         |
| \<c:param>     | 用来给包含或重定向的页面传递参数                             |
| \<c:redirect>  | 重定向至一个新的URL.                                         |
| \<c:url>       | 使用可选的查询参数来创造一个URL                              |



### 格式化标签

JSTL格式化标签用来格式化并输出文本、日期、时间、数字。引用格式化标签库的语法如下：

```jsp
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
```

| 标签                   | 描述                                     |
| ---------------------- | ---------------------------------------- |
| \<fmt:formatNumber>    | 使用指定的格式或精度格式化数字           |
| \<fmt:parseNumber>     | 解析一个代表着数字，货币或百分比的字符串 |
| \<fmt:formatDate>      | 使用指定的风格或模式格式化日期和时间     |
| \<fmt:parseDate>       | 解析一个代表着日期或时间的字符串         |
| \<fmt:bundle>          | 绑定资源                                 |
| \<fmt:setLocale>       | 指定地区                                 |
| \<fmt:setBundle>       | 绑定资源                                 |
| \<fmt:timeZone>        | 指定时区                                 |
| \<fmt:setTimeZone>     | 指定时区                                 |
| \<fmt:message>         | 显示资源配置文件信息                     |
| \<fmt:requestEncoding> | 设置request的字符编码                    |

------

### SQL标签

JSTL SQL标签库提供了与关系型数据库（Oracle，MySQL，SQL Server等等）进行交互的标签。引用SQL标签库的语法如下：

```jsp
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
```

| 标签                 | 描述                                                         |
| -------------------- | ------------------------------------------------------------ |
| \<sql:setDataSource> | 指定数据源                                                   |
| \<sql:query>         | 运行SQL查询语句                                              |
| \<sql:update>        | 运行SQL更新语句                                              |
| \<sql:param>         | 将SQL语句中的参数设为指定值                                  |
| \<sql:dateParam>     | 将SQL语句中的日期参数设为指定的java.util.Date 对象值         |
| \<sql:transaction>   | 在共享数据库连接中提供嵌套的数据库行为元素，将所有语句以一个事务的形式来运行 |

------

### XML 标签

JSTL XML标签库提供了创建和操作XML文档的标签。引用XML标签库的语法如下：

```jsp
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
```

在使用xml标签前，你必须将XML 和 XPath 的相关包拷贝至你的<Tomcat 安装目录>\lib下:

- XercesImpl.jar

  下载地址： <http://www.apache.org/dist/xerces/j/>

- xalan.jar

  下载地址： <http://xml.apache.org/xalan-j/index.html>

| 标签           | 描述                                                      |
| -------------- | --------------------------------------------------------- |
| \<x:out>       | 与<%= ... >,类似，不过只用于XPath表达式                   |
| \<x:parse>     | 解析 XML 数据                                             |
| \<x:set>       | 设置XPath表达式                                           |
| \<x:if>        | 判断XPath表达式，若为真，则执行本体中的内容，否则跳过本体 |
| \<x:forEach>   | 迭代XML文档中的节点                                       |
| \<x:choose>    | <x:when>和<x:otherwise>的父标签                           |
| \<x:when>      | <x:choose>的子标签，用来进行条件判断                      |
| \<x:otherwise> | <x:choose>的子标签，当<x:when>判断为false时被执行         |
| \<x:transform> | 将XSL转换应用在XML文档中                                  |
| \<x:param>     | 与<x:transform>共同使用，用于设置XSL样式表                |

------

### JSTL函数

JSTL包含一系列标准函数，大部分是通用的字符串处理函数。引用JSTL函数库的语法如下：

```jsp
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
```

| 函数                                                         | 描述                                                     |
| ------------------------------------------------------------ | -------------------------------------------------------- |
| [fn:contains()](http://www.runoob.com/jsp/jstl-function-contains.html) | 测试输入的字符串是否包含指定的子串                       |
| [fn:containsIgnoreCase()](http://www.runoob.com/jsp/jstl-function-containsignoreCase.html) | 测试输入的字符串是否包含指定的子串，大小写不敏感         |
| [fn:endsWith()](http://www.runoob.com/jsp/jstl-function-endswith.html) | 测试输入的字符串是否以指定的后缀结尾                     |
| [fn:escapeXml()](http://www.runoob.com/jsp/jstl-function-escapexml.html) | 跳过可以作为XML标记的字符                                |
| [fn:indexOf()](http://www.runoob.com/jsp/jstl-function-indexof.html) | 返回指定字符串在输入字符串中出现的位置                   |
| [fn:join()](http://www.runoob.com/jsp/jstl-function-join.html) | 将数组中的元素合成一个字符串然后输出                     |
| [fn:length()](http://www.runoob.com/jsp/jstl-function-length.html) | 返回字符串长度                                           |
| [fn:replace()](http://www.runoob.com/jsp/jstl-function-replace.html) | 将输入字符串中指定的位置替换为指定的字符串然后返回       |
| [fn:split()](http://www.runoob.com/jsp/jstl-function-split.html) | 将字符串用指定的分隔符分隔然后组成一个子字符串数组并返回 |
| [fn:startsWith()](http://www.runoob.com/jsp/jstl-function-startswith.html) | 测试输入字符串是否以指定的前缀开始                       |
| [fn:substring()](http://www.runoob.com/jsp/jstl-function-substring.html) | 返回字符串的子集                                         |
| [fn:substringAfter()](http://www.runoob.com/jsp/jstl-function-substringafter.html) | 返回字符串在指定子串之后的子集                           |
| [fn:substringBefore()](http://www.runoob.com/jsp/jstl-function-substringbefore.html) | 返回字符串在指定子串之前的子集                           |
| [fn:toLowerCase()](http://www.runoob.com/jsp/jstl-function-tolowercase.html) | 将字符串中的字符转为小写                                 |
| [fn:toUpperCase()](http://www.runoob.com/jsp/jstl-function-touppercase.html) | 将字符串中的字符转为大写                                 |
| [fn:trim()](http://www.runoob.com/jsp/jstl-function-trim.html) | 移除首位的空白符                                         |