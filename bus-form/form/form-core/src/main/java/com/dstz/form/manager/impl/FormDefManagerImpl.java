package com.dstz.form.manager.impl;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.FileUtil;
import com.dstz.base.core.util.PropertyUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.form.dao.FormDefDao;
import com.dstz.form.manager.FormDefManager;
import com.dstz.form.model.FormDef;
import com.dstz.sys.api2.model.ISysTreeNode;
import com.dstz.sys.api2.service.ISysTreeNodeService;

/**
 * 表单 Manager处理实现类
 *
 * @author aschs
 * @email aschs@qq.com
 * @time 2018-03-19 20:30:46
 */
@Service("formDefManager")
public class FormDefManagerImpl extends BaseManager<String, FormDef> implements FormDefManager {
	@Resource
	FormDefDao formDefDao;
	@Autowired
	ISysTreeNodeService sysTreeNodeService;

	@Override
	public FormDef getByKey(String key) {
		return formDefDao.getByKey(key);
	}

	@Override
	public void saveBackupHtml(FormDef formDef) {
		String formDefPath = PropertyUtil.getFormDefBackupPath();
		if (StringUtil.isEmpty(formDefPath)) {
			return;
		}

		ISysTreeNode node = sysTreeNodeService.getById(formDef.getGroupId());
		String fileName = formDefPath + File.separator + node.getKey() + File.separator + formDef.getKey() + ".html";
		FileUtil.writeFile(fileName, formDef.getHtml());
	}

	@Override
	public String getBackupHtml(FormDef formDef) {
		String formDefPath = PropertyUtil.getFormDefBackupPath();
		if (StringUtil.isNotEmpty(formDefPath)) {
			ISysTreeNode node = sysTreeNodeService.getById(formDef.getGroupId());
			String fileName = formDefPath + File.separator + node.getKey() + File.separator + formDef.getKey() + ".html";
			formDef.setHtml(FileUtil.readFile(fileName));
		}

		return formDef.getHtml();
	}

	public static void main(String[] args) {
		String str = FileUtil.readFile("D:\\projects\\dream\\agile-bpm\\modules\\agile-bpm-platform-new\\src\\main\\webapp\\form\\mrfl\\nh1.html");
		System.out.println(str);
	}
}
