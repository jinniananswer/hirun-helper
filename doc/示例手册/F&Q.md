# RBAC 说明
> Role-Based Access Control
> 一种基于角色的权限访问控制。原则上所有的功能权限以及菜单权限都是基于角色来授权，避免直接给予用户授权。

#### 登录步骤说明:
* 根据 username 查 ins_user 表，获取用户的基本信息。
* 根据 user_id 查 ins_user_role 表, 获取用户有哪些角色。
* 根据 role_id 查 ins_func_role 表, 获取用户有哪些功能权限。
* 根据 role_id 查 ins_menu_role 表，获取用户有哪些菜单权限。

#### 获取登录上下文:
```java
UserContext userContext = WebContextUtils.getUserContext();
userContext.getMobileNo(); // 手机号码
userContext.getUsername(); // 用户名
userContext.getUserId();   // 用户ID
userContext.getRoles();    // 用户角色
Collection<GrantedAuthority> grantedAuthorities = userContext.getGrantedAuthorities();
grantedAuthorities.forEach(System.out::println); // 获取功能权限
```

#### 权限核心表说明
* ins_user             用户表
* ins_user_role    用户角色关联
* ins_role              角色表
* ins_func_role    角色权限关联
* sys_func            权限表
* sys_menu         菜单表        
* ins_menu_role 角色菜单关联
```sql
                             ins_user
                                |
                          ins_user_role
                                |
  sys_func - ins_func_role - ins_role - ins_menu_role - sys_menu
```

#### 菜单授权:
* 只需对叶子菜单进去授权，系统会将叶子菜单的所有上级目录展示出来。
* 菜单授权基于角色表来进行。



# 使用 .gitignore 的问题
> 用于定义不加入 Git 版本控制的文件和目录清单。
> 注：只能忽略那些原来没有被 track 的文件。如果某些文件已经被纳入了版本管理中，则修改 .gitignore 是无效的。 处理方法如下:
```shell
E:\idea-workspace\hi-tech\hirun-helper>git rm --cached .gradle -r
rm '.gradle/4.10.3/fileChanges/last-build.bin'
rm '.gradle/4.10.3/fileContent/fileContent.lock'
rm '.gradle/4.10.3/fileHashes/fileHashes.bin'
rm '.gradle/4.10.3/fileHashes/fileHashes.lock'
rm '.gradle/4.10.3/fileHashes/resourceHashesCache.bin'
rm '.gradle/4.10.3/gc.properties'
rm '.gradle/4.10.3/javaCompile/classAnalysis.bin'
rm '.gradle/4.10.3/javaCompile/jarAnalysis.bin'
rm '.gradle/4.10.3/javaCompile/javaCompile.lock'
rm '.gradle/4.10.3/javaCompile/taskHistory.bin'
rm '.gradle/4.10.3/taskHistory/taskHistory.bin'
rm '.gradle/4.10.3/taskHistory/taskHistory.lock'
rm '.gradle/buildOutputCleanup/buildOutputCleanup.lock'
rm '.gradle/buildOutputCleanup/cache.properties'
rm '.gradle/buildOutputCleanup/outputFiles.bin'
rm '.gradle/vcs-1/gc.properties'
```

# 使用 idea 的 gradle 插件运行 build 的问题
> build 会连带执行 Task: test，如果想要跳过 Task: test，可以定义 -x test
> 并通过 Run 'hirun-help [build]' with Coverage 来执行
```shell
Run/Debug Configurations:
-> Gradle:
-> -> hirun-helper [build]
-> -> -> Arguments: -x test
```