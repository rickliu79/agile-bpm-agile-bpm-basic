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



