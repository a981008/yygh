# 项目简介

本项目来自尚硅谷的[尚医通](https://www.bilibili.com/video/BV1V5411K7rT)

项目代码与课上代码有些不同

# 项目结构

以下只展示当前已完成的模块

```
├── common                 # 公共模块
│   │── common-util        # 工具类模块，所有模块都可以依赖于它
│   └── service-util       # 服务的工具包，包含 service 服务的公共配置类，所有 service 模块依赖于它
├── model                  # 实体类模块
├── service                # 服务 api 接口父节点
│   │── service-hosp       # 医院 api 接口服务
│   └── service-user       # 用户 api 接口服务
└── 资源                    # 存放 SQL 等资源
```

