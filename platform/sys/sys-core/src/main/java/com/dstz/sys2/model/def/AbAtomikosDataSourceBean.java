package com.dstz.sys2.model.def;

import java.util.Properties;

import com.atomikos.jdbc.AtomikosDataSourceBean;

/**
 * <pre>
 * 描述：为了配合系统数据源的构造而做的AtomikosDataSourceBean
 * 有些必要参数在properties中，在页面很难设置，所以在这里提到了最外层配置
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年5月29日
 * 版权:summer
 * </pre>
 */
public class AbAtomikosDataSourceBean extends AtomikosDataSourceBean {
	private String url;
	private String username;
	private String password;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		setProperty("url", url);
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		setProperty("username", username);
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		setProperty("password", password);
		this.password = password;
	}

	private void setProperty(String key, String val) {
		Properties properties = getXaProperties();
		if (properties == null) {
			properties = new Properties();
			setXaProperties(properties);
		}
		properties.setProperty(key, val);
	}
}
