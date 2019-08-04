
-- 屏蔽掉开发平台的个人办公
UPDATE `sys_resource` SET `ENABLE_`='0' WHERE `ID_`='1';

-- 把这几个菜单的 url 加上   ?type=iview  (不改也行)
-- eg: bpm/task/taskList.html?type=iview
Select * from sys_resource where id_ in (1000000071000 , 10000000710006 , 20000003070153);

-- 屏蔽掉vue表单的入口，使用 pc_iview
UPDATE `sys_resource` SET `ENABLE_`='0' WHERE `ID_`='33';
UPDATE `sys_resource` SET `ENABLE_`='1' WHERE `ID_`='30';

-- 门户平台sql
INSERT INTO `sys_subsystem`(`id_`, `name_`, `alias_`, `url_`, `open_type_`, `enabled_`, `create_time_`, `is_default_`, `desc_`, `config_`, `create_by_`, `update_time_`, `update_by_`) VALUES ('2', '门户平台', 'eip', 'http://localhost:8081/#/', '_blank', 1, NULL, 0, '子系统', '用于系统特殊配置', NULL, NULL, NULL);
