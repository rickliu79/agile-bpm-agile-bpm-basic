package com.dstz.sys.core.manager.impl;

import com.dstz.base.core.cache.ICache;
import com.dstz.base.core.id.IdUtil;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.sys.core.dao.ResRoleDao;
import com.dstz.sys.core.manager.ResRoleManager;
import com.dstz.sys.core.model.ResRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <pre>
 * 描述：角色资源分配 处理实现类
 * </pre>
 */
@Service("resRoleManager")
public class ResRoleManagerImpl extends BaseManager<String, ResRole> implements ResRoleManager {
    @Resource
    ResRoleDao resRoleDao;
    @Resource
    ICache iCache;
    public final String URL_ROLE_MAPPING = "agilebpm:sys:resoucesUrlRoleMapping:";

    @Override
    public List<ResRole> getAllByRoleId(String roleId) {
        return resRoleDao.getByRoleId(roleId);
    }


    @Override
    public void assignResByRoleSys(String resIds, String systemId, String roleId) {
        resRoleDao.removeByRoleAndSystem(roleId, systemId);

        String[] aryRes = resIds.split(",");
        for (String resId : aryRes) {
            if ("0".equals(resId)) {
            	continue;
            }
            ResRole resRole = new ResRole(systemId,resId,roleId);
            resRoleDao.create(resRole);
        }

    }

   

    @Override
    public Map<String, Set<String>> getUrlRoleMappingBySystem(String systemId) {
        String urlCacheKey = URL_ROLE_MAPPING.concat(systemId);
        if (iCache.containKey(urlCacheKey)) {
            return (Map<String, Set<String>>) iCache.getByKey(urlCacheKey);
        }

        List<ResRole> list = resRoleDao.getResRoleBySystemId(systemId);

        Map<String, Set<String>> urlRoleMapping = new HashMap<String, Set<String>>();

        for (ResRole res : list) {
            String url = res.getUrl();
            if (urlRoleMapping.containsKey(url)) {
                Set<String> set = urlRoleMapping.get(url);
                set.add(res.getRoleAlias());
            } else {
                Set<String> set = new HashSet<String>();
                set.add(res.getRoleAlias());
                urlRoleMapping.put(url, set);
            }
        }
        //添加到缓存
        iCache.add(urlCacheKey, urlRoleMapping);
        return urlRoleMapping;
    }

    @Override
    public void cleanResoucesCache(String systemId) {
        String urlStr = URL_ROLE_MAPPING .concat(systemId);
        iCache.delByKey(urlStr);
    }


}
