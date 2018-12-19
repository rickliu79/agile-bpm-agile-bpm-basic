package com.dstz.org.core.manager.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.core.dao.UserDao;
import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.manager.UserManager;
import com.dstz.org.core.model.OrgRelation;
import com.dstz.org.core.model.User;

import cn.hutool.core.collection.CollectionUtil;

/**
 * <pre>
 * 描述：用户表 处理实现类
 * </pre>
 */
@Service("userManager")
public class UserManagerImpl extends BaseManager<String, User> implements UserManager {
    @Resource
    UserDao userDao;
    @Resource
    OrgRelationManager orgRelationMananger;

    public User getByAccount(String account) {
        return this.userDao.getByAccount(account);
    }

    @Override
    public boolean isUserExist(User user) {
        return userDao.isUserExist(user)>0;
    }

	@Override
	public List<User> getUserListByRelation(String relId, String type) {
		return userDao.getUserListByRelation(relId,type);
	}

	@Override
	public void saveUserInfo(User user) {
		this.create(user);
		
		List<OrgRelation> orgRelationList = user.getOrgRelationList();
		if(CollectionUtil.isEmpty(orgRelationList)) return;
		
		for(OrgRelation rel : orgRelationList) {
			orgRelationMananger.create(rel);
		}
	}

}
