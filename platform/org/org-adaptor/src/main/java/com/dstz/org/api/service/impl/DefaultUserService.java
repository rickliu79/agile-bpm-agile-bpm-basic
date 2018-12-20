package com.dstz.org.api.service.impl;


import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dstz.base.core.util.BeanCopierUtils;
import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.model.IUserRole;
import com.dstz.org.api.model.dto.UserDTO;
import com.dstz.org.api.model.dto.UserRoleDTO;
import com.dstz.org.api.service.UserService;
import com.dstz.org.core.manager.GroupManager;
import com.dstz.org.core.manager.UserManager;
import com.dstz.org.core.manager.UserRoleManager;
import com.dstz.org.core.model.User;
import com.dstz.org.core.model.UserRole;

import cn.hutool.core.collection.CollectionUtil;


@SuppressWarnings("unchecked")
@Service(value = "userService")
public class DefaultUserService implements UserService {
    @Resource
    UserManager userManager;
    @Resource
    GroupManager groupManager;
    @Resource
    UserRoleManager userRoleManager;

    @Override
    public IUser getUserById(String userId) {
    	IUser user = userManager.get(userId);
        return BeanCopierUtils.transformBean(user, UserDTO.class);
    }

    @Override
    public IUser getUserByAccount(String account) {
    	IUser user =  userManager.getByAccount(account);
    	return BeanCopierUtils.transformBean(user, UserDTO.class);
    }

    @Override
    public List<IUser> getUserListByGroup(String groupType, String groupId) {
        //此处可以根据不同的groupType去调用真实的实现：如角色下的人，组织下的人
    	
    	List<User> userList  = null;
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
        	userList = userManager.getUserListByOrgId(groupId);
        }

        if (groupType.equals(GroupTypeConstant.ROLE.key())) {
        	userList =   userManager.getUserListByRoleId(groupId);
        }
        if (groupType.equals(GroupTypeConstant.POST.key())) {
        	userList =   userManager.getListByRelId(groupId);
        }
       
        if(CollectionUtil.isNotEmpty(userList)) {
        	return (List)BeanCopierUtils.transformList(userList, UserDTO.class);
        }
        
        return Collections.emptyList();
    }

	@Override
	public List<IUserRole> getUserRole(String userId) {
		List<UserRole> userRole = userRoleManager.getListByUserId(userId);
		
		return  (List)BeanCopierUtils.transformList(userRole, UserRoleDTO.class);
	}


}
