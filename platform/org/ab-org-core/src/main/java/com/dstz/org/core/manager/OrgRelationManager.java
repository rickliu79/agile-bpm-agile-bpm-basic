package com.dstz.org.core.manager;

import java.util.List;

import com.dstz.base.manager.Manager;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.model.OrgRelation;

/**
 * 用户组织关系 Manager处理接口
 * @author Jeff
 * @email for_office@qq.com
 * @time 2018-12-16 01:07:59
 */
public interface OrgRelationManager extends Manager<String, OrgRelation>{

	List getPostByUserId(String userId);

	IGroup getPostById(String groupId);
	
	List<OrgRelation>  getUserRelation(String userId,String relationType);

	void removeByUserId(String id);
	
}
