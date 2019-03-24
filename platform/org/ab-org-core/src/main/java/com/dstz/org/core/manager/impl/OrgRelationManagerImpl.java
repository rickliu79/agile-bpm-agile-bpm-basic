package com.dstz.org.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.core.constant.RelationTypeConstant;
import com.dstz.org.core.dao.OrgRelationDao;
import com.dstz.org.core.model.OrgRelation;
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

}
