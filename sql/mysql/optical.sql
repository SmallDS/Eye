-- 东方镜通眼镜店管理系统 - 首期业务初始化

CREATE TABLE IF NOT EXISTS `opt_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(64) NOT NULL COMMENT '姓名',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint DEFAULT NULL COMMENT '性别',
  `age` int DEFAULT NULL COMMENT '年龄',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `latest_optometry_time` datetime DEFAULT NULL COMMENT '最近验光时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_opt_customer_name` (`name`),
  KEY `idx_opt_customer_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='东方镜通客户';

CREATE TABLE IF NOT EXISTS `opt_optometry_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `customer_id` bigint NOT NULL COMMENT '客户编号',
  `optometry_time` datetime NOT NULL COMMENT '验光时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `distance_right_sph` decimal(8,2) DEFAULT NULL COMMENT '远用右眼球光',
  `distance_right_cyl` decimal(8,2) DEFAULT NULL COMMENT '远用右眼散光',
  `distance_right_axis` decimal(8,2) DEFAULT NULL COMMENT '远用右眼轴线',
  `distance_right_prism` decimal(8,2) DEFAULT NULL COMMENT '远用右眼三棱',
  `distance_right_base` varchar(32) DEFAULT NULL COMMENT '远用右眼基底',
  `distance_right_addition` decimal(8,2) DEFAULT NULL COMMENT '远用右眼加光',
  `distance_right_base_curve_v` decimal(8,2) DEFAULT NULL COMMENT '远用右眼基弧V',
  `distance_right_base_curve_h` decimal(8,2) DEFAULT NULL COMMENT '远用右眼基弧H',
  `distance_right_diameter` decimal(8,2) DEFAULT NULL COMMENT '远用右眼直径',
  `distance_right_naked_vision` decimal(8,2) DEFAULT NULL COMMENT '远用右眼裸眼视力',
  `distance_right_corrected_vision` decimal(8,2) DEFAULT NULL COMMENT '远用右眼矫正视力',
  `distance_left_sph` decimal(8,2) DEFAULT NULL COMMENT '远用左眼球光',
  `distance_left_cyl` decimal(8,2) DEFAULT NULL COMMENT '远用左眼散光',
  `distance_left_axis` decimal(8,2) DEFAULT NULL COMMENT '远用左眼轴线',
  `distance_left_prism` decimal(8,2) DEFAULT NULL COMMENT '远用左眼三棱',
  `distance_left_base` varchar(32) DEFAULT NULL COMMENT '远用左眼基底',
  `distance_left_addition` decimal(8,2) DEFAULT NULL COMMENT '远用左眼加光',
  `distance_left_base_curve_v` decimal(8,2) DEFAULT NULL COMMENT '远用左眼基弧V',
  `distance_left_base_curve_h` decimal(8,2) DEFAULT NULL COMMENT '远用左眼基弧H',
  `distance_left_diameter` decimal(8,2) DEFAULT NULL COMMENT '远用左眼直径',
  `distance_left_naked_vision` decimal(8,2) DEFAULT NULL COMMENT '远用左眼裸眼视力',
  `distance_left_corrected_vision` decimal(8,2) DEFAULT NULL COMMENT '远用左眼矫正视力',
  `near_right_sph` decimal(8,2) DEFAULT NULL COMMENT '近用右眼球光',
  `near_right_cyl` decimal(8,2) DEFAULT NULL COMMENT '近用右眼散光',
  `near_right_axis` decimal(8,2) DEFAULT NULL COMMENT '近用右眼轴线',
  `near_right_prism` decimal(8,2) DEFAULT NULL COMMENT '近用右眼三棱',
  `near_right_base` varchar(32) DEFAULT NULL COMMENT '近用右眼基底',
  `near_right_addition` decimal(8,2) DEFAULT NULL COMMENT '近用右眼加光',
  `near_right_base_curve_v` decimal(8,2) DEFAULT NULL COMMENT '近用右眼基弧V',
  `near_right_base_curve_h` decimal(8,2) DEFAULT NULL COMMENT '近用右眼基弧H',
  `near_right_diameter` decimal(8,2) DEFAULT NULL COMMENT '近用右眼直径',
  `near_right_naked_vision` decimal(8,2) DEFAULT NULL COMMENT '近用右眼裸眼视力',
  `near_right_corrected_vision` decimal(8,2) DEFAULT NULL COMMENT '近用右眼矫正视力',
  `near_left_sph` decimal(8,2) DEFAULT NULL COMMENT '近用左眼球光',
  `near_left_cyl` decimal(8,2) DEFAULT NULL COMMENT '近用左眼散光',
  `near_left_axis` decimal(8,2) DEFAULT NULL COMMENT '近用左眼轴线',
  `near_left_prism` decimal(8,2) DEFAULT NULL COMMENT '近用左眼三棱',
  `near_left_base` varchar(32) DEFAULT NULL COMMENT '近用左眼基底',
  `near_left_addition` decimal(8,2) DEFAULT NULL COMMENT '近用左眼加光',
  `near_left_base_curve_v` decimal(8,2) DEFAULT NULL COMMENT '近用左眼基弧V',
  `near_left_base_curve_h` decimal(8,2) DEFAULT NULL COMMENT '近用左眼基弧H',
  `near_left_diameter` decimal(8,2) DEFAULT NULL COMMENT '近用左眼直径',
  `near_left_naked_vision` decimal(8,2) DEFAULT NULL COMMENT '近用左眼裸眼视力',
  `near_left_corrected_vision` decimal(8,2) DEFAULT NULL COMMENT '近用左眼矫正视力',
  `distance_pd` decimal(8,2) DEFAULT NULL COMMENT '远瞳距',
  `rpd` decimal(8,2) DEFAULT NULL COMMENT 'RPD',
  `lpd` decimal(8,2) DEFAULT NULL COMMENT 'LPD',
  `near_pd` decimal(8,2) DEFAULT NULL COMMENT '近瞳距',
  `rh` decimal(8,2) DEFAULT NULL COMMENT 'Rh',
  `lh` decimal(8,2) DEFAULT NULL COMMENT 'Lh',
  `left_sph` decimal(8,2) DEFAULT NULL COMMENT '旧字段-左眼球镜',
  `left_cyl` decimal(8,2) DEFAULT NULL COMMENT '旧字段-左眼柱镜',
  `left_axis` decimal(8,2) DEFAULT NULL COMMENT '旧字段-左眼轴位',
  `left_vision` decimal(8,2) DEFAULT NULL COMMENT '旧字段-左眼视力',
  `right_sph` decimal(8,2) DEFAULT NULL COMMENT '旧字段-右眼球镜',
  `right_cyl` decimal(8,2) DEFAULT NULL COMMENT '旧字段-右眼柱镜',
  `right_axis` decimal(8,2) DEFAULT NULL COMMENT '旧字段-右眼轴位',
  `right_vision` decimal(8,2) DEFAULT NULL COMMENT '旧字段-右眼视力',
  `pupil_distance` decimal(8,2) DEFAULT NULL COMMENT '旧字段-瞳距',
  `optometrist_user_id` bigint DEFAULT NULL COMMENT '旧字段-验光师用户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_opt_optometry_customer` (`customer_id`),
  KEY `idx_opt_optometry_time` (`optometry_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='东方镜通验光记录';

CREATE TABLE IF NOT EXISTS `opt_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `customer_id` bigint NOT NULL COMMENT '客户编号',
  `optometry_record_id` bigint DEFAULT NULL COMMENT '验光记录编号',
  `total_amount` decimal(12,2) NOT NULL DEFAULT 0 COMMENT '总金额',
  `discount_amount` decimal(12,2) NOT NULL DEFAULT 0 COMMENT '优惠金额',
  `receivable_amount` decimal(12,2) NOT NULL DEFAULT 0 COMMENT '应收金额',
  `paid_amount` decimal(12,2) NOT NULL DEFAULT 0 COMMENT '已付金额',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `pickup_time` datetime DEFAULT NULL COMMENT '取镜时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_opt_order_no` (`order_no`),
  KEY `idx_opt_order_customer` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='东方镜通配镜订单';

CREATE TABLE IF NOT EXISTS `opt_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint NOT NULL COMMENT '订单编号',
  `item_type` varchar(32) DEFAULT NULL COMMENT '项目类型 frame镜架 lens镜片 other其他',
  `product_id` bigint DEFAULT NULL COMMENT '商品编号',
  `product_name` varchar(128) DEFAULT NULL COMMENT '项目名称',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '数量',
  `unit_price` decimal(12,2) NOT NULL DEFAULT 0 COMMENT '单价',
  `total_price` decimal(12,2) NOT NULL DEFAULT 0 COMMENT '小计',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_opt_order_item_order` (`order_id`),
  KEY `idx_opt_order_item_product` (`product_id`),
  KEY `idx_opt_order_item_type_name` (`item_type`, `product_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='东方镜通配镜订单明细';

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(90000, '东方镜通', '', 1, 30, 0, '/opt', 'ep:view', NULL, NULL, 1, b'0', b'1', b'0', 'admin', NOW(), 'admin', NOW(), b'0'),
(90010, '客户管理', 'opt:customer:query', 2, 10, 0, '/opt/customer', 'ep:user', 'opt/customer/index', 'OptCustomer', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90011, '客户档案', 'opt:customer:query', 2, 11, 0, '/opt/customer/detail/:id', 'ep:user', 'opt/customer/detail', 'OptCustomerDetail', 0, b'0', b'0', b'0', 'admin', NOW(), 'admin', NOW(), b'0'),
(90020, '验光管理', 'opt:optometry:query', 2, 20, 90000, 'optometry', 'ep:view', 'opt/optometry/index', 'OptOptometryRecord', 1, b'0', b'1', b'0', 'admin', NOW(), 'admin', NOW(), b'0'),
(90030, '配镜订单', 'opt:order:query', 2, 30, 0, '/opt/order', 'ep:tickets', 'opt/order/index', 'OpticalOrder', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(90101, '客户查询', 'opt:customer:query', 3, 1, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90102, '客户新增', 'opt:customer:create', 3, 2, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90103, '客户修改', 'opt:customer:update', 3, 3, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90104, '客户删除', 'opt:customer:delete', 3, 4, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90105, '客户导出', 'opt:customer:export', 3, 5, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90201, '验光查询', 'opt:optometry:query', 3, 1, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90202, '验光新增', 'opt:optometry:create', 3, 2, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90203, '验光修改', 'opt:optometry:update', 3, 3, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90204, '验光删除', 'opt:optometry:delete', 3, 4, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90205, '验光导出', 'opt:optometry:export', 3, 5, 90010, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90301, '订单查询', 'opt:order:query', 3, 1, 90030, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90302, '订单新增', 'opt:order:create', 3, 2, 90030, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90303, '订单修改', 'opt:order:update', 3, 3, 90030, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90304, '订单删除', 'opt:order:delete', 3, 4, 90030, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90305, '订单导出', 'opt:order:export', 3, 5, 90030, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(90070, '数据导入', 'opt:import:create', 2, 70, 0, '/opt/import', 'ep:upload', 'opt/import/index', 'OptImport', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(90701, '导入模板下载', 'opt:import:template', 3, 1, 90070, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
(90702, '数据导入', 'opt:import:create', 3, 2, 90070, '', '', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT r.`id`, m.`id`, 'admin', NOW(), 'admin', NOW(), b'0', r.`tenant_id`
FROM `system_role` r
JOIN `system_menu` m ON m.`id` BETWEEN 90000 AND 90702 AND m.`id` NOT IN (90020, 90040, 90050, 90060, 90401, 90402, 90403, 90404, 90405, 90501, 90502, 90503, 90504, 90505, 90601, 90602, 90603, 90604, 90605)
WHERE r.`deleted` = b'0'
  AND r.`status` = 0
  AND NOT EXISTS (
    SELECT 1
    FROM `system_role_menu` rm
    WHERE rm.`role_id` = r.`id`
      AND rm.`menu_id` = m.`id`
      AND rm.`deleted` = b'0'
  );

-- ----------------------------
-- Restored tenant tables required by login
-- ----------------------------
-- Table structure for system_tenant
-- ----------------------------
DROP TABLE IF EXISTS `system_tenant`;
CREATE TABLE `system_tenant`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '租户编号',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户名',
  `contact_user_id` bigint NULL DEFAULT NULL COMMENT '联系人的用户编号',
  `contact_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '联系人',
  `contact_mobile` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系手机',
  `websites` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '绑定域名数组',
  `package_id` bigint NOT NULL COMMENT '租户套餐编号',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `account_count` int NOT NULL COMMENT '账号数量',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 162 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户表';

-- ----------------------------
-- Records of system_tenant
-- ----------------------------
BEGIN;
INSERT INTO `system_tenant` (`id`, `name`, `contact_user_id`, `contact_name`, `contact_mobile`, `status`, `websites`, `package_id`, `expire_time`, `account_count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1, '芋道源码', NULL, '芋艿', '17321315478', 0, 'www.iocoder.cn,127.0.0.1:3000,wxc4598c446f8a9cb3', 0, '2099-02-19 17:14:16', 9999, '1', '2021-01-05 17:03:47', '1', '2025-08-19 05:18:41', b'0');
INSERT INTO `system_tenant` (`id`, `name`, `contact_user_id`, `contact_name`, `contact_mobile`, `status`, `websites`, `package_id`, `expire_time`, `account_count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (121, '小租户', 110, '小王2', '15601691300', 0, 'zsxq.iocoder.cn,123321', 111, '2026-07-10 00:00:00', 30, '1', '2022-02-22 00:56:14', '1', '2025-08-19 21:19:29', b'0');
INSERT INTO `system_tenant` (`id`, `name`, `contact_user_id`, `contact_name`, `contact_mobile`, `status`, `websites`, `package_id`, `expire_time`, `account_count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (122, '测试租户', 113, '芋道', '15601691300', 0, 'test.iocoder.cn,222,333', 111, '2023-04-29 00:00:00', 50, '1', '2022-03-07 21:37:58', '1', '2026-06-01 23:29:35', b'0');
COMMIT;

-- ----------------------------
-- Table structure for system_tenant_package
-- ----------------------------
DROP TABLE IF EXISTS `system_tenant_package`;
CREATE TABLE `system_tenant_package`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '套餐编号',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '备注',
  `menu_ids` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '关联的菜单编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户套餐表';

-- ----------------------------
-- Records of system_tenant_package
-- ----------------------------
BEGIN;
INSERT INTO `system_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (111, '普通套餐', 0, '小功能', '[1,2,5,1031,1032,1033,1034,1035,1036,1037,1038,1039,1050,1051,1052,1053,1054,1056,1057,1058,1059,1060,1063,1064,1065,1066,1067,1070,1075,1077,1078,1082,1083,1084,1085,1086,1087,1088,1089,1090,1091,1092,1117,1118,1119,1120,100,101,102,1126,103,1127,1128,1129,106,1130,107,1132,1133,110,1134,111,1135,112,1136,113,1137,2161,114,1138,1139,115,1140,116,1141,1142,1143,1150,1161,1162,1166,1173,1174,2713,2714,1178,2715,2716,2717,2718,2720,2721,1185,2722,1186,1187,2723,1188,2724,1189,2725,1190,2726,1191,2727,1192,2728,2729,1193,1194,2730,1195,2731,2732,1197,2733,1198,2734,1199,2735,1200,1201,1202,2739,2740,1207,1208,1209,2745,1210,2746,1211,2747,1212,2748,1213,1215,1216,1217,1218,1219,1220,2756,1221,2757,1222,1224,1225,1226,1227,1228,1229,1237,1238,2262,1239,1240,1241,1242,1243,2275,2276,2277,1255,1256,1257,2281,1258,2282,1259,2283,1260,2284,2285,2287,2288,2293,2294,2297,2300,2301,2302,2317,2318,2319,2320,2321,2322,2323,2324,2325,2326,2327,2328,2329,2330,2331,2332,2333,2334,2335,2363,2364,5011,5012,2472,2478,2479,2480,2481,2482,2483,2484,2485,2486,2487,2488,2489,2490,2491,2492,2493,2494,2495,2497,2525,1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1011,1012,1013,2549,1014,2550,1015,2551,1016,2552,1017,2553,1018,2554,1019,2555,1020,2556,2557,2558,2559]', '1', '2022-02-22 00:54:00', '1', '2025-09-06 20:52:25', b'0');
INSERT INTO `system_tenant_package` (`id`, `name`, `status`, `remark`, `menu_ids`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (113, '测试套餐（啥都没有）', 0, '', '[2160,1254,2159]', '1', '2026-06-01 23:22:39', '1', '2026-06-09 14:44:00', b'0');
COMMIT;

-- ----------------------------
