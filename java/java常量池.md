## 一、概述

常量池大体可分为两类：

* **静态常量池：** 即 .class文件中的常量池，class文件中的常量池不仅仅包含**字符串（数字）字面量**，还包含**类、方法的信息**，占用class文件绝大部分空间。
  * 主要用于存放两大类常量：
    * **字面量(Literal)**：相当于Java语言层面常量的概念，如**文本字符串**，声明为**final的常量值**等
    * **符号引用量(Symbolic References)**：属于编译原理方面的概念，包括了如下三种类型的常量：
      * 类和接口的全限定名
      * 字段名称和描述符
      * 方法名称和描述符
* **运行时常量池：**jvm虚拟机在完成类装载操作后，将class文件中的常量池载入到内存中，并保存在**方法区**中，我们**常说的常量池**，就是指方法区中的运行时常量池。
  * 具备动态性：Java语言并不要求常量一定只有编译期才能产生，也就是并非预置入CLass文件中常量池的内容才能进入方法区运行时常量池，运行期间也可能将新的常量放入池中，例如：String类的intern()方法

## 二、常量池的好处

常量池是为了**避免频繁的创建和销毁对象**而影响系统性能，其实现了**对象的共享**。

例如字符串常量池，在编译阶段就把所有的字符串文字放到一个常量池中。

* **节省内存空间：**常量池中所有相同的字符串常量被合并，只占用一个空间。
* **节省运行时间：**比较字符串时，\==比equals()快。对于两个引用变量，只用\==判断引用是否相等，也就可以判断实际值是否相等。



## 三、字符串常量池

两个方法使用常量池：

* 直接用字符串字面量赋值
* 使用intern方法

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = "Hel" + "lo";
String s4 = "Hel" + new String("lo");
String s5 = new String("Hello");
String s6 = s5.intern();
String s7 = "Hel";
String s8 = "lo";

System.out.println(s1 == s2);  // true
// 新建String对象的时候，如果直接使用字符串字面量赋值，那么字面量会直接放入class文件的常量池中。

System.out.println(s1 == s3);  // true
// Hotspot中编译时"Hel" + "lo"将直接变成"Hello"

System.out.println(s1 == s4);  // false
// new String("lo")这部分不是已知字面量，是一个不可预料的部分，编译器不会优化，
// 必须等到运行时才可以确定结果

System.out.println(s7+s8 == s1);  // false
// 相当于new String(s7 + s8)，存放在堆中

System.out.println(s4 == s5);  // false
// 都在堆中，指向不同地址

System.out.println(s1 == s6);  // true
// intern方法，首先在常量池中查找是否存在一份字面量相等的字符串，如果有的话就返回该字符串的引用，
// 没有的话就将它加入到字符串常量池中，并返回在常量池中的地址
```

**注：**在java 中，直接使用==操作符，比较的是两个字符串的引用地址，并不是比较内容，比较内容为String.equals()。

解析：

* 新建String对象的时候，如果直接使用字符串字面量赋值，那么字面量会直接放入class文件的常量池中。

* 编译器的优化，Hotspot中编译时"Hel" + "lo"将直接变成"Hello"，s7+s8则不会优化，因为不知道在之前的步骤中s7和s8会不会发生变化
* Java对于String的相加是用语法糖，通过StringBuilder实现的，例如s7+s8，先构造一个StringBuilder里面存放"Hel"，然后调用append()方法追加"lo"，然后将值为"Hello"的StringBuilder转化为String对象

* 对于所有包含new方式新建对象（包括null）的“+”连接表达式，它所产生的新对象都不会被加入字符串池中。



```java
String s = new String("abc"); // 创建了几个对象

String s = "abc";	// 创建了几个对象
```

第一句：

* 如果常量池中不存在"abc"，那么会创建两个对象，一个是new出来的在堆上的对象，另一个是常量池中的"abc"，字符串字面量一旦出现，就会自动复制到常量池（7之后复制的应该是引用）
* 如果常量池中存在"abc"，那么会创建一个对象，new出来在堆上的对象，不会在常量池中创建

第二句：

* 如果常量池中不存在"abc"，那么会创建一个对象，常量池中的对象
* 如果常量池中存在"abc"，那么会创建0个对象，直接返回常量池中的引用



## 四、包装类的常量池（缓存）

自动装箱就是`valueOf`这个方法，自动拆箱就是`intValue`方法。

除了包装类Long，Double，Float 没有实现这个缓存技术，其它的包装类均实现了它。

当我们给Integer对象赋一个int值的时候，会调用Integer类的静态方法`valueOf`，下面是`valueOf`的源码

```java
@HotSpotIntrinsicCandidate
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

```java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];

    static {
        // high value may be configured by property
        int h = 127;
        String integerCacheHighPropValue =
            VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
        if (integerCacheHighPropValue != null) {
            try {
                int i = parseInt(integerCacheHighPropValue);
                i = Math.max(i, 127);
                // Maximum array size is Integer.MAX_VALUE
                h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
            } catch( NumberFormatException nfe) {
                // If the property cannot be parsed into an int, ignore it.
            }
        }
        high = h;

        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);

        // range [-128, 127] must be interned (JLS7 5.1.7)
        assert IntegerCache.high >= 127;
    }

    private IntegerCache() {}
}
```

可以发现，如果使用`Integer e = 1 或 Integer e = Integer.valueOf(1)`，并且整型字面量在`-128 ~127`之间，那么不会new新的Integer对象，而是直接引用常量池中的Integer对象，可以当做基本类型比较。

```java
Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;

System.out.println(f1 == f2);	// true
System.out.println(f3 == f4);	// false
```

但是，如果使用`Integer e = new Integer(1)`，无论值是多少，都要作为对象比较。

```java
Integer n1 = new Integer(47);
Integer n1 = new Integer(47);

System.out.println(n1 == n2);	// false
System.out.println(n1 != n2);	// true
```

