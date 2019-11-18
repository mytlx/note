## spring security自定义登录界面报错：logout isn't a valid redirect URL

```xml
<security:form-login login-page="/login.html"
                     login-processing-url="/login"
                     username-parameter="username" password-parameter="password"
                     authentication-failure-url="/failer.html"
                     default-target-url="/success.html"
                     authentication-success-forward-url="/success.html"
/>
```

配置的地址都需要加斜线`/`





