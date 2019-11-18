## 使用tomcat插件运行后，无法关闭

运行命令：

```bash
mvn tomcat7:run
```

此时，只能从后台任务管理器关闭

* 打开cmd

* 输入命令，并记住查到的pid

  ```bash
  netstat -ano | findstr "端口号"
  ```

  * netstat：显示协议统计信息和当前 TCP/IP 网络连接
    * -a：显示所有连接和侦听端口
    * -n：以数字形式显示地址和端口号
    * -o：显示拥有的与每个连接关联的进程 ID

* 输入命令，kill进程

  ```bash
  taskkill -pid 查到的pid -F
  ```

  * taskkill：使用该工具按照进程 ID (PID) 或映像名称终止任务
    * -pid：指定要终止的进程的 PID
    * -F：指定强制终止进程

