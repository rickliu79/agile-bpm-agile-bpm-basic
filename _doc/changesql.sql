ALTER TABLE bpm_task_stack DROP COLUMN path_;

ALTER TABLE bpm_task_stack
MODIFY COLUMN task_id_  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务ID' AFTER id_,
ADD COLUMN node_type_  varchar(64) NULL AFTER is_muliti_task_;

ALTER TABLE `bpm_task_stack`
ADD INDEX `idx_exestack_taskid` (`task_id_`) ;