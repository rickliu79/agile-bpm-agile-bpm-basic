package com.dstz.org.rest.controller;

import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.manager.Manager;
import com.dstz.base.rest.BaseController;
import com.dstz.org.core.manager.RoleManager;
import com.dstz.org.core.model.Role;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 角色管理 控制器类
 */
@RestController
@RequestMapping("/org/role")
public class RoleController extends BaseController<Role> {
    @Resource
    RoleManager roleManager;


    @Override
    protected String getModelDesc() {
        return "角色";
    }

    @Override
    @CatchErr
    public ResultMsg<String> save( @RequestBody Role role) throws Exception {
        if (StringUtil.isEmpty(role.getId())) {
            boolean isExist = roleManager.isRoleExist(role);
            if (isExist) {
                throw new BusinessException("角色在系统中已存在!");
            }
        }
       return super.save(role);
    }

}
