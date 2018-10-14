package com.dstz.bus.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.bus.api.model.IBusinessObject;
import com.dstz.bus.api.service.IBusinessObjectService;
import com.dstz.bus.manager.BusinessObjectManager;
import com.dstz.bus.service.impl.AbDataSourceTransactionManager;
import com.dstz.bus.util.BusinessObjectCacheUtil;

/**
 * <pre>
 *  
 * 描述：IBusinessObjectService实现类
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年3月26日 下午5:53:29
 * 版权:summer
 * </pre>
 */
@Service
public class BusinessObjectService implements IBusinessObjectService {
	@Autowired
	BusinessObjectManager businessObjectManager;

	@Override
	public IBusinessObject getByKey(String key) {
		return businessObjectManager.getByKey(key);
	}

	@Override
	public IBusinessObject getFilledByKey(String key) {
		return businessObjectManager.getFilledByKey(key);
	}

	@Override
	public List<JSONObject> boTreeData(String key) {
		return businessObjectManager.boTreeData(key);
	}

	@Override
	public void prepareForAbTransaction(Set<String> boKeys) {
		// 在事务内了，不需要如下操作，因为这是在进入事务前操作的
		if ((boolean) ThreadMapUtil.getOrDefault(AbDataSourceTransactionManager.AB_TRANSACTION_MANAGER_EXIST, false)) {
			return;
		}

		Set<String> keys = new HashSet<>();
		for (String key : boKeys) {
			Set<String> dsKeys = BusinessObjectCacheUtil.getDataSourcesKeys(key);
			if (dsKeys == null) {
				dsKeys = getFilledByKey(key).calDataSourceKeys();
				BusinessObjectCacheUtil.putDataSourcesKeys(key, dsKeys);
			}
			keys.addAll(dsKeys);
		}
		ThreadMapUtil.put(AbDataSourceTransactionManager.AB_TRANSACTION_MANAGER_DATASOURCE_KEYS, keys);
	}
}
