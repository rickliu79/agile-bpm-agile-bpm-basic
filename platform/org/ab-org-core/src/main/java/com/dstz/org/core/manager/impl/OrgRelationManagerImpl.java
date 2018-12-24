package com.dstz.org.core.manager.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dstz.base.core.cache.ICache;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.constant.RelationTypeConstant;
import com.dstz.org.core.dao.OrgRelationDao;
import com.dstz.org.core.model.OrgRelation;
import com.dstz.sys.util.ContextUtil;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import cn.hutool.core.util.ArrayUtil;

import com.dstz.org.core.manager.OrgRelationManager;
/**
 * 用户组织关系 Manager处理实现类
 * @author Jeff
 * @email for_office@qq.com
 * @time 2018-12-16 01:07:59
 */
@Service("orgRelationManager")
public class OrgRelationManagerImpl extends BaseManager<String, OrgRelation> implements OrgRelationManager{
	@Resource
	OrgRelationDao orgRelationDao;

	@Override
	public List getPostByUserId(String userId) {
		return orgRelationDao.getUserRelation(userId, RelationTypeConstant.POST.getKey());
	}

	@Override
	public IGroup getPostById(String groupId) {
		return null;
	}

	@Override
	public List<OrgRelation> getUserRelation(String userId, String relationType) {
		return orgRelationDao.getUserRelation(userId, relationType);
	}

	@Override
	public void removeByUserId(String userId) {
		orgRelationDao.removeByUserId(userId);
	}

	@Override
	public List<OrgRelation> getGroupPost(String groupId) {
		return orgRelationDao.getGroupPost(groupId);
	}

	@Override
	public void removeGroupPostById(String id) {
		orgRelationDao.removeGroupPostById(id);
	}

	/**
	 * 呵呵
	 */
	@Override
	public void updateUserGroupRelationIsMaster(String id) {
		OrgRelation relation = orgRelationDao.get(id);
		if(relation == null || StringUtil.isEmpty(relation.getUserId())) return ;
		
		List<String> relationList = Arrays.asList(RelationTypeConstant.GROUP_USER.getKey(),RelationTypeConstant.POST_USER.getKey());
		//查询出用户 与 岗位，组织的所有关系，置为 非主版本
		List<OrgRelation>  userGroupRelations = orgRelationDao.getRelationsByParam(relationList, relation.getUserId(), null, null);
		userGroupRelations.forEach(rel ->{
			rel.setIsMaster(0);
			orgRelationDao.update(rel);
		});
		//切换是否主版本
		relation.setIsMaster(relation.getIsMaster() == 0 ? 1:0);
		orgRelationDao.update(relation);
	}

	@Override
	public void changeStatus(String id, int status) {
	   OrgRelation relation = orgRelationDao.get(id);
	   if(relation == null) return ;
	   
	   relation.setStatus(status);
	   orgRelationDao.update(relation);
	   
	   String userId = relation.getUserId();
	   if(StringUtil.isEmpty(userId)) return;
	   
	   //清楚缓存
	   ICache<IGroup> iCache = AppUtil.getBean(ICache.class);
       String userKey = ICurrentContext.CURRENT_ORG + relation.getUserId();
       iCache.delByKey(userKey);
	}

	@Override
	public void saveUserGroupRelation(String groupId, String[] roleIds, String[] userIds) {
		for(String userId : userIds) {
			if(StringUtil.isEmpty(userId))continue;
			OrgRelation orgRelation = new OrgRelation(groupId,userId,null,RelationTypeConstant.GROUP_USER.getKey());
			if(ArrayUtil.isNotEmpty(roleIds)) {
				for(String roleId : roleIds) {
					orgRelation.setRoleId(roleId);
					orgRelation.setType(RelationTypeConstant.POST_USER.getKey());
					
					orgRelationDao.create(orgRelation);
					continue;
				}
				continue;
			}
			orgRelationDao.create(orgRelation);
		}
	}

}
