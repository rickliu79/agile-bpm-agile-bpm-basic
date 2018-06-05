package com.dstz.form.api.constant;

/**
 * <pre>
 * 描述：FormTemplate的type枚举
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年3月8日 上午10:25:38
 * 版权:summer
 * </pre>
 */
public enum FormTemplateType {
	/**
	 * 主表模板
	 */
	MAIN("main", "主表模板"),
	/**
	 * 手机端主表模板
	 */
	MOBILE_MAIN("mobileMain", "手机端主表模板"),
	/**
	 * 子表模板
	 */
	SUB_TABLE("subTable", "子表模板"),
	/**
	 * 手机端子表模板
	 */
	MOBILE_SUB_TABLE("mobileSubTable", "手机端子表模板"),
	/**
	 * 宏模板
	 */
	MACRO("macro", "宏模板"),
	/**
	 * 手机端宏模板
	 */
	MOBILE_MACRO("mobileMacro", "手机端宏模板"),
	/**
	 * 自定义SQL查询模版
	 */
	QUERY_DATA_TEMPLATE("queryDataTemplate", "自定义SQL查询模版"),
	/**
	 * 表管理模板 
	 */
	TABLE_MANAGE("tableManage","表管理模板 "),
	/**
	 * 列表模版
	 */
	LIST("list","列表模版"),
	/**
	 * 明细模版
	 */
	DETAIL("detail","明细模版"),
	/**
	 * 表管理模板
	 */
	DATA_TEMPLATE("dataTemplate","表管理模板")
	;
	/**
	 * key
	 */
	private String key;
	/**
	 * 描述
	 */
	private String desc;

	private FormTemplateType(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * <pre>
	 * 根据key来判断是否跟当前一致
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	public boolean equalsWithKey(String key) {
		return this.key.equals(key);
	}
}
