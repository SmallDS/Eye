# 东方镜通眼镜店管理系统

东方镜通眼镜店管理系统是基于 Yudao Boot Mini / Vue3 管理端二次开发的轻量化门店业务系统，面向小型眼镜店的客户档案、验光记录、配镜单与历史数据导入场景。

本仓库保留原框架的登录、权限、租户、菜单、字典、日志等基础能力，在业务侧收敛为眼镜店实际需要的流程，弱化或移除库存、商品、支付对接等小店铺暂不需要的复杂模块。

## 当前业务范围

- 客户档案：客户检索、新增、编辑、删除、基础资料维护。
- 验光记录：严格绑定客户档案，在客户档案内查看历史，并使用接近纸质验光单的布局录入数据。
- 配镜单：在验光单编辑页内开单，一张验光单可关联多张配镜单；镜架、镜片、其他项目支持手工录入和历史输入提示。
- 数据导入：支持客户与验光单 Excel 导入，适合旧系统或手工台账迁移。
- 菜单精简：隐藏或删除工作台、库存、商品、付款对接等当前不使用的入口。

## 技术栈

- 后端：JDK 17+、Spring Boot、MyBatis Plus、Maven、MySQL、Redis
- 前端：Vue 3、Vite、TypeScript、Element Plus、pnpm
- 基础能力：沿用 Yudao Boot Mini 的 RBAC、租户、菜单、系统管理能力

## 目录结构

```text
.
├── yudao-server/                         # 后端启动模块
├── yudao-module-optical/                 # 东方镜通业务模块
├── yudao-module-system/                  # 用户、角色、菜单、权限等系统模块
├── yudao-module-infra/                   # 基础设施模块
├── yudao-framework/                      # 框架公共能力
├── sql/mysql/
│   ├── ruoyi-vue-pro.sql                 # 基础系统初始化 SQL
│   ├── optical.sql                       # 东方镜通业务初始化 SQL
│   ├── fix_tenant_login.sql              # 租户登录修复脚本
│   ├── optical_cleanup_unused_modules.sql # 不使用模块清理脚本
│   └── optical_phase_order_sheet.sql     # 配镜单增量脚本
└── yudao-ui/
    └── yudao-ui-admin-vue3/              # 当前维护的 Vue3 管理端
```

仓库内仍保留了部分原始前端目录，当前二开主要维护 `yudao-ui/yudao-ui-admin-vue3`。

## 本地启动

### 1. 数据库

新环境建议先初始化基础系统，再初始化东方镜通业务数据：

```text
sql/mysql/ruoyi-vue-pro.sql
sql/mysql/optical.sql
```

如果是已有数据库升级，按实际环境选择执行增量脚本，例如租户登录修复、无用菜单清理、配镜单脚本。执行前建议先备份数据库。

### 2. 后端

使用 IntelliJ IDEA 打开仓库根目录，选择 JDK 17 或更高版本，启动 `yudao-server`。

本地数据库、Redis、第三方配置请写在本机配置文件中，不要提交到仓库。仓库已忽略 `application-local.yaml`、`application-dev.yaml`、`.env*` 等本地配置文件。

命令行编译示例：

```powershell
$env:JAVA_HOME='C:\Users\Smallds\.jdks\dragonwell-21.0.11'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
& 'C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.4\plugins\maven\lib\maven3\bin\mvn.cmd' -pl yudao-module-optical -am -DskipTests compile
```

### 3. 前端

```powershell
cd C:\Users\Smallds\Desktop\1\yudao-boot-mini-master-jdk17\yudao-ui\yudao-ui-admin-vue3
pnpm install
pnpm dev
```

常用检查：

```powershell
pnpm ts:check
pnpm lint:eslint:check
```

## 业务说明

### 客户档案

客户列表主要负责检索客户和进入档案。客户编辑集中到客户档案页，避免列表页承担过多业务操作。

客户档案是当前系统的主入口，承载客户基础资料、验光记录、配镜单入口和订单历史信息。

### 验光记录

验光记录通过 `customer_id` 与客户档案绑定。新增、编辑、删除验光记录后，会刷新客户最近验光时间。

验光单包含远用右眼、远用左眼、近用右眼、近用左眼四组验光数据，以及远瞳距、近瞳距、RPD、LPD、Rh、Lh 等附加参数。

### 配镜单

配镜单从验光单编辑页创建，自动绑定当前客户和当前验光记录。一张验光单可以有多张配镜单。

配镜内容按实际小店铺录入习惯设计，支持手工填写镜架、镜片、其他项目及金额，并基于历史输入提供提示。

### 数据导入

导入功能面向历史数据迁移，当前支持客户与验光单。导入模板应与数据库字段保持一致，客户与验光单关联优先使用数据库内客户 ID，避免只靠姓名或手机号造成误匹配。

## 开发约定

- 不提交 `.env*`、`application-local.yaml`、`application-dev.yaml`、日志、构建产物、依赖目录。
- 业务初始化以 `sql/mysql/optical.sql` 为主，增量脚本仅用于已有部署环境的补丁处理。
- 小店铺不需要的库存、商品、支付对接类能力，优先隐藏或删除入口，避免增加使用复杂度。
- 修改客户、验光、配镜相关功能时，优先保持客户档案为主入口。
- 前端当前以 `yudao-ui/yudao-ui-admin-vue3` 为准。

## 提交前检查

前端检查：

```powershell
cd C:\Users\Smallds\Desktop\1\yudao-boot-mini-master-jdk17\yudao-ui\yudao-ui-admin-vue3
pnpm ts:check
pnpm lint:eslint:check
```

后端按需执行 Maven 编译，确认 `yudao-module-optical` 可以编译通过。

## 远端仓库

GitHub: https://github.com/SmallDS/Eye