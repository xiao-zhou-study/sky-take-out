# 苍穹外卖

## 项目介绍

苍穹外卖是一个基于Spring Boot的餐饮点餐系统，采用前后端分离的开发模式，包含用户端和管理端两个子系统。

## 技术选型

### 后端技术

- Spring Boot 2.7.3
- MySQL
- MyBatis
- Spring Cache
- WebSocket
- JWT
- Lombok
- Knife4j
- 阿里云OSS
- 微信支付

## 项目结构

```
sky-take-out
├── sky-common -- 公共模块，存放工具类、常量类等
├── sky-pojo -- 实体模块，存放实体类、VO、DTO等
└── sky-server -- 服务端模块，存放配置类、拦截器、Controller、Service等
```

## 开发环境

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0
- Redis 6.0+

## 快速开始

1. 克隆项目到本地

```bash
git clone [项目地址]
```

2. 创建数据库并导入SQL文件

```sql
create
database sky_take_out;
```

3. 修改数据库配置
   修改 `sky-server/src/main/resources/application-dev.yml` 中的数据库连接信息

4. 启动项目
   运行 `sky-server` 模块下的启动类

## 功能特性

- 管理端
    - 员工管理
    - 分类管理
    - 菜品管理
    - 套餐管理
    - 订单管理
    - 数据统计

- 用户端
    - 微信登录
    - 商品浏览
    - 购物车
    - 下单支付
    - 订单查询

## 项目亮点

- 基于Spring Boot框架，代码简洁、易扩展
- 采用前后端分离架构，接口规范
- 集成微信支付，支持在线支付
- 使用WebSocket实现订单实时通知
- 集成阿里云OSS，实现图片存储

## 联系我们

如有任何问题，请联系项目维护团队。