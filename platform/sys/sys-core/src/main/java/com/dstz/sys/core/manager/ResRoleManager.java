package com.dstz.sys.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.sys.core.model.ResRole;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * 描述：角色资源分配 处理接口
 * </pre>
 */
public interface ResRoleManager extends Manager<String, ResRole> {

    List<ResRole> getAllByRoleId(String roleId);

    /**
     * 分配角色资源。
     *
     * @param resIds
     * @param systemId
     * @param roleId
     */
    void assignResByRoleSys(String resIds, String systemId, String roleId);

    /**
     * 清除缓存。
     *
     * @param systemId
     */
    void cleanResoucesCache(String systemId);
    
    /**
     * 通过url 获取可访问的角色
     * @param url
     * @return
     */
	Set<String> getAccessRoleByUrl(String url);

}
