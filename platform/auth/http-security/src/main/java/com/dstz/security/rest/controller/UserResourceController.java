package com.dstz.security.rest.controller;

import com.alibaba.fastjson.JSONObject;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.BeanUtils;
import com.dstz.base.core.util.string.StringUtil;
import com.dstz.base.rest.GenericController;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import com.dstz.org.core.manager.GroupManager;
import com.dstz.org.core.model.Group;
import com.dstz.security.util.SubSystemUtil;
import com.dstz.sys.core.manager.SubsystemManager;
import com.dstz.sys.core.manager.SysResourceManager;
import com.dstz.sys.core.model.Subsystem;
import com.dstz.sys.core.model.SysResource;
import com.dstz.sys.util.ContextUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户资源
 *
 * @author Jeff
 */
@RestController
public class UserResourceController extends GenericController {
    @Resource
    SubsystemManager subsystemManager;
    @Resource
    GroupManager orgManager;
    @Resource
    SysResourceManager sysResourceManager;


    @RequestMapping("userResource/userMsg")
    @CatchErr
    public ResultMsg<JSONObject> userMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Subsystem> subsystemList = subsystemManager.getCuurentUserSystem();
        JSONObject mv = new JSONObject();
        if (BeanUtils.isEmpty(subsystemList)) {
            throw new BusinessException("当前用户尚未分配任何资源权限！");
        }

        String systemId = SubSystemUtil.getSystemId(request);
        Subsystem currentSystem = null;
        //获取当前系统
        if (StringUtil.isNotEmpty(systemId)) {
            for (Subsystem system : subsystemList) {
                if (system.getId().equals(systemId)) {
                    currentSystem = system;
                    break;
                }
            }
        } else {
            //获取默认系统
            currentSystem = subsystemManager.getDefaultSystem(ContextUtil.getCurrentUserId());
        }

        //没有之前使用的系统
        if (currentSystem == null) {
            currentSystem = subsystemList.get(0);
        }
        SubSystemUtil.setSystemId(request, response, currentSystem.getId());

        IGroup group = ContextUtil.getCurrentGroup();
        List<Group> orgList = orgManager.getByUserId(ContextUtil.getCurrentUserId());

        mv.put("currentEnviroment",AppUtil.getCtxEnvironment());
        mv.put("subsystemList", subsystemList);
        mv.put("currentSystem", currentSystem);
        mv.put("currentOrg", group);
        mv.put("orgList", orgList);
        mv.put("user", ContextUtil.getCurrentUser());
        mv.put("resourceList", getSysResource(request, response));
        return getSuccessResult(mv);
    }

    
    // 重新获取 userMsg
    @RequestMapping("userResource/changeSystem")
    public ResultMsg changeSystem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = RequestUtil.getString(request, "id");
        SubSystemUtil.setSystemId(request, response, id);

        return getSuccessResult("切换成功");
    }
    // 重新获取 userMsg
    @RequestMapping("userResource/changeOrg")
    public ResultMsg changeOrg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = RequestUtil.getString(request, "id");
        Group org = orgManager.get(id);
        ContextUtil.setCurrentOrg(org);

        return getSuccessResult("切换成功");
    }

    @RequestMapping("userResource/getResTree")
    public List<SysResource> getSysResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IUser user = ContextUtil.getCurrentUser();
        String systemId = SubSystemUtil.getSystemId(request);
        boolean isAdmin = user.isAdmin();
        List<SysResource> list = null;
        if (isAdmin) {
            list = sysResourceManager.getBySystemId(systemId);
        } else {
            list = sysResourceManager.getBySystemAndUser(systemId, user.getUserId());
        }

        return BeanUtils.listToTree(list);
    }
}
