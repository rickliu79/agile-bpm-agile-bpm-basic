-- 有新增的SQL 添加进该文件，标注上日期
-- 2018-6-10 00:27:01
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统附件';

-- 附件存储 2018-6-10 00:29:06
-- IUploader 实现db策略的上传实现
CREATE TABLE `db_uploader` (
  `id_` varchar(64) NOT NULL,
  `bytes_` longblob,
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 表单分类
ALTER TABLE `form_def` 
ADD COLUMN `type_` varchar(64) NULL COMMENT '分类（pc/mobile）' AFTER `id_`;

-- 表单模板新增类型
ALTER TABLE `form_template` 
ADD COLUMN `form_type_` varchar(64) NULL COMMENT '表单类型（pc/mobile/vuepc）' AFTER `name_`;

-- 资源菜单
UPDATE  `sys_resource` SET `ID_`='31', `SYSTEM_ID_`='1', `ALIAS_`='mobileForm', `NAME_`='手机表单', `default_url_`='form/formDef/mobileFormDefList.html', `ENABLE_MENU_`='1', `HAS_CHILDREN_`='1', `OPENED_`='1', `ICON_`='', `NEW_WINDOW_`='0', `SN_`='0', `PARENT_ID_`='28', `CREATE_TIME_`=NULL WHERE (`ID_`='31');

-- 更新意见时间
ALTER TABLE `bpm_task_opinion`
MODIFY COLUMN `dur_ms_`  bigint(20) NULL DEFAULT NULL COMMENT '持续时间(ms)' AFTER `create_time_`;

ALTER TABLE `bpm_instance`
MODIFY COLUMN `duration_`  bigint(20) NULL DEFAULT NULL COMMENT '持续时间(ms)' AFTER `end_time_`;

-- 插入一个流水号demo
INSERT INTO `sys_serialno` (`id_`, `name_`, `alias_`, `regulation_`, `gen_type_`, `no_length_`, `cur_date_`, `init_value_`, `cur_value_`, `step_`) VALUES ('10000001620002', '每天使用一组流水号', 'dayNo', '{yyyy}{MM}{DD}{NO}', '1', '5', '20180710', '1', '1', '1');
