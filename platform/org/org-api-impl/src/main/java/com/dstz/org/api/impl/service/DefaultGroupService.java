package com.dstz.org.api.impl.service;

import com.dstz.base.core.util.BeanUtils;
import com.dstz.org.api.constant.GroupTypeConstant;
import com.dstz.org.api.model.GroupType;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.service.GroupService;
import com.dstz.org.core.manager.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户与组关系的实现类：通过用户找组，通过组找人等
 *
 * @author Administrator
 */
@Service("userGroupService")
public class DefaultGroupService implements GroupService {
    @Resource
    UserManager userManager;

    @Resource
    GroupManager groupManager;

    @Resource
    GroupRelationManager groupRelManager;

    @Resource
    UserRoleManager userRoleManager;

    @Resource
    RoleManager roleManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<IGroup> getGroupsByGroupTypeUserId(String groupType, String userId) {
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            return (List) groupManager.getByUserId(userId);
        }
        if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            return (List) roleManager.getListByUserId(userId);
        }
        if (groupType.equals(GroupTypeConstant.POSITION.key())) {
            return (List) groupRelManager.getListByUserId(userId);
        }
        return null;
    }

    @Override
    public Map<String, List<IGroup>> getAllGroupByAccount(String account) {
        Map<String, List<IGroup>> listMap = new HashMap<String, List<IGroup>>();
        List<IGroup> listOrg = (List) groupManager.getByUserAccount(account);
        if (BeanUtils.isNotEmpty(listOrg)) {
            listMap.put(GroupTypeConstant.ORG.key(), listOrg);
        }
        List<IGroup> listRole = (List) roleManager.getListByAccount(account);
        if (BeanUtils.isNotEmpty(listRole)) {
            listMap.put(GroupTypeConstant.ROLE.key(), listRole);
        }
        List<IGroup> listOrgRel = (List) groupRelManager
                .getListByAccount(account);
        if (BeanUtils.isNotEmpty(listOrgRel)) {
            listMap.put(GroupTypeConstant.POSITION.key(), listOrgRel);
        }
        return listMap;
    }

    @Override
    public Map<String, List<IGroup>> getAllGroupByUserId(String userId) {
        Map<String, List<IGroup>> listMap = new HashMap<String, List<IGroup>>();
        List<IGroup> listOrg = (List) groupManager.getByUserId(userId);
        if (BeanUtils.isNotEmpty(listOrg)) {
            listMap.put(GroupTypeConstant.ORG.key(), listOrg);
        }
        List<IGroup> listRole = (List) roleManager.getListByUserId(userId);
        if (BeanUtils.isNotEmpty(listRole)) {
            listMap.put(GroupTypeConstant.ROLE.key(), listRole);
        }
        List<IGroup> listOrgRel = (List) groupRelManager.getListByUserId(userId);
        if (BeanUtils.isNotEmpty(listOrgRel)) {
            listMap.put(GroupTypeConstant.POSITION.key(), listOrgRel);
        }
        return listMap;
    }

    /**
     * 根据组类别和用户账号获取所有组
     */
    @Override
    public List<IGroup> getGroupsByGroupTypeAccount(String groupType, String account) {
        List<IGroup> listMap = new ArrayList<IGroup>();
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            return (List) groupManager.getByUserAccount(account);
        }
        if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            return (List) roleManager.getListByAccount(account);
        }
        if (groupType.equals(GroupTypeConstant.POSITION.key())) {
            return (List) groupRelManager.getListByAccount(account);
        }
        return listMap;
    }

    /**
     * 根据用户ID获取所有组
     */
    @Override
    public List<IGroup> getGroupsByUserId(String userId) {
        List<IGroup> listMap = new ArrayList<IGroup>();
        List<IGroup> listOrg = (List) groupManager.getByUserId(userId);
        if (BeanUtils.isNotEmpty(listOrg)) {
            listMap.addAll(listOrg);
        }
        List<IGroup> listRole = (List) roleManager.getListByUserId(userId);
        if (BeanUtils.isNotEmpty(listRole)) {
            listMap.addAll(listRole);
        }
        List<IGroup> listOrgRel = (List) groupRelManager.getListByUserId(userId);
        if (BeanUtils.isNotEmpty(listOrgRel)) {
            listMap.addAll(listOrgRel);
        }
        return listMap;
    }

    /**
     * 根据账号获取所有组
     */
    @Override
    public List<IGroup> getGroupsByAccount(String account) {
        List<IGroup> listMap = new ArrayList<IGroup>();
        List<IGroup> listOrg = (List) groupManager.getByUserAccount(account);
        if (BeanUtils.isNotEmpty(listOrg)) {
            listMap.addAll(listOrg);
        }
        List<IGroup> listRole = (List) roleManager.getListByAccount(account);
        if (BeanUtils.isNotEmpty(listRole)) {
            listMap.addAll(listRole);
        }
        List<IGroup> listOrgRel = (List) groupRelManager
                .getListByAccount(account);
        if (BeanUtils.isNotEmpty(listOrgRel)) {
            listMap.addAll(listOrgRel);
        }
        return listMap;
    }

    /**
     * 根据组类别和组ID获取组定义
     */
    @Override
    public IGroup getById(String groupType, String groupId) {
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            return groupManager.get(groupId);
        }
        if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            return roleManager.get(groupId);
        }
        if (groupType.equals(GroupTypeConstant.POSITION.key())) {
            return groupRelManager.get(groupId);
        }
        return null;
    }

    /**
     * 根据组类别和组编码获取组定义
     */
    @Override
    public IGroup getByCode(String groupType, String code) {
        if (groupType.equals(GroupTypeConstant.ORG.key())) {
            return groupManager.getByCode(code);
        }
        if (groupType.equals(GroupTypeConstant.ROLE.key())) {
            return roleManager.getByAlias(code);
        }
        if (groupType.equals(GroupTypeConstant.POSITION.key())) {
            return groupRelManager.getByCode(code);
        }
        return null;
    }

    /**
     * 获取所有组类型
     */
    @Override
    public List<GroupType> getGroupTypes() {
        List<GroupType> list = new ArrayList<GroupType>();
        for (GroupTypeConstant e : GroupTypeConstant.values()) {
            GroupType type = new GroupType(e.key(), e.label());
            list.add(type);
        }
        return list;
    }

    @Override
    public IGroup getMainGroup(String userId) {
        return groupManager.getMainGroup(userId);
    }

}
