## 命令行创建maven项目

### 方法一：互动模式

#### 1. 用骨架创建项目

切换到项目目录，输入命令：

```powershell
mvn archetype:generate
```

![01_cmd_创建项目.png](./img/01_cmd_创建项目.png)

#### 2. 选择骨架

输入对应号码即可。

![02_cmd_选择骨架.png](./img/02_cmd_选择骨架.png)

#### 3. 输入坐标

![03_cmd_输入坐标.png](./img/03_cmd_输入坐标.png)

#### 4. 创建成功

确认后，创建成功会输出成功信息。

![04_cmd_创建成功.png](./img/04_cmd_创建成功.png)

#### 5. 将项目转化为idea项目

```powershell
# 先进入项目目录
cd MvnTest01
# 转为idea项目
mvn idea:idea
```

![05_cmd_转为idea1.png](./img/05_cmd_转为idea1.png)

![06_cmd_转为idea2.png](./img/06_cmd_转为idea2.png)

可以看到目录中增加了项目信息

![07_cmd_转为idea3.png](./img/07_cmd_转为idea3.png)

在idea中，可以直接打开

![08_cmd_转为idea4.png](./img/08_cmd_转为idea4.png)









## 命令行中其他的一些mvn命令

#### 1. 编译,将Java 源程序编译成 class 字节码文件 

```powershell
mvn compile
```

如果出现以下错误，那么可以在项目的pom.xml文件中添加如下几行。

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
```

![09_cmd_compile_error.png](./img/09_cmd_compile_error.png)

修改完文件后，重新编译。

![10_cmd_compile_success.png](./img/10_cmd_compile_success.png)



#### 2.  测试，并生成测试报告 

导入到idea中，添加测试用例。

![12_cmd_test2.png](./img/12_cmd_test2.png)

命令行执行测试命令。

```powershell
mvn test
```

![11_cmd_test.png](./img/11_cmd_test.png)



#### 3.  将以前编译得到的旧的 class 字节码文件删除 

```powershell
mvn clean
```

![13_cmd_clean1.png](./img/13_cmd_clean1.png)

可以看到，target文件夹没了。

![14_cmd_clean2.png](./img/14_cmd_clean2.png)



#### 4.  打包，动态 web工程打 war包，Java工程打 jar 包 

```
mvn package
```

![15_cmd_package1.png](./img/15_cmd_package1.png)

![16_cmd_package2.png](./img/16_cmd_package2.png)

成功生成了jar包。

![17_cmd_package3.png](./img/17_cmd_package3.png)



#### 5.  将项目生成 jar 包放在仓库中，以便别的模块调用，安装到本地 

```
mvn install
```

![18_cmd_install1.png](./img/18_cmd_install1.png)

![19_cmd_install2.png](./img/19_cmd_install2.png)