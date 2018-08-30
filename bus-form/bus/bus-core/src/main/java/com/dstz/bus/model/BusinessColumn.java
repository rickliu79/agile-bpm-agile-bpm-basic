package com.dstz.bus.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dstz.base.api.constant.ColumnType;
import com.dstz.base.api.model.IBaseModel;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.core.util.time.DateFormatUtil;
import com.dstz.base.db.model.table.Column;
import com.dstz.bus.api.model.IBusinessColumn;

/**
 * <pre>
 * 业务字段
 * </pre>
 *
 * @author aschs
 *
 */
public class BusinessColumn extends Column implements IBaseModel, IBusinessColumn {
	@NotEmpty
	private String id;
	/**
	 * 列key
	 */
	@NotEmpty
	private String key;
	/**
	 * 表Id
	 */
	@NotEmpty
	private String tableId;

	// 以下字段不进行持久化
	/**
	 * 字段控件
	 */
	@Valid
	private BusColumnCtrl ctrl;
	private BusinessTable table;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public BusColumnCtrl getCtrl() {
		return ctrl;
	}

	public void setCtrl(BusColumnCtrl ctrl) {
		this.ctrl = ctrl;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	@Override
	public BusinessTable getTable() {
		return table;
	}

	public void setTable(BusinessTable table) {
		this.table = table;
	}

	@Override
	public Object initValue() {
		return value(defaultValue);
	}

	@Override
	public Object value(String str) {
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		Object value = null;
		if (ColumnType.VARCHAR.equalsWithKey(type) || ColumnType.CLOB.equalsWithKey(type)) {
			value = str;
		} else if (ColumnType.NUMBER.equalsWithKey(type)) {
			BigDecimal bigDecimal = new BigDecimal(str);
			// 保留精度小数，且四舍五入
			value = bigDecimal.setScale(this.getDecimal(), RoundingMode.HALF_UP);
		} else if (ColumnType.DATE.equalsWithKey(type)) {
			JSONObject config = JSON.parseObject(this.getCtrl().getConfig());
			value = DateFormatUtil.parse(str, config.getString("format"));
		}

		return value;
	}

	@Override
	public Date getCreateTime() {
		return null;
	}

	@Override
	public void setCreateTime(Date createTime) {

	}

	@Override
	public String getCreateBy() {
		return null;
	}

	@Override
	public void setCreateBy(String createBy) {

	}

	@Override
	public Date getUpdateTime() {
		return null;
	}

	@Override
	public void setUpdateTime(Date updateTime) {

	}

	@Override
	public String getUpdateBy() {
		return null;
	}

	@Override
	public void setUpdateBy(String updateBy) {

	}

}
