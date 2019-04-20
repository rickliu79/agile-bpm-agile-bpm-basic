package com.dstz.sys.api.service;

import java.util.List;

import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.model.SysIdentity;

public interface SysIdentityConvert {
	
	/**
	 *  identity type 必须为 user
	 * @param identity
	 * @return
	 */
	public IUser convert2User(SysIdentity identity);
	
<<<<<<< HEAD
	public List<? extends IUser> convert2Users(SysIdentity identity);
	
	public List<? extends IUser> convert2Users(List<SysIdentity> identity);
=======
	public List<IUser> convert2Users(SysIdentity identity);
	
	public List<IUser> convert2Users(List<SysIdentity> identity);
>>>>>>> branch 'master' of https://gitee.com/agile-bpm/agile-bpm-basic.git
	
}
