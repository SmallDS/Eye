-- 东方镜通配镜单轻量化：订单明细增加项目类型
-- 可重复执行；只补充字段和索引，不删除历史数据。

SET @schema_name = DATABASE();

SET @sql = IF(
  (SELECT COUNT(1) FROM information_schema.COLUMNS
   WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'opt_order_item' AND COLUMN_NAME = 'item_type') = 0,
  'ALTER TABLE `opt_order_item` ADD COLUMN `item_type` varchar(32) DEFAULT NULL COMMENT ''项目类型 frame镜架 lens镜片 other其他'' AFTER `order_id`',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @sql = IF(
  (SELECT COUNT(1) FROM information_schema.STATISTICS
   WHERE TABLE_SCHEMA = @schema_name AND TABLE_NAME = 'opt_order_item' AND INDEX_NAME = 'idx_opt_order_item_type_name') = 0,
  'CREATE INDEX `idx_opt_order_item_type_name` ON `opt_order_item` (`item_type`, `product_name`)',
  'SELECT 1'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;