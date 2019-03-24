package com.dstz.org.rest.controller;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dstz.base.rest.BaseController;

import com.dstz.base.api.aop.annotion.CatchErr;

import com.dstz.org.core.manager.OrgRelationManager;
import com.dstz.org.core.model.OrgRelation;


/**
 * 用户组织关系 控制器类<br/>
 * @author  Jeff
 * </pre>
 */
@RestController
@RequestMapping("/org/orgRelation")
public class OrgRelationController extends BaseController<OrgRelation>{
	@Resource
	OrgRelationManager orgRelationManager;
	
	
	@Override
	protected String getModelDesc() {
		return "用户组织关系";
	}
	   
}
