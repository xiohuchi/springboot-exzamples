Spring Security的Oauth2

## 密码模式

#### 1 登录

```
http://localhost:3012/oauth/token?grant_type=password&scope=hro&client_secret=123456&username=user_1&password=123456&client_id=client3
```


{

​    "access_token": "5d061edc-7caf-4f58-b445-b8991b8b4481",

​    "token_type": "bearer",

​    "refresh_token": "9034b94c-c8d0-49bb-b35a-9af3e2405035",

​    "expires_in": 43060,

​    "scope": "hro"

}



#### 2 刷新token请求

```
http://localhost:3012/oauth/token?grant_type=refresh_token&refresh_token=9034b94c-c8d0-49bb-b35a-9af3e2405035&client_id=client3&client_secret=123456
```

{

​    "access_token": "4d20e60f-dea5-4a4a-ba2c-85a0aba90003",

​    "token_type": "bearer",

​    "refresh_token": "5abd0b43-9415-46af-93b2-121838bb5281",

​    "expires_in": 43199,

​    "scope": "hro"

}

#### 3 用户信息

```
http://localhost:3012/user/info
请求头
Authorization:4d20e60f-dea5-4a4a-ba2c-85a0aba90003
```

#### 4 校验token

```
http://localhost:3012/oauth/check_token?token=4d20e60f-dea5-4a4a-ba2c-85a0aba90003
```

{

​    "aud": [

​        "order"

​    ],

​    "exp": 1571322505,

​    "user_name": "user_1",

​    "client_id": "client3",

​    "scope": [

​        "hro"

​    ]

}

#### 5 退出登录

```
http://localhost:3012/token/logout
```

## 授权码模式

#### 1 code码

```
http://localhost:3012/oauth/authorize?client_id=client3&response_type=code&redirect_uri=https://ssl.jxufe.edu.cn/cas/login
请求头
Authorization:7171a69e-243a-4acb-96d5-18a1c1a735b70
```

#### 2 security登录

```
http://localhost:3012/token/form?username=user1&password=123456
```

#### 3 code换token

```
http://localhost:3012/oauth/token?grant_type=authorization_code&code=JXVoNB&client_id=client3&client_secret=123456&redirect_uri=http://www.baidu.com
```

## 客户端模式

#### 1 获取token

```
http://localhost:3012/oauth/token?grant_type=client_credentials&scope=app&client_secret=123456&client_id=client1
```

{

​    "access_token": "682e6728-29ed-4c14-b418-f3ec84cf2f80",

​    "token_type": "bearer",

​    "expires_in": 41583,

​    "scope": "app"

}

## 简化模式

该模式直接在浏览器中向认证服务器申请令牌，无需经过client端的服务器，跳过了"授权码"这个步骤，所有步骤在浏览器中完成，直接在回调url中传递令牌。适合直接在前端应用获取token的应用。

#### 调用

```
http://localhost:3012/oauth/authorize?response_type=token&client_secret=123456&client_id=client3&redirect_uri=https://www.baidu.com
请求头
Authorization:7171a69e-243a-4acb-96d5-18a1c1a735b70
```

#### 跳转

```
https://www.baidu.com/#access_token=7171a69e-243a-4acb-96d5-18a1c1a735b7&token_type=bearer&expires_in=42071&scope=hro
```

