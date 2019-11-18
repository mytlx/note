**RegOpenKeyEx()**

功能： 打开一个指定的注册表键


```c++
LONG RegOpenKeyEx(
    HKEY hKey, // 需要打开的主键的名称
    LPCTSTR lpSubKey, //需要打开的子键的名称
    DWORD ulOptions, // 保留，设为0
    REGSAM samDesired, // 安全访问标记，也就是权限
    PHKEY phkResult // 得到的将要打开键的句柄
) 
```



**CheckMenuItem()**

功能：在弹出菜单中为菜单项增加选中标记或移除选中标记和创建一个水平分隔线等

函数原型：`DWORD CheckMenuItem(HMENU *hmenu*, UINT *uIDCheckItem*, UINT *uCheck*);`

参数：

* **hmenu**：含有其菜单项的标志将被提取得的菜单的句柄。

* **uId**：其某单标志将被取得的菜单项，此参数含义由参数uFlags决定。

* **UFlags**：用于指定参数uld的含义的值。此参数可取下列值之一：

* **MF_BYCOMMAND**：表示参数uId给出菜单项的标识符。如果MF_BYCOMMAND和MF_BYPOSITION都没被指定，则MF_BYCOMMAND是缺省值。

* **MF_BYPOSITION**：表示参数uId给出菜单项相对于零的位置。

返回值：

* 如果指定的项不存在，返回值是OXFFFFFFFF；
* 如果菜单项打开了一个子菜单，则返回值的低位含有与菜单相联系的菜单标志，高位含有子菜单的项数。
* 否则，返回值是莱单标志的掩码（布尔OR）。

菜单标志：

* **MF_CHECKED**：放置选取标记于菜单项旁边（只用于下拉式菜单、子菜单或快捷菜单）。

* **MF_DISABLED**：使菜单项无效。MF_GRAYED：使菜单项无效并变灰。MF_HILITE：加亮菜单项。

* **MF_MENUBARBREAK**：对下拉式菜单、子菜单和快捷菜单，新列和旧列由垂直线隔开，其余功能同MF_MENUBREAK标志。

* **MF_MENUBREAK**：将菜单项放于新行（对菜单条）或无分隔列地放于新列（对下拉式菜单、子菜单或快捷菜单）。

* **MF_SEPARATOR**：创建一个水平分隔线（只用于下拉式菜单、子菜单或快捷菜单）。

* **MF_UNCHECKED**: 相当于MF_CHECKED 的反作用，取消放置于菜单项旁边的标记。