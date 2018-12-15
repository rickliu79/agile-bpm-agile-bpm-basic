package com.dstz.org.core.model;
import java.util.Date;
import java.math.BigDecimal;

import com.dstz.base.core.model.BaseModel;


/**
 * 组 实体对象
 * @author Jeff
 * @email for_office@qq.com
 * @time 2018-12-16 01:11:44
 */
public class Group extends BaseModel{
	/**
	* 名字
	*/
	protected  String name; 
	/**
	* 父ID
	*/
	protected  String parentId; 
	/**
	* 编码
	*/
	protected  String code; 
	/**
	* 类型
	*/
	protected  Integer type; 
	/**
	* 描述
	*/
	protected  String desc; 
	/**
	* path_
	*/
	protected  String path; 
	/**
	* 排序
	*/
	protected  Integer sn; 
	
	
	
	
	
	public void setName( String name) {
		this.name = name;
	}
	
	/**
	 * 返回 名字
	 * @return
	 */
	public  String getName() {
		return this.name;
	}
	
	
	
	
	public void setParentId( String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 返回 父ID
	 * @return
	 */
	public  String getParentId() {
		return this.parentId;
	}
	
	
	
	
	public void setCode( String code) {
		this.code = code;
	}
	
	/**
	 * 返回 编码
	 * @return
	 */
	public  String getCode() {
		return this.code;
	}
	
	
	
	
	public void setType( Integer type) {
		this.type = type;
	}
	
	/**
	 * 返回 类型
	 * @return
	 */
	public  Integer getType() {
		return this.type;
	}
	
	
	
	
	public void setDesc( String desc) {
		this.desc = desc;
	}
	
	/**
	 * 返回 描述
	 * @return
	 */
	public  String getDesc() {
		return this.desc;
	}
	
	
	
	
	public void setPath( String path) {
		this.path = path;
	}
	
	/**
	 * 返回 path_
	 * @return
	 */
	public  String getPath() {
		return this.path;
	}
	
	
	
	
	public void setSn( Integer sn) {
		this.sn = sn;
	}
	
	/**
	 * 返回 排序
	 * @return
	 */
	public  Integer getSn() {
		return this.sn;
	}
	
	
	
	
	
 
}