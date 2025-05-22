# 医院管理系统 (Hospital Management System)

一个医院管理解决方案，包含病患管理、医生排班、病床管理、药品管理等功能。该系统采用前后端分离架构，后端使用 Spring Boot，前端使用 Vue 3。

## 项目结构

```
DBWork/
├── HMS.sql                      # 数据库脚本
├── databaseWork_backend/        # 后端项目 (Spring Boot)
└── vue/
    └── HospitalManagementSystem/ # 前端项目 (Vue 3)
```

## 技术栈

### 后端
- **框架**: Spring Boot 3.4.5
- **语言**: Java 17
- **数据库**: Microsoft SQL Server
- **认证**: JWT (JSON Web Token)
- **构建工具**: Maven

### 前端
- **框架**: Vue 3
- **UI 组件库**: Element Plus
- **构建工具**: Vite
- **路由**: Vue Router
- **HTTP 客户端**: Axios

## 功能模块

系统主要包含以下几个模块:

### 管理员模块
- 管理员账户管理
- 医生管理
- 科室管理
- 病床管理
- 病房管理
- 药品管理
- 排班管理

### 医生模块
- 医生个人信息管理
- 查看排班信息
- 门诊管理
- 住院患者管理

### 患者模块
- 患者信息管理
- 挂号管理
- 查看就诊记录
- 在线支付

## 开始使用

### 数据库配置

1. 使用 SQL Server 创建数据库
2. 执行 `HMS.sql` 脚本创建表和初始数据

```sql
-- 创建数据库
CREATE DATABASE HospitalManagementSystem;
GO
USE HospitalManagementSystem;
GO
-- 继续执行脚本中的其余部分
```

### 后端配置与启动

1. 进入后端项目目录
```sh
cd databaseWork_backend
```

2. 使用 Maven 安装依赖并运行
```sh
./mvnw clean install
./mvnw spring-boot:run
```
或在 Windows 上:
```sh
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

默认情况下，后端服务会在 `http://localhost:8080` 运行

### 前端配置与启动

1. 安装 Node.js（推荐使用 nvm 管理 Node.js 版本）
   - 下载地址: https://github.com/coreybutler/nvm-windows/releases

2. 进入前端项目目录
```sh
cd vue/HospitalManagementSystem
```

3. 安装依赖
```sh
npm install
```

4. 启动开发服务器
```sh
npm run dev
```

5. 构建生产版本
```sh
npm run build
```

## 默认账户

系统初始化后包含以下默认账户:

- **管理员账户**:
  - 用户名: admin
  - 密码: admin123
  - 医生账户：1
  - 医生密码：doc123
  - 病人账户：yes
  - 病人密码: 123123
