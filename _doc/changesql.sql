ALTER TABLE bpm_task_stack DROP COLUMN path_;

ALTER TABLE `bpm_task_stack` MODIFY COLUMN `task_id_`  varchar(64) NOT NULL COMMENT '任务ID' AFTER `id_`,
ADD COLUMN node_type_  varchar(64) NULL AFTER is_muliti_task_;

update bpm_task_stack set  node_type_ = 'userNode';

ALTER TABLE `bpm_task_stack` ADD INDEX `idx_exestack_taskid` (`task_id_`) ;