

# VMware安装mac OS

1. ### unlocker208

   ![1538836010283](G:\TLX\Documents\MarkDown\blog\unlocker.png)

   管理员运行**win-install.cmd**文件，之后新建虚拟机时便会出现mac OS选项

2. 按照正常步骤创建虚拟机后，运行虚拟机会出现错误提示。

   在该虚拟机目录下寻找***.vmx**文件，记事本打开，找到`smc.present = "TRUE"`，在此行下面添加`smc.version = "0"`，如下图

   ![1538836503643](G:\TLX\Documents\MarkDown\blog\vmx.png)

3. 重新启动虚拟机，如果出现下图蓝屏**boot manager**的现象，则是镜像包的问题，**CRD镜像文件**可以成功安装。

   ![1538836789787](G:\TLX\Documents\MarkDown\blog\boot.png)

4. 成功启动后显示如下画面，等待安装

   ![1538837965415](G:\TLX\Documents\MarkDown\blog\apple.png)

   进度条完成后选择语言

   ![1538838202480](G:\TLX\Documents\MarkDown\blog\language.png)

   选择 实用工具 - 磁盘工具 进行分区

   ![1538838345443](G:\TLX\Documents\MarkDown\blog\disk.png)

   选择磁盘，点击抹掉，设置名称，点击抹掉

   ![1538839229331](G:\TLX\Documents\MarkDown\blog\disk_operate.png)

   分区成功

   ![1538839289472](G:\TLX\Documents\MarkDown\blog\disk_success.png)

   退出磁盘工具，点击继续，同意服务什么的，出现如下画面，将系统装在刚才创建的分区中

   ![1538839494132](G:\TLX\Documents\MarkDown\blog\install.png)

   等待系统安装

   ![1538839524076](G:\TLX\Documents\MarkDown\blog\install2.png)

   然后我就卡死了。