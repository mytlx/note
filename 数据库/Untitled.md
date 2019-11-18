* 查看数据库表中外键信息，外键名称等

```mysql
mysql> select * from information_schema.key_column_usage
    -> where table_name = 'depositor' and table_schema = 'bank';
```

* 删除外键

```mysql
mysql> alter table table_name drop foreign key foreign_key_name;
```

* 查看建表语句

```mysql
mysql> show create table table_name;
```

* 添加外键，并设置级联删除

```mysql
mysql> alter table 表名 add constraint foreign key 外键标识 (外键列名) 
    -> references 另一表名 (另一表中外键列名) on delete cascade;
```

