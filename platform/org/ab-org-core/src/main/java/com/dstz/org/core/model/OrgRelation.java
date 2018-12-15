package com.dstz.org.core.model;
import java.util.Date;
import java.math.BigDecimal;

import com.dstz.base.core.model.BaseModel;


/**
 * 用户组织关系 实体对象
 * @author Jeff
 * @email for_office@qq.com
 * @time 2018-12-16 01:07:59
 */
public class OrgRelation extends BaseModel{
	/**
	* 组ID
	*/
	protected  String groupId; 
	/**
	* 用户ID
	*/
	protected  String userId; 
	/**
	* 0:默认组织，1：从组织
	*/
	protected  Integer isMaster; 
	/**
	* 角色ID
	*/
	protected  String roleId; 
	/**
	* 类型：groupUser,groupRole,userRole,groupUserRole
	*/
	protected  String type; 
	
	
	
	
	
	public void setGroupId( String groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * 返回 组ID
	 * @return
	 */
	public  String getGroupId() {
		return this.groupId;
	}
	
	
	
	
	public void setUserId( String userId) {
		this.userId = userId;
	}
	
	/**
	 * 返回 用户ID
	 * @return
	 */
	public  String getUserId() {
		return this.userId;
	}
	
	
	
	
	public void setIsMaster( Integer isMaster) {
		this.isMaster = isMaster;
	}
	
	/**
	 * 返回 0:默认组织，1：从组织
	 * @return
	 */
	public  Integer getIsMaster() {
		return this.isMaster;
	}
	
	
	
	
	public void setRoleId( String roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * 返回 角色ID
	 * @return
	 */
	public  String getRoleId() {
		return this.roleId;
	}
	
	
	
	
	public void setType( String type) {
		this.type = type;
	}
	
	/**
	 * 返回 类型：groupUser,groupRole,userRole,groupUserRole
	 * @return
	 */
	public  String getType() {
		return this.type;
	}
	
	
	
	
	
 
}