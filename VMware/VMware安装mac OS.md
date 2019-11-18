

# VMware安装mac OS

1. ### unlocker208

   ![unlocker.png](./img/unlocker.png)

   管理员运行**win-install.cmd**文件，之后新建虚拟机时便会出现mac OS选项

2. 按照正常步骤创建虚拟机后，运行虚拟机会出现错误提示。

   在该虚拟机目录下寻找***.vmx**文件，记事本打开，找到`smc.present = "TRUE"`，在此行下面添加`smc.version = "0"`，如下图

   ![vmx.png](./img/vmx.png)

3. 重新启动虚拟机，如果出现下图蓝屏**boot manager**的现象，则是镜像包的问题，**CRD镜像文件**可以成功安装。

   ![boot.png](./img/boot.png)

4. 成功启动后显示如下画面，等待安装

   ![apple.png](./img/apple.png)

5. 进度条完成后选择语言

   ![language.png](./img/language.png)

6. 选择 实用工具 - 磁盘工具 进行分区

   ![disk.png](./img/disk.png)

7. 选择磁盘，点击抹掉，设置名称，点击抹掉

   ![disk_operate.png](./img/disk_operate.png)

8. 分区成功

   ![disk_success.png](./img/disk_success.png)

9. 退出磁盘工具，点击继续，同意服务什么的，出现如下画面，将系统装在刚才创建的分区中

   ![install.png](./img/install.png)

10. 等待系统安装，时间会比较长，甚至会卡死

  ![install2.png](./img/install2.png)

  然后就可以了。