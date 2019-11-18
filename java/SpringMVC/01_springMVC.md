## 常用注解

### 1. RequestParam

* 作用：
  * 把请求中指定名称的参数给控制器中的形参赋值。
* 属性：
  * value：请求参数中的名称
  * required：请求参数中是否必须提供此参数
    * 默认值：true，表示必须提供，如果不提供将报错

### 2. RequestBody

* 作用：
  * 用于获取请求体内容，直接使用得到的是`key=value&key=value...`结构的数据。
  * get请求方式不适用
* 属性：
  - required：是否必须有请求体。
    - 默认值：true
    - 取值是true时，get请求方式报错，取值为false，get请求得到的是null

```java
@RequestMapping("/testRequestBody")
public String testRequestBody(@RequestBody String body) {
    System.out.println("执行了...");
    System.out.println(body);
    return "success";
}
```

### 3. PathVaribale

* 作用：
  * 用于绑定url中的占位符。
  * 例如：请求url中`/delete/{id}`，这个`{id}`就是url占位符
  * url支持占位符是spring3.0之后加入的，是springmvc支持rest风格url的一个重要标志
* 属性：
  * value：用于指定url中占位符名称
  * required：是否必须提供占位符

### 4. RequestHeader

* 作用：
  * 用于获取请求消息头
* 属性：
  * value：提供消息头名称
  * required：是否必须有此消息头
* 注：在实际开发中不怎么用

### 5. CookieValue

* 作用：

  * 用于把指定cookie名称的值传入控制器方法参数

* 属性：

  * value：指定cookie的名称

  * required：是否必须有此cookie

### 6. ModelAttribute

* 作用：
  * 该注解是SpringMVC4.3 以后新加入的
  * 用于修饰方法和参数
    * 出现在方法上，表示当前方法会在控制器方法执行之前，先执行
      * 可以修饰没有返回值的方法，也可以修饰有具体返回值的方法
    * 出现在参数上，获取指定的数据给参数赋值

* 属性：
  * value：用于获取数据的key
    * key可以是pojo的属性名称，也可以是map结构的key
* 应用场景：
  * 当表单提交数据不是完整的实体类数据时，保证没有提交数据的字段使用数据库对象原来的数据
  * 例如：我们在编辑一个用户时，用户有一个创建信息字段，该字段的值是不允许被修改的。在提交表单数据是肯定没有此字段的内容，一旦更新会把该字段内容置为null，此时就可以使用此注解解决问题。

### 7. SessionAttribute

* 作用：
  * 用于多次执行控制器方法间的参数共享。
* 属性：
  * value：用于指定存入的属性名称
  * type：用于指定存入的数据类型