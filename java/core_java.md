# java基础

## 数据类型

8种基本类型（primitive type）：

* 4种整型
  * byte
  * short
  * int
  * long
* 2种浮点类型
  * float
  * double
* 1种用于表示Unicode编码的字符单元的字符类型char
  * char
* 一种用于表示真值的boolean类型
  * boolean

### 整型

| 类型  | 存储需求 |                        取值范围                         |
| :---: | :------: | :-----------------------------------------------------: |
|  int  |  4字节   |     - 2 147 483 648 ~ 2 147 483 647（正好超过20亿）     |
| short |  2字节   |                    - 32 768 ~ 32 767                    |
| long  |  8字节   | - 9 223 372 036 854 775 808 ~ 9 223 372 036 854 775 807 |
| byte  |  1字节   |                       - 128 ~ 127                       |

* int类型最常用
* java中，所有的数值类型所占据的字节数量与平台无关
* 长整型数值有一个后缀L或l（如`40000000000L`）
* 十六进制前缀0x或0X，八进制前缀0，如 010 对应八进制的8，容易混淆，不建议使用
* java 7 开始，加上前缀0b或0B表示二进制，还可以为数字字面量加下划线，更易读（如 `0b1111_0100_0010_0100_0000` 或 `1_000_000`表示一百万），java编译器会去掉这些下划线
* java没有任何无符号形式的整型

### 浮点类型

|  类型  | 存储需求 |                         取值范围                          |
| :----: | :------: | :-------------------------------------------------------: |
| float  |  4字节   |      大约 +- 3.402 823 47E+38F（有效位数为 6~7 位）       |
| double |  8字节   | 大约 +- 1.797 693 134 862 315 70E+308（有效位数为 15 位） |

* 绝大部分程序采用double类型
* float类型的数值有一个后缀F或f，没有后缀F的浮点数值默认为double类型，也可以有后缀D或d
* 所有浮点数值计算都遵循 IEEE 754 规范，表示溢出和出错的三个特殊的浮点数值
  * 正无穷大：`Double.POSITIVE_INFINITY`
  * 负无穷大：`Double.NEGATIVE_INFINITY`
  * NaN（Not a Number）：`Double.NaN`
    * 不能检测一个特定值是否等于 `Double.NaN`
    * 可以使用 `Double.isNaN()`方法

### char类型

* char类型的字面量值要用单引号括起来
* 值可以表示为十六进制值，范围从 \u0000 到 \Uffff
* char类型描述了 UTF-16编码中的一个代码单元

### boolean类型

* 两个值：false 和 true
* **整型值和布尔值之间不能进行相互转换**



## 变量

* 声明一个变量之后，必须用赋值语句对变量进行显示初始化
* 在java中可以将声明放在代码中的任何地方
* 变量的声明尽可能地靠近变量第一次使用的地方
* 不区分变量的声明与定义



## 常量

* 利用关键字 **final**指示常量
  * 只能被赋值一次
  * 一旦赋值之后，就不能再更改了
  * 常量名使用全大写

* 利用关键字 **static final**设置一个类常量
  * 定义位于方法外部



## 类型转换

两个不同数值类型进行二元操作时，先要将两个操作数类型转换为同一种类型，然后再进行计算：

* 如果两个操作数中的一个是double类型，另一个操作数就会转换为double类型
* 否则，如果其中一个操作数是float类型，另一个操作数将会转换为float类型
* 否则，如果其中一个操作数是long类型，另一个操作数将会转换为long类型
* 否则，两个操作数都将被转换为int类型



## 位操作

`>>>`运算符会用0填充高位，`>>`运算符用符号位填充高位，不存在 `<<<`运算符



## 字符串

* java字符串就是**Unicode字符序列**
* java没有内置的字符串类型，而是在标准java类库中提供了一个预定义类
* 每个用双引号括起来的字符串都是String类的一个实例
* 不能修改java字符串中的字符，String类对象称为**不可变字符串**
  * 编译器可以让字符串共享
  * 通常只有字符串常量是共享的，而 `+` 或 `substring` 等操作产生的结果并不是共享的

### 子串

`substring(start, end);` 左闭右开

### 拼接

* java使用 `+` 号连接两个字符串
* 当一个字符串与一个非字符串的值进行拼接时，后者被转换成字符串
* `String.join()`方法可以实现多个字符串拼接，用一个定界符分隔
  * `String.join("/", "S", "M", "L");` => `S/M/L`

### 检测相等

* `s.equals(t)` 检测两个字符串是否相等
* `s.equalsIgnoreCase(t)` 检测两个字符串是否相等，不区分大小写
* `s.compareTo(t)` s在t前返回负值，相等返回0，s在t后返回正值
* 一定不要使用 `==`检测两个字符串是否相等，只能检测是否放在同一个位置


### 空串和Null串

* 空串 `“”` 是长度为0的字符串
  * 是一个java对象，有自己的**串长度（0）**和**内容（空）**
* null串表示目前没有任何对象与该变量关联
  * `if (str == null)` 检测一个字符串是否为null



## 控制流程

### 块作用域

块（即复合语句）：由一对大括号括起来的若干条简单的java语句

* 块确定了变量的作用域

* 一个块可以嵌套在另一块中

* **不能再嵌套的两个块中声明同名的变量，否则无法编译**

  ```java
  public static void main(String[] args){
      int n;
      //...
      {
          int k;
          int n;    // Error
          //...
      }
  }
  ```

### switch语句

case标签：

* 类型为 char， byte， short 或 int 的常量表达式
* 枚举常量
* 字符串字面量（java 7开始）

### 中断控制流程语句

java设计者将goto作为保留字，但实际上并没有打算在语言中使用它。

但是提供了一种 **带标签的break语句**，用于跳出多重嵌套的循环语句

* 标签必须放在希望跳出的最外层循环之前，并且必须紧跟一个冒号

  ```java
  Scanner in = new Scanner(System.in);
  int n;
  read_data:
  while(...){
      //...
      for(...){
          System.out.print("...");
          n = in.nextInt();
          if (n < 0)
              break read_data;
          // ...
      }
  }
  // this statement is executed immediately after the labeled break
  ...
  ```

* 可以将标签应用到任何语句中，甚至可以应用到if语句或者块语句中
* 只能跳出语句块，而不能跳入语句块



## 数组

数组是一种数据结构，用来存储同一类型值的集合。

两种形式声明数组：

* `int[] a;` 建议使用
* `int a[];`

声明数组后，并没有将a初始化为一个真正的数组，应该使用**new运算符**创建数组。

```java
int[] a = new int[100];
```

这条语句创建了一个可以存储100个整数的数组。

数组长度**不要求是常量**：`new int[n]`会创建一个长度为n的数组。

创建一个数组时：

* 数字数组的所有元素会初始化为0
* boolean数组的元素会初始化为false
* 对象数组的元素会初始化为null

**数组一旦创建了，就不能再改变它的大小。**

创建数组对象并同时赋予初始值的简化书写形式，不需要调用new

```java
int[] a = { 2, 3, 4, 5, 6, 7 };
```

初始化一个**匿名数组**

```java
new int[] { 11, 12, 13, 14, 15, 16};
```

* 数组的大小就是初始值的个数
* 可以在不创建新变量的情况下重新初始化一个数组
* 允许数组长度为0， 数组长度为0和null不同
* java数组没有指针运算，不能通过 `数组名+1` 得到数组的下一个元素



`a.length` 

* 获得数组中的元素个数

`Arrays.toString(type[] a)` 

* 打印数组中的所有值

`Arrays.copyOf(type[] a, int length)` 

* 将一个数组的所有值拷贝到一个新数组中去，通常用于增加数组的大小

`Arrays.sort(type[] a)`

* 对数组进行排序，使用了**优化的快速排序**算法  



---



## 对象与类

* 实例域（instance field）：对象中的数据
* 状态（state）：对于每个特定的类实例（对象）都有一组特定的实例域值，这些值的集合就是和这个对象的当前状态

### 类之间的关系

* 依赖（uses-a）：一个类的方法操纵另一个类的对象
* 聚合（has-a）：类A的对象包含类B的对象
* 继承（is-a）：A继承B

### 对象变量与对象

```java
Date birthday = new Date();
```

birthday是对象变量，new Date() 构造了一个新对象

* 对象变量并没有实际包含一个对象，而仅仅引用一个对象
* 任何对象变量的值都是对存储在另外一个地方的一个对象的引用。new操作符的返回值也是一个引用
* 显式地将对象变量设置为null，表示这个对象变量没有引用任何对象
* 局部变量不会自动地初始化为null，而必须通过调用new或将它们设置null进行初始化
* **所有的java对象都存储在堆中，所有的java对象都是在堆中构造的**





### 用户自定义类

* 文件名必须与public类的名字相匹配
* 在一个源文件中，只能有一个公有类，但可以有任意数目的非公有类

### 构造器

* 构造器与类同名
* 每个类可以有一个以上的构造器
* 构造器可以有0个、1个或多个参数
* 构造器没有返回值
* 构造器总是伴随着new操作一起调用
* 不要在构造器中定义与实例域重名的局部变量



## final实例域

* 构建对象时必须初始化这样的域
* 初始化后不能再对它进行修改
* 大都应用于**基本类型域**或**不可变类的域**
  * 可变的类使用final会造成混乱
  * final关键字只是表示对象变量不会再引用其他对象，但是这个对象可以更改



## 静态域

* 也称为类域
* 所有对象共享
* 属于类而不属于任何对象



## 静态常量

* 例如：`public static finale double PI = 3.14145926535897323846;`
* 可以通过类名直接获取，`Math.PI`
* 由于每个类对象都可以对公有域进行修改，所以不建议将域设计为public，但是final域却没问题，因为声明为final不可修改



## 静态方法

* 不能向对象实施操作的方法
* static是类方法，不属于某一个对象
* **没有this参数**，this和super都是指向对象
* 不能访问**实例域**，可以访问自身类中的**静态域**
* 使用类名调用方法，也可以使用类下对象调用，但是不建议
* 使用静态方法的情况
  * 一个方法不需要访问对象状态，所需参数都是通过显示参数提供
  * 只需要访问类的静态域



## main方法

* 不对任何对象进行操作
  * 在启动程序时还没有任何一个对象，静态的main方法将执行并创建程序所需要的对象
* 每个类都可以有一个main方法，可以用于单元测试



## 方法参数

> 按值调用（call by value）：表示方法接收的是调用者提供的值
>
> 按引用调用（call by reference）：表示方法接收的是调用者提供的变量地址
>
> 一个方法可以修改传递引用所对应的变量值，而不能修改传递值调用所对应的变量值

* java采用**按值调用**
  * 方法不能修改传递给它的任何参数变量的内容
* 方法参数的类型
  * 基本数据类型（数字、布尔型）
  * 对象引用（按值传递）
* 方法参数的使用情况
  * 一个方法不能修改一个基本数据类型的参数（数值、布尔）
  * 一个方法可以改变一个对象参数的标志
  * 一个方法不能让对象参数引用一个新的对象



## 重载（overloading）

* 方法的**签名（signature）**
  * **方法名、参数类型**
  * **不包含返回类型**
* 多个方法有相同的名字、不同的参数，便产生重载
  * 也就是说重载需要签名不同，只是返回类型不同不算重载
* 重载解析（overloading resolution）
  * 编译器通过**各个方法给出的参数类型**与**特定方法调用所使用的值类型**进行匹配来挑选相应的方法，如果找不到匹配的参数，那么会产生编译时错误



## 默认域初始化

* 仅当类没有提供任何构造器的时候，系统才会提供一个默认的构造器（无参数构造器）
* 数值为0，布尔值为false，对象引用为null
* 必须明确地初始化方法中的局部变量，但是类中的域会自动初始化为默认值



## 初始化块

* 初始化数据域的方法
  * 在构造器中设置值
  * 在声明中赋值
  * 初始化块
* 建议将初始化块放在域定义的后面
* 调用构造器的具体处理步骤
  * 所有数据域被初始化为默认值
  * 按照在类声明中出现的次序，一次执行所有域初始化语句和初始化块
  * 如果构造器第一行调用了第二个构造器，则执行第二个构造器主体
  * 执行这个构造器的主体
* 可以使用静态的初始化块对类的静态域进行初始化
* 类第一次加载的时候，将会进行静态域的初始化
* 所有的静态初始化语句以及静态初始化块都将依照类定义的顺序进行



## 包（package）

* 所有标准的java包都处于java和javax包层次中
* 域名的逆序形式作为包名
* 嵌套的包之间没有任何关系

### 类的导入

* 一个类可以使用所属包中的所有类，以及其他包中的共有类
* 一种方式是在每个类名之前添加完整的包名
* 另一个种方式是使用import语句
  * 位于源文件的顶部，package后面
  * 只能使用星号（\*）导入一个包，而不能使用`import java.*` 或 `import java.*.*` 导入java为前缀的所有包
* C++中，必须用#include将外部特性的声明加载进来，这是因为C++编译器无法查看任何文件的内部，除了正在编译的文件以及在头文件中明确包含的文件，而java编译器可以查看其它文件的内部
  * java中，package与import类似于C++中的namespace和using

### 静态导入

* import不仅可以导入类，还可以导入**静态方法**和**静态域**

* 例如：

  ```java
  import static java.lang.System.*;
  
  out.println("----1------");    // 使用System的静态方法和静态域，不用类名
  ```

* 还可以导入特定的方法或域

  ```java
  import static java.lang.System.out;
  ```

### 将类放入包中

* 将包的名字放在源文件开头
* 没有package语句，则放置在默认包（default package）中，没有名字的包
* 编译器在变异源文件的时候不检查目录结构
  * 如果不依赖其他包，不会出现编译错误



---



## 继承

* 在Java中，所有的继承都是公有继承
* super不是一个对象的引用，不能将super赋给另一个对象变量，它只是一个指示编译器调用超类方法的特殊关键字
* 子类中可以增加域、增加方法或覆盖方法，但是不能删除继承的任何域和方法
* super调用构造器的语句必须是子类构造器的第一条语句
  * 如果子类的构造器没有显式的调用超类的构造器，则将自动的调用超类默认构造器
  * 如果超类没有默认构造器，并且在子类构造器中又没有显式的调用超类的其他构造器，则Java编译器将报告错误

### 多态

> **多态（polymorphism）：**一个对象变量可以指示多种实际类型的现象

* 置换法则（is-a规则)：出现超类对象的任何地方都可以用子类对象置换
  * 例如，可以将一个子类对象赋给超类变量，但是不能将一个超类的引用赋给子类变量
* 对象变量是多态的

* **动态绑定（dynamic binding）：**在运行时能够自动的选择调用哪个方法的现象
  * 特性：无需对现存的代码进行修改，就可以对程序进行扩展
* 覆盖方法的时候，子类方法可见性不能低于超类方法，即子类方法不能更严格

### 理解方法调用

1. 编译器查看对象的**声明类型**和**方法名**，查找隐式参数类及其父类中可访问的所有该方法名的方法
2. 编译器查看调用方法时提供的**参数类型**，如果在所有名对应的方法中存在一个与提供的参数类型完全匹配，就选择这个方法，这个过程叫**重载解析（overloading resolution）**
3. 如果编译器没有找到与参数类型匹配的方法，或者发现经过类型转换后有多个方法与之匹配，就会报告一个错误
   * 覆盖方法时，一定要保证返回类型的兼容性，允许子类将覆盖方法的返回类型定义为原返回类型的子类型，称这两个方法具有可协变的返回类型
4. **静态绑定（static binding）**：如果是private方法、static方法、final方法或者构造器，那么编译器将可以准确地知道应该调用哪个方法
5. **动态绑定**，调用的方法依赖于隐式参数的实际类型，并且在运行时实现动态绑定。采用动态绑定调用方法时，虚拟机一定调用与隐式参数所引**用对象实际类型最合适**的那个类的方法
   * **先找当前类，再找超类**

* **方法表（method table）：**列出了所有方法的签名和实际调用的方法
  * 避免时间开销过大
  * 每个类都有方法表

### final类和方法

* final类不能继承
* final方法不能覆盖
* 如果一个类声明为final，只有其中的方法自动成为final，而不包括域

### 强制类型转换

> **类型转换：**将一个类型强制转换成另外一个类型的过程

* 用一对圆括号将目标类名括起来，并放置在需要转换的对象引用之前即可
* 类型转换的原因：暂时忽视对象的实际类型，使用对线的全部功能
* 只能在继承层次内进行类型转换
* 在将超类转换成子类之前，应该使用instanceof进行检查（向下转换）

### 抽象类

> **抽象类：**abstract修饰的类

* 关键字：abstact
* 包含一个或多个抽象方法的类本身必须声明为抽象的
* 抽象类可以包含具体数据和方法
* 扩展抽象类时
  * 可以实现部分方法，但是需要声明为抽象类
  * 也可以实现全部方法，无需声明为抽象类
* 类即使不含抽象方法，也可以声明为抽象类
  * 如果该类包含任何的abstract方法都显得没有实际意义，并且不想这个类产生任何对象时，就很有用
* 抽象类不能被实例化
* 可以定义一个抽象类的对象变量，但是只能引用非抽象子类的对象

### 访问修饰符

| 修饰符  |  类  |  包  | 子类 | 其他包 |
| :-----: | :--: | :--: | :--: | :----: |
| public  |  ✔   |  ✔   |  ✔   |   ✔    |
| protect |  ✔   |  ✔   |  ✔   |   ✘    |
| default |  ✔   |  ✔   |  ✘   |   ✘    |
| private |  ✔   |  ✘   |  ✘   |   ✘    |



## Object：所有类的超类

* Object类是Java中所有类的始祖，在Java中每个类都是由它扩展而来的
* 可以使用Object类型的变量引用任何类型的对象
* 只有基本类型不是对象，所有的数组类型都扩展了Object类

### equals方法

> 用于检测一个对象是否等于另外一个对象

* 源码：

  ```java
  public boolean equals(Object obj) {
     return (this == obj);
  }
  ```

* Object中的equals用`==`实现，判断两个对象是否具有相同的引用

* 具体类使用时，需要重写equals方法

### hashcode方法

* **散列码（hash code）**
  * 由对象导出的一个整型值
  * 没有规律
* 由于hashCode方法定义在Object类中，所以每个对象都有一个默认的散列码，值为**对象的存储地址**
* 如果重新定义equals方法，就必须重新定义hashCode方法，以便用户可以将对象插入到散列表中
* Equals与hashCode的定义必须一致：如果x.equals(y)返回true，那么x.hashCode()就必须与y.hashCode()具有相同的值

### toString方法

> 返回表示对象值的字符串

* 只要对象与一个字符串通过操作符“+”连接起来，java编译就会自动的调用toString方法
  * 在调用x.toString()的地方可以用“+”替代，如果x是基本类型，一样可以执行
* Object类定义了toString方法，用来打印输出**对象所属的类名**和**散列码**
* 打印数组：`Arrays.toString()`，多维数组：`Arrays.deepToString()`



## 对象包装器与自动装箱

* 所有基本类型都有一个与之对应的类，这些类称为包装器（wrapper）
  * Integer、Long、Float、Double、Short、Byte、Character、Void、Boolean
* 对象包装器类是不可变的，一旦构造了包装器，就不允许更改包装在其中的值
* 对象包装器是final，不能定义子类

```java
Array<Integer> list = new ArrayList<>();

list.add(3);
// 上句将自动变换成
list.add(Integer.valueOf(3));
```

* 这种变换称为**自动装箱（autoboxing）**

```java
int n = list.get(i);
// 转换为
int n = list.get(i).intValue();
```

* 这种变换称为**自动拆箱**
* 装箱和拆箱是**编译器**认可的，而不是虚拟机
  * 编译器在生成类的字节码文件事，插入必要的方法调用，虚拟机只是执行这些字节码



## 参数数量可变的方法

* 自Java SE 5.0以来，提供了可变参数数量调用的方法（变参方法）

* printf方法定义如下：

  ```java
  public class PrintStream{
      public PrintStream printf(String fmt, Object... args){
      	return format(fmt, args);
      }
  }
  ```

* 省略号表示这个方法可以接受任意数量的参数

* Object...参数和Object[]完全一样

* 可以自己定义可变参数的方法，参数可以指定为任意类型，甚至是基本类型

* 允许将一个数组传递个可变参数方法的最后一个参数

  ```java
  // main可以声明为以下形式
  public static void main(String... args)
  ```



## 枚举类





---

# java高级

## 接口

* 接口不是类，而是对类的一组需求描述
* 接口中的所有方法自动属于**public**，在接口中声明方法时，不必提供关键字public
* 接口中绝不能含有实例域，java se 8开始，可以在接口中提供简单方法
* 实现接口的步骤：
  1. 将类声明为实现给定的接口
  2. 对接口中**所有方法**进行定义
* 接口中所有方法自动是public，但是**在实现接口时，必须把方法声明为public**，否则，编译器会提示更严格的访问权限
* 接口不是类，不能使用new运算符实例化一个接口
* 不能构造接口的对象，但是能声明接口的变量，接口变量必须引用实现了接口的类对象
* 可以使用instanceof检查一个对象是否实现了某个特定的接口
* 接口可以扩展，允许存在多条从具有较高通用性的接口到较高专用性的接口的链
* 接口中不能包含实例域或静态方法，但是可以包含常量，接口中的域自动设为`public static final`
* 每个类只能有一个超类，但却可以实现多个接口，使用逗号将实现的各个接口分隔开

### 静态方法

* java se 8中，允许在接口中增加静态方法
* 通常的做法都是将静态方法放在**伴随类**中
  * 标准库中成对出现的接口和实用工具类，如Collection/Collections, Path/Paths

### 默认方法

* 可以为接口方法提供一个默认实现，必须用**default修饰符**标记这样一个方法

  ```java
  public interface Comparable<T>{
      default int compareTo(T other){
          return 0;
      }
  }
  ```

* 可以实现自己需要的，而不用实现一些不需要使用的

* 默认方法可以调用任何其他方法

* 很多接口都有相应的伴随类，实现了相应接口的部分或所有方法，如Collection/AbstractCollection或MouseListener/MouseAdapter，在java se 8中，这个技术已经过时，现在可以直接在接口中实现方法

* **接口演化（interface evolution）**：为接口增加方法时，设为default，可以不用修改实现类

### 默认方法冲突

在接口中将一个方法定义为默认方法，然后又在超类或另一个接口中定义了一个同样的方法，规则如下：

1. **超类优先**。如果超类提供了一个具体方法，同名而且有相同参数类型的默认方法会被忽略
   * 类优先规则，保证了与java se 7的兼容性，如果为一个接口增加默认方法，这对于之前正常工作的代码没有任何影响
   * 不要让一个默认方法重新定义Object类中的某个方法，由于类优先规则，这样的方法永远不能超越Object的方法
2. **接口冲突**。如果一个超接口提供了一个默认方法，另一个接口提供了一个同名而且参数类型相同的方法，**必须覆盖这个方法解决冲突**
   * 如果至少有一个接口提供了一个实现，编译器就会报错



### 对象克隆

* clone方法是Object的一个protected方法

  * 子类只能调用受保护的clone方法来克隆它自己的对象
  * 必须重新定义clone为**public**才能允许所有方法克隆对象

* 默认的克隆操作是“浅拷贝”，不拷贝引用

* Cloneable接口是Java提供的一组**标记接口（tagging interface）**，或叫**记号接口（marker interface）**

  * 标记接口不包含任何方法，唯一的作用就是允许在类型查询汇总使用instanceof

* java se 1.4之前，clone的方法返回类型总是Object，而现在可以为clone方法指定正确的返回类型，这是协变返回类型的一个例子

  ```java
  class Employee implements Cloneable{
      // raise visibility level to public, change return type
      public Employee clone() throws CloneNotSupportedException{
          return (Employee)super.clone();
  	}
      // ...
  }
  ```

* 如果在一个对象上调用clone，但这个对象的类并没有实现Cloneable接口，Object类的clone方法就会抛出一个CloneNotSupportedException

* 所有数组类型都有一个public的clone方法，可以用这个方法建立一个新数组，包含原数组所有元素的副本

* 在拷贝一个对象时，要想让这个拷贝的对象和源对象完全彼此独立，那么在引用链上的每一级对象都要被显式的拷贝

## 标记接口（tagging）

* 没有任何方法以及属性的接口
* 不对实现它的类有任何语意上的要求，它仅仅表明实现它的类属于一个特定的类型
* 比如java.io.Serializable和java.rmi.Remote等接口便是标识接口
* 当一个类实现了一个标识接口之后就像是给自己打了个标签



## lambda表达式

* java中不能直接传递代码段，必须构造一个对象，对象的类需要有一个方法包含所需的代码

* lambda表达式是一个可传递的代码块，可以在以后执行一次或多次

* lambda表达式就是一个代码块，以及必须传入代码的变量规范

* lambda的基本语法：

  ```java
  (parameters) -> expression		// 箭头后面是表达式
  // 或（注意语句的花括号）
  (parameters) -> { statements; }	// 箭头后面是语句
  ```

* lambda表达式的一种形式：参数，箭头（->）以及一个表达式，如果代码无法放在一个表达式中，可以放在{}中，并包含显式的return语句

  ```java
  (String first, String second) ->{
      if (first.length() < second.length()) return -1;
      else if (first.length() > second.length()) return 1;
      else return 0;
  }
  ```

* 即使lambda表达式没有参数，仍然要提供空括号，就像无参方法一样

  ```java
  () -> {
      for (int i = 100; i >= 0; i--)
      	System.out.println(i);
  }
  ```

* 如果可以推导出一个lambda表达式的参数类型，则可以忽略其类型

  ```java
  Comparator<String> comp = (first, second)	// same as (String f, String s)
  	-> first.length() - second.length();
  ```

* 如果方法只有一个参数，并且这个参数的类型可以推导得出，那么可以省略小括号

  ```java
  ActionListener listener = event ->
  	System.out.println("The time is " + new Date());
  ```

* 无需指定lambda表达式的返回类型，返回类型总是会由上下文推导得出

* lambda表达式各分支必须都有返回值

  ```java
  (int x) -> {	
      if (x > 0)		// 不合法
      	return 1;
  }
  ```



> #### 判断下面哪个不是有效的lambda表达式？
>
> ```java
> (1) () -> {}
> (2) () -> "Raoul"
> (3) () -> { return "Mario"; }
> (4) (Integer i) -> return "Alan" + i;
> (5) (String s) -> { "Iron Man"; }
> ```
>
> * 答案：
>
> ```java
> 只有4和5是无效的lambda表达式。
> (1) 这个lambda没有参数，并返回void。它类似于主体为空的方法：public void run() {} 
> (2) 这个lambda没有参数，并返回String作为表达式。
> (3) 这个lambda没有参数，并返回String（利用显示返回语句）
> (4) return是一个控制流语句。要使此lambda有效，需要使用花括号，
> 	如：(Integer i) -> { return "Alan" + i; }
> (5) "Iron Man"是一个表达式，不是一个语句。要使此lambda有效，可以去除花括号和分号，
> 	如：(String s) -> "Iron Man"。
> 	还可以使用显示返回语句，
> 	如：(String s) -> { return "Iron Man"; }
> ```
>

  


> #### 一些lambda的例子和使用案例
>
> ```java
> // 布尔表达式
> (List<String> list) -> list.isEmpty();
> // 创建对象
> () -> new Apple(10)；
> // 消费一个对象
> (Apple a) -> {
> 	System.out.println(a.getWeight());
> }
> // 从一个对象中选择/抽取
> (String s) -> s.length();
> // 组合两个值
> (int a, int b) -> a * b;
> // 比较两个对象
> (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
> ```
>



### 函数式接口

* 对于**只有一个抽象方法的接口**，需要这种接口的对象时，就可以提供一个lambda表达式，这种接口称为**函数式接口（functional interface）**  

  ```java
  public interface Predicate<T> {
      boolean test(T t);
  }
  ```

* 接口可以拥有默认方法，但是哪怕有很多**默认方法**，只要接口定义了一个**抽象方法**，就仍然还是一个函数式接口

> #### 判断下面哪些接口是函数式接口？
>
> ```java
> public interface Adder {
>     int add(int a, int b);
> }
> 
> public interface SmartAdder extends Adder {
>     int add(double a, double b);
> }
> 
> public interface Nothing {
> }
> ```
>
> 答案：只有Adder是函数式接口。
>
> SmartAdder不是函数式接口，因为它定义了两个叫做add的抽象方法（其中一个是从Adder那里继承过来的）。
>
> Nothing也不是函数式接口，因为它没有声明抽象方法。



### 方法引用（method reference）

```java
Timer t = new Timer(1000, event -> System.out.println(event));

Timer t = new Timer(1000, System.out::println);	// 方法引用，等价于lambda表达式
```

用::操作符分隔方法名与对象或类名，主要有三种情况：

1. `object::instanceMethod`（object实例方法）
   * 等价于提供方法参数的lambda表达式
   * `System.out::println`等价于``x -> System.out.println(x)``
2. `Class::staticMethod`（静态方法）
   * 等价于提供方法参数的lambda表达式
   * `Math::pow`等价于``(x, y) -> Math.pow(x, y)``
3. `Class::instanceMethod`（普通实例方法）
   * 第一个参数会成为方法的目标
   * `String::compareToIgnoreCase`等价于`(x, y) -> x.compareToIgnoreCase(y)`

* 如果有多个同名的重载方法，编译器就会尝试从上下文中找出你指的那一个方法

* 类似于lambda表达式，方法引用不能独立存在，总是会转换为函数式接口的实例

* 可以在方法引用中使用this参数，`this::equals`等同于`x -> this.equals(x)`

* 使用super也是合法的

  * `super::instanceMethod` 使用this作为目标，会调用给定方法的超类版本

    ```java
    class Greeter{
        public void greet(){
            System.out.println("Hello World!");
        }
    }
    
    class TimedGreeter extends Greeter{
        public void greet(){
            Timer t = new Timer（1000, super::greet); // 调用超类中greet方法
            t.start();
        }
    }
    ```

### 构造器引用

* 构造器引用和方法引用很类似，只不过方法名为new
  * Person::new是Person构造器的一个引用
* 可以用数组类型建立构造器引用
  * int[]::new是一个构造器引用，有一个参数：数组的长度，等价于lambda表达式`x -> new int[x] `

### 变量作用域

* 在lambda表达式中访问外围方法或类中的变量

  ```java
  public static void repeatMessage(String text, int delay){
      ActioinListener listener = event ->{
          System.out.println(text);
          Toolkit.getDefaultToolkit().beep();
      };
      new Timer(delay, listener).start();
  }
  ```

* lambda表达式三个部分

  1. 一个代码块
  2. 参数
  3. 自由变量的值，指非参数而且不在代码中定义的变量

* 上例有一个自由变量text，表示lambda表达式的数据结构必须存储自由变量的值，说它被lambda表达式**捕获**

* lambda表达式可以捕获外围作用域中变量的值

* lambda表达式中，只能引用**值不会改变**的变量

  ```java
  public static void countDown(int start, int delay){
      ActionListener listener = event -> {
          start--;	// Error: Can't mutate captured variable
          System.out.println(start);
      };
      new Timer(delay, listener).start();
  }
  ```

* 如果在lambda表达式中改变变量，并发执行多个动作时就会不安全

* 如果在lambda表达式中引用变量，而这个变量可能在外部改变，也是不合法的

  ```java
  public static void repeat(String text, int count){
      for (int i = 1; i <= count; i++){
          ActionListener listener = event -> {
              System.out.println(i + text); // Error: Can't refer to changing i
          };
          new Timer(1000, listener).start();
      }
  }
  ```

* lambda表达式中捕获的变量必须实际上是**最终变量（effectively final）**

  * 指变量初始化后不会再为它赋新值
  * text总是指向同一个String对象，所以捕获合法

* lambda表达式的体与嵌套块有相同的作用域

* 在lambda表达式中声明一个局部变量同名的参数或局部变量是不合法的

  ```java
  Path first = Paths.get("/usr/bin");
  Comparator<String> comp = (first, second) -> first.length() - second.length();
  // Error: Variable first already defined
  ```

* lambda表达式中不能有同名的局部变量

* 在lambda表达式中使用this关键字时，指创建这个lambda表达式的方法的this参数

  ```java
  public class Application(){
      public void init(){
          ActionListener listener = event -> {
              System.out.println(this.toString());
          }
      }
  }
  ```

* lambda由Application创建，所以，this指向Application，调用Application对象的toString方法



## 内部类

> **内部类（inner class）**：定义在另一个类中的类

内部类的作用：

* 内部类的方法可以**访问**该类定义所在的作用域中的**数据**，包括私有数据
* 内部类可以对同一个包中的其他类**隐藏**起来
* 当想要定义一个**回调函数**且不想编写大量代码时，使用**匿名内部类**比较便捷

### 使用内部类访问对象状态

```java
package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: TLX
 * Date: 2019.3.12
 * Time: 12:18
 */
class TalkingClock {
    private int interval;
    private boolean beep;

    public TalkingClock(int interval, boolean beep) {
        this.interval = interval;
        this.beep = beep;
    }

    public void start() {
        ActionListener listener = new TimePrinter();
        Timer t = new Timer(interval, listener);
        t.start();
    }

    // an inner class
    public class TimePrinter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("At the tone, the time is " + new Date());
            if (beep) {	// 外围类的数据域
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}

public class InnerClassTest {
    public static void main(String[] args) {
        TalkingClock clock = new TalkingClock(1000, true);
        clock.start();

        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }
}

```

* 内部类既可以访问**自身的数据域**，也可以访问创建它的**外围类对象的数据域**

* 内部类对象总有一个**隐式引用**，指向了**创建它的外部类对象**

  * 这个引用在内部类的定义中是不可见的

  * 外围类的引用在构造器中设置，编译器修改了所有的内部类的构造器，添加一个外围类引用的参数

    ```java
    public TimePrinter(TalkingClock clock){
        outer = clock; // outer不是java关键字，只是用它说明机制
    }
    ```

* 只有内部类可以是私有类，而常规类只可以具有**包可见性（default）**或**公有可见性（public）**，不可被private和protected修饰



### 内部类的特殊语法规则

外围类引用：`OuterClass.this`

```java
public void actionPerformed(ActionEvent event){
    // ...
    if (TalkingClock.this.beep)
    	Toolkit.getDefaultToolkit().beep();
}
```

编写内部对象的构造器：`outerObject.new InnerClass(construction parameters)`

```java
ActionListener listener = this.new TimePrinter();
```

在外围类的作用域之外，可以这样引用内部类：`OuterClass.InnerClass`

内部类中声明的**所有静态域都必须是final**

* 我们希望一个静态域只有一个实例，不过对于每个外部对象，会分别有一个单独的内部类实例，如果这个域不是final，它可能不是唯一的

内部类**不能有static方法**

* 非static的内部类，在外部类加载的时候，并不会加载它，所以它里面不能有静态变量或者静态方法。
  * static类型的属性和方法，在**类加载**的时候就会存在于内存中。
  * 要使用某个类的static属性或者方法，那么这个类必须要加载到jvm中。
* 基于以上两点，可以看出，如果一个非static的内部类如果具有static的属性或者方法，那么就会出现一种情况：**内部类未加载，但是却试图在内存中创建static的属性和方法**，这当然是错误的。原因：类还不存在，但却希望操作它的属性和方法。

虽然说不能有static修饰的属性，但是**可以有常量（static final修饰），与上面所有静态域都必须是final对应**。

* java**常量放在内存中常量池**，它的机制与变量是不同的，编译时，**加载常量是不需要加载类的**



### 内部类是否有用、必要和安全

* 内部类是一种**编译器现象，与虚拟机无关**，编译器会把内部类翻译成用**\$**分隔外部类名与内部类名的常规类文件，而虚拟机对此一无所知

  * 在TalkingClock类内部的TimePrinter类将被翻译成类文件**TalkingClock$TimePrinter.class**

* 测试是否可以自己实现这种机制

  ```java
  class TalkingClock{
      public void start(){
          ActionListener listener = new TimePrinter(this); // 创建对象时传递this指针
          Timer t = new Timer(interval, listener);
          t.start();
      }
  }
  class TimePrinter implements ActionListener{
      private TalkingClock outer;
      // 模拟内部类的构造函数，将创建该对象的this指针传递给它
      public TimePrinter(TalkingClock clock){
          outer = clock;
      }
  }
  ```

  ```java
  if (outer.beep)		// error
  ```

* 内部类可以访问**外围类的私有数据**，但是这里TimePrinter类则不行

* 编译器在外围类添加静态方法access$0

  ```java
  class TalkingClock{
      private int interval;
      private boolean beep;
      
      public TalkingClock(int, boolean);
      
      static boolean access$0(TalkingClock);	// 返回传递给它的对象域的beep
      
      public void start();
  }
  ```

### 局部内部类

* TimePrinter这个类名只在start方法中创建这个类型的对象时使用了一次，可以在一个方法中定义**局部类**

  ```java
  public void start(){
      class TimePrinter implements ActionListener{
          System.out.println("At the tone, the time is " + new Date());
          if (beep)
          	Toolkit.getDefaultToolkit().beep();
      }
      
      ActionListener listener = new TimePrinter();
      Timer t = new Timer(interval, listener);
      t.start();
  }
  ```

* **局部类不能用public或private进行声明**，它的作用域被限定在声明这个局部类的块中

  * 局部变量没有访问权限修饰符，不能用public、private和protected来修饰，只能在定义它的方法内部使用。

* 局部类可以**对外部完全的隐藏**起来，上面代码中，除了start方法外，没有任何方法知道这个类的存在

### 匿名内部类

* 如果只创建这个类的一个对象，就不必命名了，这种类称为**匿名内部类（anonymous inner class）**

  ```java
  public void start(int interval, boolean beep){
      ActionListener listener = new ActionListener(){
          System.out.println("At the tone, the time is " + new Date());
          if (beep)
          	Toolkit.getDefaultToolkit().beep();
      }
      Timer t = new Timer(interval, listener);
      t.start();
  }
  ```

* 通常的语法格式

  ```java
  new SuperType(construction parameters){
      inner class methods and data
  }
  ```

* SuperType可以是接口，内部类实现这个接口，也可以是一个类，内部类扩展这个类

* 由于构造器的名字必须和类名相同，而匿名类没有类名，所以，**匿名类不能有构造器**，取而代之的是，将构造器参数传递给超类构造器

* **双括号初始化（double brace initialization）**

  ```java
  ArrayList<String> friends = new ArrayList<>();
  friends.add("Harry");
  friends.add("Tony");
  invite(friends);
  ```

* 可以简化为

  ```java
  invite(new ArrayList<String>(){
      {
          add("Harry");
          add("Tony");
      }
  });
  ```

* 外层括号建立了ArrayList的一个匿名子类，内层括号则是一个对象构造块

### 静态内部类

* 将内部类声明为static，可以**取消内部类对外围类对象的引用**
* **只有内部类可以声明为static**
* 静态内部类的对象除了没有对生成它的外围类对象的引用外，与其他内部类完全一样
* 在内部类**不需要访问外围类对象**的时候，应该使用静态内部类，有些程序员用**嵌套类（nested class）**表示静态内部类
* 与常规内部类不同，静态内部类可以有**静态域和方法**
* 声明在**接口**中的内部类自动成为**static和public类**



## 异常

> 如果出现RuntimeException异常，那么就一定是你的问题。

* 所有的异常都是由Throwable继承而来，下层分成
  * Error
    * 描述了java运行时系统的内部错误和资源耗尽错误
    * 不应该抛出这种类型的对象
  * Exception
    * RuntimeException：由程序错误导致的异常
      * 错误的类型转换
      * 数组访问越界
      * 访问null指针
    * 其他异常：程序本身没有问题，但由于像I/O错误这类问题导致的异常
      * 试图在文件尾部后面读取数据
      * 试图打开一个不存在的文件
      * 试图根据给定的字符串查找Class对象，而这个字符串表示的类并不存在
* **非受查异常（unchecked）**：派生于Error类或RuntimeException类的所有异常
* **受查异常（checked）**：其他的异常

### 声明受查异常

* 方法应该在其首部声明所有可能抛出的异常
* 应该抛出异常的情况：
  * 调用一个抛出**受查异常**的方法
  * 程序运行过程中发现**错误**，并且利用throw语句抛出一个受查异常
  * 程序出现错误
  * jvm和运行库出现的**内部错误**
* 如果一个方法有可能抛出多个受查异常类型，那么就必须在方法的首部列出**所有的异常类**，每个异常类之间用**逗号**隔开
* 一个方法必须声明所有可能抛出的**受查异常**，而非受查异常要么不可控制（Error），要么应该避免发生（RuntimeException）
* 子类方法中声明的受查异常**不能比超类方法中声明的异常更通用**

### 创建异常类

* 定义一个派生于Exception的类，或者派生于Exception子类的类
* 包含两个构造器
  * 默认的构造器
  * 带有详细描述信息的构造器

```java
class FileFormatException extends IOException{
	public FileFormatException(){}
    public FileFormatException(String gripe){
		super(gripe);
    }
}

// 调用
throw new FileFormatException();
```

### 捕获异常

* 如果某个异常发生的时候**没有任何地方进行捕获**，那程序就会终止执行，并在控制台上打印出异常信息，其中包括异常的类型和堆栈的内容

* 捕获异常，必须设置try/catch语句块

  ```java
  try{
      code
      more code
      more code
  }catch (ExceptionType e){
      handle for this type
  }
  ```

* 应该捕获那些知道如何处理的异常，而将那些不知道怎样处理的异常继续进行传递

* 如果覆盖超类方法，超类方法还没有抛出异常，这个方法必须捕获方法代码中的所有受查异常

  * 不允许在子类的throws说明符中出现超过超类方法列出的异常范围

### finally子句

* finally语句一定执行

* try语句可以只有finally子句，而没有catch子句

* 方法返回前，finally子句的内容将被执行，如果finally子句中也有一个return语句，这个返回值将会覆盖原始的返回值

  ```java
  public static int f(int n){
  	try{
   		int r = n * n;
          return r;
  	} finally{
          if(n == 2)
          	return 0;
  	}
  }
  ```

  * 如果调用f(2)，应该返回4，但是finally会导致返回0

### 带资源的try语句

```java
try (Resource res = ...){
    work with res
}
```

* try退出时，会自动调用res.close()
* 还可以指定多个资源，分号分隔
* 带资源的try语句自身也可以有catch子句和一个finally子句，这些子句会在**关闭资源之后**执行



## 断言



## 日志





---



## 泛型

* 泛型程序设计（Generic programming）意味着编写的代码可以被很多不同类型的对象所重用

### 类型参数的好处

* 在java增加泛型类之前，泛型程序设计是用**继承**实现的

  ```java
  public class ArrayList{
      private Object[] elementData;
      // ...
      public Object get(int i) {...}
      public void add(Object o) {...}
  }
  ```

* 当获取一个值时，必须进行**强制类型转换**

  ```java
  ArrayList list = new ArrayList();
  ...
  String s = (String)list.get(0);
  ```

* 可以向数组列表中添加任何类的对象

  ```java
  list.add(new File("..."));
  ```

* 泛型提供的解决办法：**类型参数（type parameters）**

  ```java
  ArrayList<String> list = new ArrayList<String>();
  ```

* 在java se 7及以后的版本中，构造函数中可以省略泛型类型，**菱形语法**

  ```java
  ArrayList<String> list = new ArrayList<>();
  ```

### 泛型类

> **泛型类（generic class）**：具有一个或多个类型变量的类

```java
public class Pair<T> {
    private T first;
    private T second;

    public Pair() {
    }

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }
}
```

* 类型变量使用大写形式，且比较短
* java库中，使用变量**E**表示**集合元素类型**，**K**和**V**分别表示**表的关键字**与**值的类型**，**T（U、S）**表示**任意类型**

### 泛型方法

```java
class ArrayAlg{
    public static <T> T getMiddle(T... a){
        return a[a.length / 2];
    }
}
```

* 类型变量放在修饰符的后面，返回类型的前面

* 泛型方法可以定义在普通类中，也可以定义在泛型类中

* 调用一个泛型方法时，尖括号中放入具体的类型

  ```java
  String middle = ArrayAlg.<String>getMiddle("join", "Q", "public");
  // 大多情况也可以省略，编译器可推断出
  String middle = ArrayAlg.getMiddle("join", "Q", "public");
  ```

### 类型变量的限定

```java
<T extends BoundingType>
```

* 表示T应该是绑定类型的子类型，T和绑定类型可以是类，也可以是接口

* 一个类型变量或通配符可以有多个限定

  * 限定类型用&分隔，类型变量用逗号分隔

  ```java
  T extend Comparable & Serializable
  ```

### 泛型代码和虚拟机

* 虚拟机没有泛型类型对象，所有对象都属于普通类

#### 类型擦除

* 无论何时定义一个泛型类型，都自动提供了一个相应的**原始类型（raw type）**

* 原始类型就是删除类型参数，擦除类型变量，并替换为**第一个限定类型**（无限定变量用**Object**）

  ```java
  public class Pair {
      private Object first;
      private Object second;
  
      public Pair() {
      }
  
      public Pair(Object first, Object second) {
          this.first = first;
          this.second = second;
      }
  
      public Object getFirst() {
          return first;
      }
  
      public void setFirst(Object first) {
          this.first = first;
      }
  
      public Object getSecond() {
          return second;
      }
  
      public void setSecond(Object second) {
          this.second = second;
      }
  }
  
  ```

#### 翻译泛型表达式

* 调用泛型方法时，如果擦除**返回类型**，编译器插入**强制类型转换**

  ```java
  Pair<Employee> buddies = ...;
  Employee buddy = buddies.getFirst();
  ```

* 编译器把这个方法调用翻译为两条虚拟机指令

  * 对原始方法Pair.getFirst的调用
  * 将返回的Object类型强制转换为Employee类型

* 当存取一个泛型域时也要插入强制类型转换

#### 翻译泛型方法

##### 继承泛型类型的多态麻烦 ---子类没有覆盖住父类方法

```java
class Son extends Pair<String>{
    public void setSecond(String second){
        ...
    }
}
```

类型擦除后变成

```java
class Son extends Pair{
    public void setSecond(String second){
        ...
    }
}
```

存在一个从父类Pair继承过来的setSecond方法

```java
public void setSecond(Object second){
    ...
}
```

本意应该是在子类Son中覆盖父类`Pair<String>`中的`setSecond(T second)`方法，但实际上，由于`Pair<String>`在编译阶段被类型擦除为Pair，他的setSecond方法变成了`setSecond(Object second)`，导致Son中的`setSecond(String second)`方法无法覆盖住此时父类Pair中的`setSecond(Object second)`方法，从而类型擦除与多态发生了冲突。

为了解决这个问题，编译器在Son类中会生成一个**桥方法（bridge method）**去覆盖住类型擦除后父类中的方法

```java
public void setSecond(Object second){	// 桥方法
    ...
}
```

此时，Son类中有两个方法

```java
public void setSecond(String second)
public void setSecond(Object second)
```

桥方法解决了覆盖住父类方法的问题，但是却引来了另一个问题，上述两个方法签名不一样可以共存，但如果签名一样呢

如果想在Son类中覆盖getSecond方法，Son类中会出现下面两个方法

```java
public String getSecond()	// override
public Object getSecond()	// bridge method
```

**这两个方法签名一样，只有返回类型不一样。**

在java代码中，这不合法，但是在虚拟机中却是可以。

* **方法重载**要求方法具备不同的**特征签名**，返回值并不包含在方法的特征签名之中，所以返回值不参与重载选择，但是在**Class文件**格式之中，只要**描述符**不是完全一致的两个方法就可以共存
* 《java虚拟机规范（第二版）》中提到，特征签名最重要的任务就是作为方法独一无二且不可重复的ID
  * 在**java代码**中的方法特征签名只包括了**方法名称**、**参数顺序**及**参数类型**
  * 而在**字节码**中的特征签名还包括**方法返回值**及**受查异常表**



