# mysql





## 查询

### 查询机制

服务器通过了对用户名和密码的验证，则为用户创建一个数据库连接，该连接从应用程序发出请求后一直保持，知道应用程序释放连接或者服务器关闭连接。

mysql服务器会给每个连接赋予一个标识符，会在首次登陆时显示。

查询发送到服务端时，会做如下检查：

* 用户是否有权限执行该语句
* 用户是否用权限访问目标数据
* 语句的语法是否正确

通过以上检测，会传递给**查询优化器**，负责为查询找到最有效率的执行方式。



### 查询语句

#### select语句

> select子句用于在所有可能的列中，选择查询结果集要包含哪些列。

select子句中可以添加一下内容：

* 字符
* 表达式
* 调用内建函数
* 用户自定义的函数调用

##### 列的别名

* `select xxx [as] yyy, aaa [as] bbb` 

* as可有可无

##### 去除重复的行

* 在select关键字之后加上distinct关键字
  * `select distinct xxx`
* all关键字是系统默认的，不需要被显示的列出
* 产生无重复的结果集需要首先对数据**排序**，对于大的结果集**相当耗时**



#### from子句

> from子句定义了查询中所使用的表，以及连接这些表的方式。

存在3种类型的表：

* 永久表（使用 create table 语句创建的表）
* 临时表（子查询所返回的表）
  * `select xxx from (select xxx from xx);`
* 虚拟表（使用 create view 子句所创建的视图）
  * **视图**是存储在数据字典中的查询，行为表现得像一个表，但实际上并不拥有任何数据
  * `create view xxx_vw as select xxx from xxx`
  * 视图创建后，并没有产生或存储任何数据，服务器只是简单的保留该查询以供将来使用

##### 定义表别名

两种在from子句之外引表的方式：

* 使用完整的表名称
* 为每个表指定别名
  * `from xxx [as] yyy, aaa [as] bbb`	as可有可无



#### where子句

> where子句用于在结果集中过滤掉不需要的行。

多个条件用and、or或者not分隔，还有用圆括号分组。



#### group by 和 having 子句

> group by子句用于根据列值对数据进行分组。
>
> having子句对分组数据进行过滤。



#### order by 子句

> order by 子句用于对结果集中的原始列数据或是根据列数据计算的表达式结果进行排序。

* `select ... from ... order by xxx, yyy`
* xxx是第一权重，yyy是第二权重

##### 升序或降序排序

* asc升序，desc降序，默认是升序
* `select ... from ... order by ... desc`
* 降序通常用于排行查询，搭配limit

##### 根据表达式排序

* 对最后三位数字进行排序
* `select ... from ... order by right(xxx,3)`
* 内建函数right（）提取xxx列的最后三个字符

##### 根据数字占位符排序

* 如果需要根据select子句中的列来排序，那么可以选择使用该列位于select子句中的**位置号**来替代列名
* `select aa, bb, cc, dd, ee from ... order by 2, 5`
* **注：不建议使用这种方式**
  * 如果增加新的列，需要同步更改order by 中的序号



## 过滤

* where子句可能包含一个或多个条件，and和or分隔，使用圆括号明确顺序，not
* 条件类型
  * 相等条件 =
    * 修改数据
  * 不等条件  <> 或 !=
* `between 下限 and 上限` 
  * 上下限是闭合的
  * 可以使用字符串作为范围搜索的条件，需要知道所使用的字符集中各字符的次序
* 成员条件
  * in 和 not in
    * `where xx in （“xx”，“yy”，“zz”）`
    * `where xx in (select xxx from xxx where xx...)`
* 匹配条件：处理部分字符串匹配
  * 通配符
    * `_` ：正好一个字符
    * `%`：任意数目的字符（包括0）
      * `where xxx like '_a%e%'`
  * 正则表达式
    * `where xxx regexp '^[FG]'`
* null：表示值的缺失
  * 表达式可以为null，但不能等于null
    * is null，is not null 正确写法
    * =null 错误写法，不会匹配到数据，但也不会报错
  * 常见错误
    * `where xxx != 6` 会遗漏该项为null的数据行
    * 正确写法：`where xxx != 6 or xxx is null`



## 多表查询

**连接（join）**：将几张表中的数据整合到一起的机制，从而实现在同一个查询的结果集中包含来自几个表的列

#### 笛卡尔积

from中包含两个表，使用关键字**join**隔开，将产生**笛卡尔积**，即**两张表的所有置换**，这种连接称为**交叉连接（cross join）**

```mysql
mysql> select e.fname, e.lname, d.name
    -> from employee e JOIN department d;
```

#### 内连接

**内连接（inner join）**：如果在一个表中的x列中存在某个值，但该值在另一张表的x列中不存在，那么相关行的连接会失败，在结果集中将会排除包含该值的行。

* 如果想要包含其中某个表的所有行，而不考虑每行是否在另一个表中存在匹配，那么可以使用**外连接（outer join）**
* 通常在对两个表使用内连接时，最好在from子句中用**INNER JOIN**显式指定连接类型。
* 如果没有指定连接类型，默认使用**内连接**

```mysql
mysql> SELECT e.fname, e.lname, d,name
	-> FROM employee e INNER JOIN department d
	-> ON e.dept_id = d.dept_id;
```

* 如果连接两个表的列名是相同的，那么可以使用**using子句**替代on子句，但是不建议使用

```mysql
mysql> SELECT e.fname, e.lname, d.name
	-> FROM employee e INNER JOIN department d
	-> USING (dept_id);
```

#### ANSI连接语法

* 所有主流数据库都采用了**SQL92版本的ANSI SQL**标准。

* 同样允许旧的语法，都用where
  * 不太容易识别哪些是连接条件，哪些是过滤条件
  * 使用何种连接类型也不显而易见
  * 容易错误遗漏某些条件

---

SQL是一种非过程化的语言，在from子句中各表出现的顺序并不重要。

---

#### 连续两次使用同一个表

```mysql
mysql> SELECT a.account_id, e.emp_id, b_a.name open_branch, b_e.name emp_branh
	-> FROM account a INNER JOIN branch b_a
	-> ON a.open_branch_id = b_a.branch_id
	-> INNER JOIN employee e
	-> ON a.open_emp_id = e.emp_id
	-> INNER JOIN branch b_e
	-> ON e.assigned_branch_id = b_e.branch_id
	-> WHERE a.product_cd = 'CHK';
```

#### 自连接

对表自身进行连接

```mysql
mysql> SELECT xxx
	-> FROM employee e INNER JOIN employee e_mgr
	-> ON e.superior_emp_id = e_mgr.emp_id;
```

#### 相等连接和不等连接

#### 连接条件和过滤条件

SQL对放置条件的位置很灵活



## 集合

