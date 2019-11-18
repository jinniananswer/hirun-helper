# remember-me
> 勾选 "记住我" 后，一次登录，后面在有效期内免登录。

> 表结构
```sql
create table persistent_logins (
  username varchar(64) not null default '', 
  series varchar(64) primary key, 
  token varchar(64) not null , 
  last_used timestamp not null
);
```

> WebSecurityConfig.java

```java
.rememberMe()
	// 两周之内登陆过不用重新登陆
	.tokenValiditySeconds(60 * 60 * 24 * 14)
	.tokenRepository(persistentTokenRepository())
```