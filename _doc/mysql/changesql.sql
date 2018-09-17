-- 2018-8-30 21:13:15 所有更新SQL 需要在此记录


ALTER TABLE `org_group` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;
	
ALTER TABLE `org_group_user` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `org_role` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `org_group_rel` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `org_user` 
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `org_user_role` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

	

ALTER TABLE `bus_table` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `bus_permission` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `bus_object` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `bus_column` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;

ALTER TABLE `bus_column_ctrl` 
ADD COLUMN `create_time_` datetime(0) NULL COMMENT '创建时间',
ADD COLUMN `create_by_` varchar(64) NULL COMMENT '创建人' AFTER `create_time_`,
ADD COLUMN `update_time_` datetime(0) NULL COMMENT '更新时间' AFTER `create_by_`,
ADD COLUMN `update_by_` varchar(64) NULL  COMMENT '更新人' AFTER `update_time_`;