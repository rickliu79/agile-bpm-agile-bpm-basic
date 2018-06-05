package com.dstz.sys2.permission.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dstz.sys.api2.permission.IPermissionCalculator;
/**
 * <pre>
 * 描述：组
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年5月8日
 * 版权:summer
 * </pre>
 */
@Service
public class GroupPermissionCalculator implements IPermissionCalculator {
	/**
	 * 线程变量ThreadMapUtil中关于当前权限解析器的线程变量
	 */
	private static String threadMapKey = "com.dstz.sys.permission.impl.GroupPermission";
	
    @Override
    public String getTitle() {
        return "组";
    }

    @Override
    public String getType() {
        return "group";
    }

	@Override
	public boolean haveRights(JSONObject json) {
		return false;
	}
	
}
