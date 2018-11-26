-- 流程任务提交日志表 2018-11-26 23:06:41
CREATE TABLE `bpm_submit_data_log` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `task_id_` varchar(64) DEFAULT NULL COMMENT '任务ID',
  `inst_id_` varchar(64) DEFAULT NULL COMMENT '实例ID',
  `data` longtext COMMENT '业务数据',
  `destination` varchar(255) DEFAULT NULL COMMENT '目标节点',
  `extendConf` varchar(500) DEFAULT NULL COMMENT '特殊配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务对象数据提交日志';

