CREATE TABLE `hello` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `number` int(10) unsigned DEFAULT NULL COMMENT '数量',
  `price` decimal(10,4) unsigned DEFAULT NULL COMMENT '价格',
  `birth_day` timestamp NULL DEFAULT NULL COMMENT '上新日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_key` (`name`,`birth_day`) USING BTREE,
  KEY `idx_end_time` (`price`) USING BTREE,
  KEY `idx_create_time` (`create_time`),
  KEY `idx_update_time` (`update_time`)
)