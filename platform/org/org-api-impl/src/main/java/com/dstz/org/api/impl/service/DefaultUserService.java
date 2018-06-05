package com.dstz.org.api.impl.service;


import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.org.core.manager.GroupManager;
import com.dstz.org.core.manager.UserManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;


@SuppressWarnings("unchecked")
@Service(value = "userService")
public class DefaultUserService implements UserService {
    @Resource
    UserManager userManager;
    @Resource
    GroupManager groupManager;

    @Override
    public IUser getUserById(String userId) {
        return userManager.get(userId);
    }

    @Override
    public IUser getUserByAccount(String account) {
        return userManager.getByAccount(account);
    }

    @Override
    public List<IUser> getUserListByGroup(String groupType, String groupId) {
        //此处可以根据不同的groupType去调用真实的实现：如角色下的人，组织下的人
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            return (List) userManager.getUserListByOrgId(groupId);
        }

        if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            return (List) userManager.getUserListByRoleId(groupId);
        }
        if (groupType.equals(GroupTypeConstant.POSITION.key())) {
            return (List) userManager.getListByRelId(groupId);
        }
        
        return Collections.emptyList();
    }


}
