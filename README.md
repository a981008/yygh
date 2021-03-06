# 项目简介

> 该项目太假，不适合面试吹水。无限期拖更。

本项目来自尚硅谷的[尚医通](https://www.bilibili.com/video/BV1V5411K7rT) ，主要是后端代码

项目代码与课上代码有些不同

所需资源：[前端代码](资源/前端代码)、[SQL](资源/SQL)


> 本人不会前端，只保证该项目勉强能跑，奇奇怪怪的问题我也不懂。
 
所用到的技术：
* SpringBoot、SpringCloud（Gateway、Nacos、Sentinel、Task、Feign）
* MyBatis、MyBatis Plus
* MySQL、Redis、MongoDB
* RabbitMQ
* HTTPClient
* Swagger2
* Nginx

# 项目结构

以下只展示当前项目出现的模块

```
yygh-parent
├── common                     # 公共模块
│   │── common-util            # 工具类模块，所有模块都可以依赖于它
│   └── service-util           # 服务的工具包，包含 service 服务的公共配置类，所有 service 模块依赖于它
├── model                      # 实体类模块
├── service                    # 服务 api 接口父节点
│   │── service-cmn            # 公共 api 接口服务
│   │── service-hosp           # 医院 api 接口服务
│   └── service-user           # 用户 api 接口服务
├── service-client             # feign 服务调用父节点
│   └── service-client-cmn     # 公共 api 接口
├── service-gateway            # 服务网关
└── 资源                        # 存放 SQL、前端代码
```

