package com.dstz.base.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.model.IBaseModel;
import com.dstz.base.api.query.Direction;
import com.dstz.base.api.query.FieldSort;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageJson;
import com.dstz.base.db.model.query.DefaultFieldSort;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.manager.Manager;
import com.dstz.base.rest.util.RequestUtil;
import com.github.pagehelper.Page;

/**
 * <pre>
 * 描述：controller的基础类
 * 包含了常用的增删查改的方法，需要定制化可自行覆盖
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年4月3日 下午8:41:30
 * 版权:summer
 * </pre>
 */
public abstract class BaseController<T extends IBaseModel> extends GenericController{
	
    protected abstract String getModelDesc();

    @Autowired
    Manager<String,T> manager;
    
    /**
     * 分页列表
     */
    @RequestMapping("listJson")
    public PageJson listJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryFilter queryFilter = getQueryFilter(request);
        Page<T> pageList = (Page<T>) manager.query(queryFilter);
        return new PageJson(pageList);
    }

    /**
     * 获取对象
     */
    @RequestMapping("get")
    @CatchErr
    public ResultMsg<T> get(@RequestParam String id) throws Exception {
       T t = manager.get(id);
       return getSuccessResult(t);
    }

    /**
     * 保存
     */
    @RequestMapping("save")
    @CatchErr
    public ResultMsg<String> save(@RequestBody T t) throws Exception {
        String desc;
        if (StringUtil.isEmpty(t.getId())) {
            desc = "添加%s成功";
            manager.create(t);
        } else {
            manager.update(t);
            desc = "更新%s成功";
        }
        return getSuccessResult(String.format(desc, getModelDesc()));
    }

    /**
     * 批量删除
     */
    @RequestMapping("remove")
    @CatchErr
    public ResultMsg<String> remove(@RequestParam String id) throws Exception {
         String[] aryIds = StringUtil.getStringAryByStr(id);
         manager.removeByIds(aryIds);
         return getSuccessResult(String.format("删除%s成功", getModelDesc()));
    }
  
}