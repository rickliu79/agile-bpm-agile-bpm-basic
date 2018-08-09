package com.dstz.sys.rest.controller;

import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.id.UniqueIdUtil;
import com.dstz.base.db.model.page.PageJson;
import com.dstz.base.rest.GenericController;
import com.dstz.base.rest.util.RequestUtil;
import com.dstz.sys2.manager.SysDataSourceDefManager;
import com.dstz.sys2.model.SysDataSourceDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <pre>
 * 描述：sysDataSourceDef层的controller
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:下午5:11:06
 * 版权:summer
 * </pre>
 */
@Controller
@RequestMapping("/sys/sysDataSourceDef/")
public class SysDataSourceDefController extends GenericController {
    @Autowired
    SysDataSourceDefManager sysDataSourceDefManager;

    /**
     * <pre>
     * 根据类路径获取类字段
     * </pre>
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("initAttributes")
    @CatchErr(write2response = true, value = "初始化属性异常")
    public void initAttributes(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String classPath = RequestUtil.getString(request, "classPath");
        writeSuccessData(response, sysDataSourceDefManager.initAttributes(classPath));
    }

    /**
     * <pre>
     * sysDataSourceDefEdit.html的save后端
     * </pre>
     *
     * @param request
     * @param response
     * @param sysDataSourceDef
     * @throws Exception
     */
    @RequestMapping("save")
    @CatchErr(write2response = true, value = "保存数据源模板失败")
    public void save(HttpServletRequest request, HttpServletResponse response, @RequestBody SysDataSourceDef sysDataSourceDef) throws Exception {
        if (StringUtil.isEmpty(sysDataSourceDef.getId())) {
            sysDataSourceDef.setId(UniqueIdUtil.getSuid());
            sysDataSourceDefManager.create(sysDataSourceDef);
        } else {
            sysDataSourceDefManager.update(sysDataSourceDef);
        }

        writeSuccessData(response, sysDataSourceDef, "保存数据源模板成功");
    }

    /**
     * <pre>
     * 获取sysDataSourceDef的后端
     * 目前支持根据id 获取sysDataSourceDef
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getObject")
    @CatchErr(write2response = true, value = "获取sysDataSourceDef异常")
    public void getObject(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = RequestUtil.getString(request, "id");
        SysDataSourceDef sysDataSourceDef = null;
        if (StringUtil.isNotEmpty(id)) {
            sysDataSourceDef = sysDataSourceDefManager.get(id);
        }
        writeSuccessData(response, sysDataSourceDef);
    }

    /**
     * <pre>
     * list页的后端
     * </pre>
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("listJson")
    @ResponseBody
    public PageJson listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        List<SysDataSourceDef> list = sysDataSourceDefManager.query(queryFilter);
        return new PageJson(list);
    }
}
