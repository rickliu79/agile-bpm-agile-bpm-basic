package com.dstz.org.core.model;

import com.dstz.base.core.model.BaseModel;
import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.model.GroupStructEnum;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IdentityType;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;


/**
 * 组织关系关联对象
 */
public class GroupRelation extends BaseModel implements IGroup {

    protected String id;

    protected String groupId;

    protected String relDefId;

    protected String relName;

    protected String relCode;
    protected String orgName;
    protected String jobName;

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public String getOrgName() {
        return this.orgName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }


    public void setRelDefId(String relDefId) {
        this.relDefId = relDefId;
    }


    public String getRelDefId() {
        return this.relDefId;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }

    public String getRelName() {
        return this.relName;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }


    public String getRelCode() {
        return this.relCode;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.id)
                .append("groupId", this.groupId)
                .append("relDefId", this.relDefId)
                .append("relName", this.relName)
                .append("relCode", this.relCode)
                .toString();
    }

    public String getIdentityType() {
        return IdentityType.GROUP;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public String getName() {
        return this.relName;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupCode() {
        return this.relCode;
    }

    public Long getSn() {
        return Long.valueOf(0);
    }

    public String getGroupType() {
        return GroupTypeConstant.POSITION.key();
    }

    public GroupStructEnum getStruct() {
        return GroupStructEnum.PLAIN;
    }

    public String getParentId() {
        return null;
    }

    public String getPath() {
        return null;
    }

    public Map<String, Object> getParams() {
        return null;
    }
}