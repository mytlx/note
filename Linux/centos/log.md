### centos重启后无法联网

无法启动网络服务，查看网络状态如下：

```bash
systemctl status network.service
```

![40_网络服务失败状态.png](./img/40_网络服务失败状态.png)

 解决方式：

#### 1. 禁用NetworkManager 

```bash
systemctl stop NetworkManager
systemctl disable NetworkManager
```

![41_禁用网络管理器.png](./img/41_禁用网络管理器.png)

#### 2. 重启网络服务

```bash
systemctl start network.service
```

#### 3. 查看网络状态

```bash
systemctl status network.service
```

![42_重启网络服务.png](./img/42_重启网络服务.png)



### centos挂载共享文件夹

#### 1. 在vmware中正常添加共享文件夹

#### 2. 查看共享目录是否设置是成功

```bash
vmware-hgfsclient
```

![43_查看共享目录是否设置成功.png](./img/43_查看共享目录是否设置成功.png)

#### 3. 手动挂载共享目录

```bash
vmhgfs-fuse .host:/shareFiles /mnt/hgfs
```

![44_手动挂载共享目录.png](./img/44_手动挂载共享目录.png)

至此，挂载成功，但是重启后会失效。

#### 4. 自动挂载共享目录

```bash
vim /etc/fstab

# 文件中添加下面一行信息
.host:/VMShare /mnt/hgfs fuse.vmhgfs-fuse allow_other,defaults 0 0
```

![46_文件中添加挂载信息.png](./img/46_文件中添加挂载信息.png)

让刚刚写入的挂载信息立即生效

```bash
mount -a
```

![47_挂载信息生效.png](./img/47_挂载信息生效.png)



#### 5. 卸载挂载目录	

```bash
umount /mnt/hgfs
```

![45_卸载挂载目录.png](./img/45_卸载挂载目录.png)