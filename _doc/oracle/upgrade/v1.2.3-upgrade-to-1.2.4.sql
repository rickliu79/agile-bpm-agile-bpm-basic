ALTER TABLE "BPM_TASK_STACK" 
MODIFY ("TASK_ID_" NOT NULL );

ALTER TABLE "BPM_TASK_STACK" 
ADD ("NODE_TYPE_" VARCHAR2(64) )
ADD ("ACTION_NAME_" VARCHAR2(64) );

ALTER TABLE "BPM_TASK_STACK" DROP ("PATH_");

CREATE INDEX "idx_exestack_taskid" 
  ON "BPM_TASK_STACK" ("TASK_ID_"); 
	
  update bpm_task_stack set  node_type_ = 'userNode';