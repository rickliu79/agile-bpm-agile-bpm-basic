CREATE TABLE `org_user` (
  `id_` varchar(64) NOT NULL,
  `fullname_` varchar(255) NOT NULL COMMENT '姓名',
  `account_` varchar(255) NOT NULL COMMENT '账号',
  `password_` varchar(64) NOT NULL COMMENT '密码',
  `email_` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `mobile_` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `weixin_` varchar(64) DEFAULT NULL COMMENT '微信号',
  `address_` varchar(512) DEFAULT NULL COMMENT '地址',
  `photo_` varchar(255) DEFAULT NULL COMMENT '头像',
  `sex_` varchar(10) DEFAULT NULL COMMENT '性别：男，女，未知',
  `from_` varchar(64) DEFAULT NULL COMMENT '来源',
  `status_` int(11) NOT NULL DEFAULT '1' COMMENT '0:禁用，1正常',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time_` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';


CREATE TABLE `org_role` (
  `id_` varchar(64) NOT NULL,
  `name_` varchar(64) NOT NULL COMMENT '角色名称',
  `alias_` varchar(64) NOT NULL COMMENT '英文别名',
  `enabled_` int(11) NOT NULL DEFAULT '1' COMMENT '0：禁用，1：启用',
  `description` varchar(200) NOT NULL COMMENT '描述',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time_` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人',
  `type_id_` varchar(64) DEFAULT NULL COMMENT '分类ID',
  `type_name_` varchar(64) DEFAULT NULL COMMENT '分类名字',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色管理';


CREATE TABLE `org_group` (
  `id_` varchar(64) NOT NULL COMMENT '主键',
  `name_` varchar(64) NOT NULL COMMENT '名字',
  `parent_id_` varchar(64) DEFAULT NULL COMMENT '父ID',
  `code_` varchar(64) NOT NULL COMMENT '编码',
  `type_` int(10) DEFAULT NULL COMMENT '类型',
  `desc_` varchar(500) DEFAULT NULL COMMENT '描述',
  `path_` varchar(1000) DEFAULT NULL,
  `sn_` int(11) DEFAULT '100' COMMENT '排序',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time_` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组';

CREATE TABLE `org_relation` (
  `id_` varchar(64) NOT NULL COMMENT 'ID',
  `group_id_` varchar(64) NOT NULL COMMENT '组ID',
  `user_id_` varchar(64) NOT NULL COMMENT '用户ID',
  `is_master_` int(11) NOT NULL DEFAULT '0' COMMENT '0:默认组织，1：从组织',
  `role_id_` varchar(64) DEFAULT NULL COMMENT '角色ID',
  `type` varchar(255) DEFAULT NULL COMMENT '类型：groupUser,groupRole,userRole,groupUserRole',
  `create_time_` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by_` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_time_` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by_` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id_`),
  KEY `user_id_idex` (`user_id_`) USING BTREE,
  KEY `group_id_idx` (`group_id_`) USING BTREE,
  KEY `role_id_idx` (`role_id_`) USING BTREE,
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组织关系';

