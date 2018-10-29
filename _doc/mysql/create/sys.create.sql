-- -----含qrtz，常用脚本，流水号，子系统，菜单资源，通用授权，面板，系统属性，数据字典等功能表

-- -------------------qrtz 相关-----------------------
-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;



-- ------------------sys 模块功能 持久化表-----------------

-- ----------------------------
-- Table structure for sys_authorization
-- ----------------------------
DROP TABLE IF EXISTS `sys_authorization`;
CREATE TABLE `sys_authorization` (
  `rights_id_` varchar(64) NOT NULL COMMENT 'id',
  `rights_object_` varchar(64) NOT NULL COMMENT '授权对象表分区用',
  `rights_target_` varchar(64) NOT NULL COMMENT '授权目标ID',
  `rights_type_` varchar(64) NOT NULL COMMENT '权限类型',
  `rights_identity_` varchar(64) NOT NULL COMMENT '授权标识',
  `rights_identity_name_` varchar(255) NOT NULL COMMENT '标识名字',
  `rights_permission_code_` varchar(125) NOT NULL COMMENT '授权code=identity+type',
  `rights_create_time_` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `rights_create_by_` varchar(64) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`rights_id_`),
  KEY `idx_permission_code_` (`rights_permission_code_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin COMMENT='通用资源授权配置';

-- ----------------------------
-- Table structure for sys_data_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_dict`;
CREATE TABLE `sys_data_dict` (
  `id_` varchar(64) NOT NULL COMMENT 'id',
  `parent_id_` varchar(64) DEFAULT NULL COMMENT '上级id',
  `key_` varchar(255) NOT NULL COMMENT 'key',
  `name_` varchar(255) NOT NULL COMMENT 'name',
  `dict_key_` varchar(255) NOT NULL COMMENT '字典key',
  `type_id_` varchar(64) DEFAULT NULL COMMENT '分组id',
  `sn_` int(10) DEFAULT NULL COMMENT '排序',
  `dict_type_` varchar(10) NOT NULL COMMENT 'dict/node字典项',
  `delete_flag_` varchar(1) DEFAULT NULL COMMENT '是否删除',
  `create_time_` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin COMMENT='数据字典';

-- ----------------------------
-- Table structure for sys_data_source
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_source`;
CREATE TABLE `sys_data_source` (
  `id_` varchar(64) NOT NULL COMMENT '主键',
  `key_` varchar(64) DEFAULT NULL COMMENT '别名',
  `name_` varchar(64) DEFAULT NULL COMMENT '数据源名称',
  `desc_` varchar(256) DEFAULT NULL COMMENT '数据源的描述',
  `db_type_` varchar(64) DEFAULT NULL COMMENT '数据库类型',
  `class_path_` varchar(100) DEFAULT NULL COMMENT '数据源全路径',
  `attributes_json_` text COMMENT '属性配置',
  PRIMARY KEY (`id_`),
  UNIQUE KEY `key_unique` (`key_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin COMMENT='数据源';

-- ----------------------------
-- Table structure for sys_data_source_def
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_source_def`;
CREATE TABLE `sys_data_source_def` (
  `id_` varchar(64) NOT NULL COMMENT '主键',
  `name_` varchar(64) DEFAULT NULL COMMENT '数据源名称',
  `class_path_` varchar(100) DEFAULT NULL COMMENT '数据源全路径',
  `attributes_json_` text COMMENT '属性配置',
  PRIMARY KEY (`id_`),
  KEY `class_path_unique` (`class_path_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin COMMENT='数据源模板';
 
-- ----------------------------
-- Table structure for sys_log_err
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_err`;
CREATE TABLE `sys_log_err` (
  `id_` varchar(50) NOT NULL COMMENT '主键',
  `ACCOUNT_` varchar(20) DEFAULT NULL COMMENT '帐号',
  `IP_` varchar(20) DEFAULT NULL COMMENT 'IP来源',
  `URL_` varchar(1500) DEFAULT NULL COMMENT '错误URL',
  `CONTENT_` text COMMENT '出错信息',
  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '出错时间',
  `stack_trace_` longtext COMMENT '出错异常堆栈',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for sys_properties
-- ----------------------------
DROP TABLE IF EXISTS `sys_properties`;
CREATE TABLE `sys_properties` (
  `id_` varchar(64) NOT NULL,
  `name_` varchar(64) DEFAULT NULL,
  `alias_` varchar(64) DEFAULT NULL,
  `group_` varchar(64) DEFAULT NULL,
  `value_` varchar(500) DEFAULT NULL,
  `encrypt_` int(11) DEFAULT NULL,
  `update_by_` varchar(64) DEFAULT NULL,
  `update_time_` datetime DEFAULT NULL,
  `create_by_` varchar(64) DEFAULT NULL,
  `create_time_` datetime DEFAULT NULL,
  `description_` varchar(500) DEFAULT NULL,
  `environment_` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin;

-- ----------------------------
-- Table structure for sys_rel_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_rel_resources`;
CREATE TABLE `sys_rel_resources` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `RES_ID_` varchar(50) DEFAULT NULL COMMENT '资源ID',
  `NAME_` varchar(50) DEFAULT NULL COMMENT '名称',
  `RES_URL_` varchar(100) DEFAULT NULL COMMENT '资源地址',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='关联资源';

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `SYSTEM_ID_` varchar(50) DEFAULT NULL COMMENT '子系统ID',
  `ALIAS_` varchar(50) DEFAULT NULL COMMENT '子系统中独一无二',
  `NAME_` varchar(50) DEFAULT NULL COMMENT '资源名',
  `default_url_` varchar(50) DEFAULT NULL COMMENT '默认地址',
  `ENABLE_MENU_` int(11) DEFAULT NULL COMMENT '显示到菜单(1,显示,0 ,不显示)',
  `HAS_CHILDREN_` int(11) DEFAULT NULL COMMENT '是否有子节点',
  `OPENED_` int(11) DEFAULT NULL,
  `ICON_` varchar(50) DEFAULT NULL COMMENT '图标',
  `NEW_WINDOW_` int(11) DEFAULT NULL COMMENT '打开新窗口',
  `SN_` bigint(20) DEFAULT NULL COMMENT '排序',
  `PARENT_ID_` varchar(50) DEFAULT NULL COMMENT '父节点ID',
  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='子系统资源';

-- ----------------------------
-- Table structure for sys_res_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_res_role`;
CREATE TABLE `sys_res_role` (
  `ID_` varchar(50) NOT NULL DEFAULT '' COMMENT '主键',
  `SYSTEM_ID_` varchar(50) DEFAULT NULL COMMENT '系统ID',
  `RES_ID_` varchar(50) DEFAULT NULL COMMENT '资源ID',
  `ROLE_ID_` varchar(50) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='角色资源分配';

-- ----------------------------
-- Table structure for sys_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_schedule_job`;
CREATE TABLE `sys_schedule_job` (
  `id_` varchar(64) NOT NULL COMMENT '主键编号',
  `name_` varchar(50) NOT NULL COMMENT '任务计划名称',
  `group_` varchar(100) NOT NULL COMMENT '任务计划分组',
  `description_` varchar(1000) DEFAULT NULL COMMENT '任务计划说明',
  `invoke_target_` varchar(500) NOT NULL COMMENT '调用目标',
  `cron_expression_` varchar(50) NOT NULL COMMENT '运行表达式',
  `running_state_` varchar(10) NOT NULL COMMENT '运行状态',
  `is_concurrent_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否并发执行',
  `create_by_` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by_` varchar(50) DEFAULT NULL COMMENT '修改用户',
  `update_time_` datetime DEFAULT NULL COMMENT '修改时间',
  `delete_flag_` tinyint(1) DEFAULT '0' COMMENT '有效记录 0 正常 1 已删除',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='系统任务计划';

-- ----------------------------
-- Table structure for sys_schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_schedule_job_log`;
CREATE TABLE `sys_schedule_job_log` (
  `id` varchar(64) NOT NULL COMMENT '编号',
  `job_id` varchar(64) NOT NULL COMMENT '任务计划ID',
  `run_state` varchar(50) NOT NULL COMMENT '执行状态',
  `run_ms` int(21) NOT NULL COMMENT '运行毫秒',
  `content` text COMMENT '运行内容',
  `start_time` datetime NOT NULL COMMENT '运行启动时间',
  `end_time` datetime NOT NULL COMMENT '运行结束时间',
  `create_by_` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by_` varchar(50) DEFAULT NULL COMMENT '修改用户',
  `update_time_` datetime DEFAULT NULL COMMENT '修改时间',
  `delete_flag_` tinyint(1) DEFAULT '0' COMMENT '有效记录 0 正常 1 已删除',
  PRIMARY KEY (`id`),
  KEY `job_id` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='任务计划日志';

-- ----------------------------
-- Table structure for sys_script
-- ----------------------------
DROP TABLE IF EXISTS `sys_script`;
CREATE TABLE `sys_script` (
  `id_` varchar(64) NOT NULL COMMENT '主键',
  `name_` varchar(128) DEFAULT NULL COMMENT '脚本名称',
  `script_` text COMMENT '脚本',
  `category_` varchar(128) DEFAULT NULL COMMENT '脚本分类',
  `memo_` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='常用脚本';

-- ----------------------------
-- Table structure for sys_serialno
-- ----------------------------
DROP TABLE IF EXISTS `sys_serialno`;
CREATE TABLE `sys_serialno` (
  `id_` varchar(64) NOT NULL COMMENT '主键',
  `name_` varchar(64) DEFAULT NULL COMMENT '名称',
  `alias_` varchar(20) DEFAULT NULL COMMENT '别名',
  `regulation_` varchar(128) DEFAULT NULL COMMENT '规则',
  `gen_type_` smallint(6) DEFAULT NULL COMMENT '生成类型',
  `no_length_` int(11) DEFAULT NULL COMMENT '流水号长度',
  `cur_date_` varchar(20) DEFAULT NULL COMMENT '当前日期',
  `init_value_` int(11) DEFAULT NULL COMMENT '初始值',
  `cur_value_` int(11) DEFAULT NULL COMMENT '当前值',
  `step_` smallint(6) DEFAULT NULL COMMENT '步长',
  PRIMARY KEY (`id_`),
  KEY `idx_uni_alias_val` (`alias_`,`cur_value_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='流水号生成';

-- ----------------------------
-- Table structure for sys_subsystem
-- ----------------------------
DROP TABLE IF EXISTS `sys_subsystem`;
CREATE TABLE `sys_subsystem` (
  `ID_` varchar(50) NOT NULL COMMENT '主键',
  `name_` varchar(50)   DEFAULT NULL COMMENT '系统名称',
  `alias_` varchar(50) DEFAULT NULL COMMENT '系统别名',
  `logo_` varchar(50) DEFAULT NULL COMMENT 'logo地址',
  `enabled_` int(11) DEFAULT NULL COMMENT '是否可用 1 可用，0 ，不可用',
  `home_url_` varchar(100) DEFAULT NULL COMMENT '主页地址',
  `base_url_` varchar(50) DEFAULT NULL COMMENT '基础地址',
  `tenant_` varchar(50) DEFAULT NULL COMMENT '租户名称',
  `MEMO_` varchar(200) DEFAULT NULL COMMENT '备注',
  `creator_Id_` varchar(50) DEFAULT NULL COMMENT '创建人ID',
  `creator_` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `is_default_` int(11) DEFAULT NULL COMMENT '是否默认 1 可用，0 ，不可用',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='子系统定义';

-- ----------------------------
-- Table structure for sys_tree
-- ----------------------------
DROP TABLE IF EXISTS `sys_tree`;
CREATE TABLE `sys_tree` (
  `id_` varchar(64)  NOT NULL COMMENT '主键',
  `key_` varchar(64)  DEFAULT NULL COMMENT '别名',
  `name_` varchar(256)  DEFAULT NULL COMMENT '名字',
  `desc_` varchar(256)  DEFAULT NULL COMMENT '描述',
  `system_` tinyint(4) DEFAULT NULL COMMENT '是否系统内置树',
  PRIMARY KEY (`id_`),
  UNIQUE KEY `key_unique_` (`key_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin    COMMENT='系统树';

-- ----------------------------
-- Table structure for sys_tree_node
-- ----------------------------
DROP TABLE IF EXISTS `sys_tree_node`;
CREATE TABLE `sys_tree_node` (
  `id_` varchar(64)  NOT NULL COMMENT '主键',
  `key_` varchar(64)  DEFAULT NULL COMMENT '别名',
  `name_` varchar(128)  DEFAULT NULL COMMENT '名字',
  `desc_` varchar(256)  DEFAULT NULL COMMENT '描述',
  `tree_id_` varchar(64)  DEFAULT NULL COMMENT '所属树id',
  `parent_id_` varchar(64)  DEFAULT NULL COMMENT '父ID',
  `path_` varchar(512)  DEFAULT NULL COMMENT '路径 eg:pppid.ppid.pid',
  `sn_` tinyint(4) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`id_`),
  UNIQUE KEY `tree_id_key_unique_` (`key_`,`tree_id_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin    COMMENT='系统树节点';

-- ----------------------------
-- Table structure for sys_workbench_layout
-- ----------------------------
DROP TABLE IF EXISTS `sys_workbench_layout`;
CREATE TABLE `sys_workbench_layout` (
  `id_` varchar(64) NOT NULL,
  `panel_id_` varchar(255) NOT NULL COMMENT '面板id',
  `cust_width_` int(10) DEFAULT NULL COMMENT '自定义宽',
  `cust_height_` int(10) DEFAULT NULL COMMENT '自定义高',
  `sn_` int(10) DEFAULT NULL COMMENT '排序',
  `user_id_` varchar(64) NOT NULL COMMENT '用户id',
  `create_time_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id_`),
  KEY `idx_panel_id_` (`panel_id_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='工作台布局';

-- ----------------------------
-- Table structure for sys_workbench_panel
-- ----------------------------
DROP TABLE IF EXISTS `sys_workbench_panel`;
CREATE TABLE `sys_workbench_panel` (
  `id_` varchar(64) NOT NULL,
  `alias_` varchar(255) NOT NULL COMMENT '标识',
  `name_` varchar(255) NOT NULL DEFAULT '' COMMENT '名字',
  `type_` varchar(64) DEFAULT NULL COMMENT '类型',
  `desc_` varchar(500) DEFAULT NULL COMMENT '描述',
  `data_type_` varchar(64) DEFAULT NULL COMMENT '数据类型',
  `data_source_` varchar(255) DEFAULT NULL COMMENT '数据来源',
  `auto_refresh_` int(10) DEFAULT '0' COMMENT '自动刷新',
  `width_` int(10) DEFAULT NULL COMMENT '宽',
  `height_` int(10) DEFAULT NULL COMMENT '高',
  `display_content_` text COMMENT '展示内容',
  `more_url_` varchar(255) DEFAULT NULL COMMENT '更多链接',
  `create_time_` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by_` varchar(64) DEFAULT NULL,
  `update_time_`  datetime  DEFAULT NULL ,
  `update_by_` varchar(64) DEFAULT NULL,
  `delete_flag_` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id_`),
  KEY `idx_alias_` (`alias_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='工作台面板';

-- ----------------------------
-- Table structure for sys_workbench_panel_templ
-- ----------------------------
DROP TABLE IF EXISTS `sys_workbench_panel_templ`;
CREATE TABLE `sys_workbench_panel_templ` (
  `id_` varchar(64) NOT NULL,
  `key_` varchar(255) DEFAULT NULL COMMENT '模板key',
  `name_` varchar(255) DEFAULT NULL,
  `desc_` varchar(500) DEFAULT NULL COMMENT '模板描述',
  `html_` text COMMENT '模板内容',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='工作台面板模板';

CREATE TABLE `sys_file` (
  `id_` varchar(64) NOT NULL COMMENT '主键',
  `name_` varchar(64) DEFAULT NULL COMMENT '附件名',
  `uploader_` varchar(128) DEFAULT NULL COMMENT '上传器',
  `path_` varchar(256) DEFAULT NULL,
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
  `update_time_` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
  `version_` int(11) DEFAULT NULL COMMENT '版本号',
  `delete_` tinyint(4) DEFAULT NULL COMMENT '逻辑删除标记',
  PRIMARY KEY (`id_`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_bin  COMMENT='系统附件';


-- 附件存储 2018-6-10 00:29:06
-- IUploader 实现db策略的上传实现
CREATE TABLE `db_uploader` (
  `id_` varchar(64) NOT NULL,
  `bytes_` longblob,
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




DROP TABLE IF EXISTS `c_holiday_conf`;
CREATE TABLE `c_holiday_conf` (
  `id` varchar(64) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `system` varchar(255) DEFAULT NULL,
  `year` int(255) DEFAULT NULL,
  `startDay` date DEFAULT NULL,
  `endDay` date DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `c_work_calendar` (
  `id` varchar(20) NOT NULL,
  `day` date DEFAULT NULL,
  `isWorkDay` varchar(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `system` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `c_schedule` (
  `id` varchar(20) NOT NULL COMMENT 'ID',
  `title` varchar(500) DEFAULT NULL COMMENT '标题',
  `desc` varchar(2000) DEFAULT NULL COMMENT '描述',
  `task_url` varchar(255) DEFAULT NULL COMMENT '任务连接',
  `type` varchar(64) DEFAULT NULL COMMENT '类型',
  `open_type` varchar(64) DEFAULT NULL COMMENT '任务打开方式',
  `owner` varchar(64) DEFAULT NULL COMMENT '所属人',
  `owner_name` varchar(64) DEFAULT NULL COMMENT '所属人',
  `participant_names` varchar(1000) DEFAULT NULL COMMENT '参与者',
  `start_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '开始日期',
  `end_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '结束日期',
  `actual_start_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '实际开始日期',
  `complete_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `rate_progress` int(10) DEFAULT NULL COMMENT '进度',
  `submitter` varchar(64) DEFAULT NULL COMMENT '提交人',
  `submitNamer` varchar(64) DEFAULT NULL COMMENT '提交人',
  `remark` varchar(500) DEFAULT NULL,
  `isLock` varchar(10) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人',
  `delete_flag` varchar(10) DEFAULT NULL COMMENT '删除标记',
  `rev` int(10) DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日程';



CREATE TABLE `c_schedule_participant` (
  `id_` varchar(20) NOT NULL COMMENT 'id',
  `schedule_id` varchar(20) DEFAULT NULL COMMENT '日程ID',
  `participantor_name` varchar(255) DEFAULT NULL COMMENT '参与者名字',
  `participantor` varchar(64) DEFAULT NULL COMMENT '参与者',
  `rate_progress` int(10) DEFAULT NULL COMMENT 'ilka完成比例',
  `submit_comment` varchar(500) DEFAULT NULL COMMENT 'ilka提交注释',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `actual_start_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'ilka实际开始时间',
  `complete_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'ilka完成时间',
  PRIMARY KEY (`id_`),
  KEY `idx_schedule_id` (`schedule_id`),
  KEY `idx_participantor` (`participantor`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日程参与者';


CREATE TABLE `c_schedule_biz` (
  `id` varchar(20) NOT NULL COMMENT 'id',
  `schedule_id` varchar(20) NOT NULL COMMENT '日程id',
  `biz_id` varchar(20) NOT NULL COMMENT '业务id',
  `from` varchar(64) NOT NULL COMMENT '来源',
  PRIMARY KEY (`id`),
  KEY `idx_schedule_id` (`schedule_id`),
  KEY `idx_biz_id` (`biz_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日程业务关联表';












 