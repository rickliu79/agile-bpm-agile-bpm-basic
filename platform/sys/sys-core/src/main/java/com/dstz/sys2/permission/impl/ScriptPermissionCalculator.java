package com.dstz.sys2.permission.impl;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dstz.base.core.util.BeanUtils;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api2.permission.IPermissionCalculator;
import com.dstz.sys.util.ContextUtil;

/**
 * <pre>
 * 描述：脚本
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年5月8日
 * 版权:summer
 * </pre>
 */
@Service
public class ScriptPermissionCalculator implements IPermissionCalculator {
	@Resource
	IGroovyScriptEngine groovyScriptEngine;

	@Override
	public String getTitle() {
		return "脚本";
	}

	@Override
	public String getType() {
		return "script";
	}

	@Override
	public boolean haveRights(JSONObject json) {
		String script = json.getString("script");
		Set<String>  set = (Set<String>) groovyScriptEngine.executeObject(script, null);
		if (BeanUtils.isEmpty(set)) {
			return false;
		}
		String userId = ContextUtil.getCurrentUserId();
		if (set.contains(userId)) {
			return true;
		}
		return false;
	}

}
