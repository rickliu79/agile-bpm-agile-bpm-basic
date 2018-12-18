package com.dstz.sys.core.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dstz.base.core.id.IdUtil;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.core.dao.RelResourceDao;
import com.dstz.sys.core.dao.SysResourceDao;
import com.dstz.sys.core.manager.SysResourceManager;
import com.dstz.sys.core.model.RelResource;
import com.dstz.sys.core.model.SysResource;

import cn.hutool.core.collection.CollectionUtil;

/**
 * <pre>
 * 描述：子系统资源 处理实现类
 * </pre>
 */
@Service("sysResourceManager")
public class SysResourceManagerImpl extends BaseManager<String, SysResource> implements SysResourceManager {
    @Resource
    SysResourceDao sysResourceDao;

    @Resource
    RelResourceDao resouceRelationDao;


    @Override
    public List<SysResource> getBySystemId(String id) {
        List<SysResource> list = sysResourceDao.getBySystemId(id);

        return list;
    }

    @Override
    public SysResource getByResId(String id) {
        SysResource sysResource = this.get(id);
        sysResource.setRelResources(resouceRelationDao.getByResourceId(id));
        return sysResource;
    }

    @Override
    public void create(SysResource resource) {
        resource.setId(IdUtil.getSuid());
        List<RelResource> relationList = resource.getRelResources();
        
        for (RelResource relation : relationList) {
            relation.setResId(resource.getId());
            resouceRelationDao.create(relation);
        }
        
        sysResourceDao.create(resource);
    }

    @Override
    public void update(SysResource sysResource) {
        resouceRelationDao.removeByResId(sysResource.getId());
        
        List<RelResource> relResourcesList = sysResource.getRelResources();
        for (RelResource rel : relResourcesList) {
            rel.setResId(rel.getId());
            resouceRelationDao.create(rel);
        }
        sysResourceDao.update(sysResource);
    }

    @Override
    public List<SysResource> getBySystemAndRole(String systemId, String roleId) {
        return sysResourceDao.getBySystemAndRole(systemId, roleId);
    }

    @Override
    public boolean isExist(SysResource resource) {
        boolean rtn = sysResourceDao.isExist(resource)>0;
        return rtn;
    }

    @Override
    public void removeByResId(String resId) {
    	SysResource resource = sysResourceDao.get(resId);
    	if(resource == null) return ;
    	List<SysResource> relatedResouces = new ArrayList<>();
    	
    	 getChildList(relatedResouces,resource.getId());
    	
        for (SysResource r : relatedResouces) {
            this.remove(r.getId());
        }
    }


    private void getChildList(List<SysResource> relatedResouces, String id) {
    	List<SysResource> children = sysResourceDao.getByParentId(id);
    	if(CollectionUtil.isEmpty(children))return;
    	
    	for (SysResource r : children) {
    		getChildList(relatedResouces, r.getId());
        }
    	relatedResouces.addAll(children);
	}

	@Override
    public void remove(String entityId) {
        resouceRelationDao.removeByResId(entityId);
        super.remove(entityId);
    }


    @Override
    public List<SysResource> getBySystemAndUser(String systemId, String userId) {
        List<SysResource> list = sysResourceDao.getBySystemAndUser(systemId, userId);
        return list;
    }


}
