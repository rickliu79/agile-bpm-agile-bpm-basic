package com.dstz.demo.core.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dstz.base.manager.impl.BaseManager;
import com.dstz.demo.core.dao.DemoDao;
import com.dstz.demo.core.manager.DemoManager;
import com.dstz.demo.core.manager.DemoSubManager;
import com.dstz.demo.core.model.Demo;
import com.dstz.demo.core.model.DemoSub;
/**
 * 案例 Manager处理实现类
 * @author aschs
 * @email aschs@qq.com
 * @time 2018-08-24 18:06:04
 */
@Service("demoManager")
public class DemoManagerImpl extends BaseManager<String, Demo> implements DemoManager{
	@Resource
	DemoDao demoDao;
	@Resource
	DemoSubManager demoSubMananger;

	@Override
	public Demo get(String entityId) {
		Demo demo = super.get(entityId);
		if(demo == null) return null;
		
		List<DemoSub> demoSublist = demoSubMananger.getByFk(entityId);
		demo.setDemoSubList(demoSublist);
		
		return demo;
	}
	
	
}
