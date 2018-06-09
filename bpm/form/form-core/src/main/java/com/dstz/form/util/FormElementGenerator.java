package com.dstz.form.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.ThreadMapUtil;
import com.dstz.base.core.util.string.StringUtil;
import com.dstz.bus.api.constant.BusColumnCtrlType;
import com.dstz.bus.api.constant.BusTableRelType;
import com.dstz.bus.api.model.IBusTableRel;
import com.dstz.bus.api.model.IBusinessColumn;

/**
 * 自定义表单控件生成器<br>
 * input select radio dic 等等的生成<br>
 * @author jeff
 */
public class FormElementGenerator {
	private static FormElementGenerator generator = new FormElementGenerator();
	public static FormElementGenerator getInstance() { return generator; }

	/**
	 * 获取字段的html内容
	 * @param column
	 * @param relation
	 * @return HTML
	 */
	public String getColumn(IBusinessColumn column,IBusTableRel relation) {
		BusColumnCtrlType columnType = BusColumnCtrlType.getByKey(column.getCtrl().getType());
		String boCode = relation.getBusObj().getKey();
		ThreadMapUtil.put("boCode", boCode);
		ThreadMapUtil.put("relation", relation);
		
		switch (columnType) {
		
			case ONETEXT: return getColumnOnetext(column);
			
			case DATE: return getColumnDate(column);
			
			case DIC: return getColumnDic(column);
			
			//case IDENTITY: return getColumnIdentity(column);
			
			case MULTITEXT: return getColumnMultitext(column);
			
			case CHECKBOX: return getColumnCheckBox(column);
				
			case MULTISELECT: return getColumnSelect(column,true);	
			
			case RADIO: return getColumnRadio(column);	
			
			case SELECT: return getColumnSelect(column,false);	
			
			case FILE: return getColumnFile(column);	
				
			default: return "";
		
		}
		
	}
	
	private Element getElement(String type) {
		Document doc = Jsoup.parse("");
		Element element = doc.createElement(type);
		return element;
	}
	
	private void handleNgModel(Element element,IBusinessColumn column) {
		String boCode = (String) ThreadMapUtil.get("boCode");
		IBusTableRel relation  = (IBusTableRel) ThreadMapUtil.get("relation");
		//如果是多行子表
		if(relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
			element.attr("ng-model",column.getTable().getKey() + "." + column.getKey());
			return ;
		}
		
		element.attr("ng-model",getScopePath(relation)+ "." + column.getKey());
	}
	
	private void handlePermission(Element element,IBusinessColumn column) {
		element.attr("ab-basic-permission", getPermissionPath(column));
		element.attr("desc", column.getComment());
	}
	
	private String getPermissionPath(IBusinessColumn column) {
		String boCode = (String) ThreadMapUtil.get("boCode");
		return "permission."+ boCode + "." + column.getTable().getKey() + "." + column.getKey();
	}
	
	private void handleValidateRules(Element element,IBusinessColumn column) {
		String rulesStr = column.getCtrl().getValidRule();
		if(StringUtils.isEmpty(rulesStr)) return ;
		
		JSONArray rules = JSONArray.parseArray(rulesStr);
		//[{"name":"time","title":"时间"},{"name":"required","title":"必填"}]
		// to {time:true,required:true}
		JSONObject validateRule = new JSONObject();
		for (int i = 0; i < rules.size(); i++) {
			JSONObject rule = rules.getJSONObject(i);
			
			validateRule.put(rule.getString("name"), true);
		}
		
		if (column.isRequired()) {
			validateRule.put("required", true);
		}
		
		element.attr("ab-validate", validateRule.toJSONString());
		//为了validateRule提示
		element.attr("desc", column.getComment());
	}
	
	private String getColumnOnetext(IBusinessColumn column) {
		
		Element element = getElement("input").attr("type", "text").addClass("form-control");
		
		handleNgModel(element, column);
		handlePermission(element, column);
		handleValidateRules(element, column);
		
		return element.toString();
	}
	

	private String getColumnDate(IBusinessColumn column) {
		Element element = getElement("input").addClass("form-control");
		
		handleNgModel(element, column);
		handleValidateRules(element, column);
		handlePermission(element, column);
		
		String configStr = column.getCtrl().getConfig();
		if(StringUtil.isEmpty(configStr)) {
			throw new BusinessException(String.format("表【%s】日期字段  【%s】解析失败,配置信息不能为空", column.getTable().getKey(),column.getComment()));
		}
		element.attr("ab-date",JSON.parseObject(configStr).getString("format"));
		
		return element.toString();
	}
	
	private String getColumnDic(IBusinessColumn column) {
		Element element = getElement("span").attr("type", "text").addClass("input-div");
		
		handleNgModel(element, column);
		handleValidateRules(element, column);
		handlePermission(element, column);
		
		JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
		if(!config.containsKey("key")) {
			throw new BusinessException(String.format("表【%s】数据字典  字段【%s】解析失败,alias 配置信息不能为空", column.getTable().getKey(),column.getComment()));
		}
		
		element.attr("ab-combox", element.attr("ng-model"));
		element.attr("dict-key", config.getString("key"));
		
		return element.toString();
	}

	private String getColumnIdentity(IBusinessColumn column) {
		Element element = getElement("input").attr("type", "text").addClass("form-control");
		handleNgModel(element, column);
		handlePermission(element, column);
		handleValidateRules(element, column);
		
		JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
		if(!config.containsKey("alias")) {
			throw new BusinessException(String.format("表【%s】流水号  字段【%s】解析失败,alias 配置信息不能为空", column.getTable().getKey(),column.getComment()));
		}
		element.attr("ab-identity", config.getString("alias"));
		return element.toString();
	}

	private String getColumnMultitext(IBusinessColumn column) {
		Element element = getElement("textarea").attr("type", "text").addClass("form-control");
		
		handleNgModel(element, column);
		handlePermission(element, column);
		handleValidateRules(element, column);
		
		return element.toString();
	}

	private String getColumnCheckBox(IBusinessColumn column) {
		JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
		if(!config.containsKey("options")) {
			throw new BusinessException(String.format("表【%s】CheckBox 字段  【%s】解析失败,options 配置信息不能为空", column.getTable().getKey(),column.getComment()));
		}
		
		Element permissionElement = getElement("div");
		handleNgModel(permissionElement, column); 
		permissionElement.attr("ab-checked-permission", getPermissionPath(column));
		handleValidateRules(permissionElement, column);
		
		JSONArray options = config.getJSONArray("options");
		for (int i = 0; i < options.size(); i++) {
			JSONObject option = options.getJSONObject(i);
			
			Element element = permissionElement.appendElement("label").addClass("checkbox-inline");
			Element child  = element.appendElement("input").attr("type", "checkbox");
			child.attr("ab-checkbox", "");
			handleNgModel(child, column);
			child.attr("value", option.getString("key"));
			element.appendText(option.getString("txt"));
		}
		
		return permissionElement.toString();
	}

	private String getColumnRadio(IBusinessColumn column) {
		JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
		if(!config.containsKey("options")) {
			throw new BusinessException(String.format("表【%s】Radio 字段  【%s】解析失败,options 配置信息不能为空", column.getTable().getKey(),column.getComment()));
		}
		
		Element permissionElement = getElement("div");
		handleNgModel(permissionElement, column);
		permissionElement.attr("ab-checked-permission",  getPermissionPath(column));
		handleValidateRules(permissionElement, column);
		
		JSONArray options = config.getJSONArray("options");
		for (int i = 0; i < options.size(); i++) {
			JSONObject option = options.getJSONObject(i);
			
			Element element = permissionElement.appendElement("label").addClass("radio-inline");
			Element child  = element.appendElement("input").attr("type", "radio");
			handleNgModel(child, column);
			child.attr("value", option.getString("key"));
			element.appendText(option.getString("txt"));
		}
		
		return permissionElement.toString();
	}

	private String getColumnSelect(IBusinessColumn column,Boolean isMultiple) {
		JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
		if(!config.containsKey("options")) {
			throw new BusinessException(String.format("表【%s】Select 字段  【%s】解析失败,options 配置信息不能为空", column.getTable().getKey(),column.getComment()));
		}
		
		Element permissionElement = getElement("select").addClass("form-control");
		handleNgModel(permissionElement, column);
		permissionElement.attr("ab-checked-permission",getPermissionPath(column));
		handleValidateRules(permissionElement, column);
		
		if(isMultiple) {
			permissionElement.attr("multiple", "true");
		}
		
		JSONArray options = config.getJSONArray("options");
		for (int i = 0; i < options.size(); i++) {
			JSONObject option = options.getJSONObject(i);
			
			Element element = permissionElement.appendElement("option");
			element.attr("value", option.getString("key"));
			element.text(option.getString("txt"));
		}
		
		return permissionElement.toString();
	}

	private String getColumnFile(IBusinessColumn column) {
		//<a href="javascript:void(0)" class="btn btn-primary fa-upload" ab-upload ng-model="test">指令测试</a>
		Element element = getElement("a").attr("href", "javascript:void(0)").addClass("btn btn-primary fa-upload");
		element.attr("ab-upload","");
		handleNgModel(element, column);
		handlePermission(element, column);
		handleValidateRules(element, column);
		
		return element.toString();
	}
	
	/**
	 * <pre>
	 * 获取子表的路径
	 * 一直向上递归、若上级为主表、或者一对多的子表则停止。
	 * eg: mian.subaList[1].subbList[1].name  那subb的path为 subbList
	 * eg: mian.suba.subbList[1].name 那subb的path 为 main.suba.subbList
	 * eg: main.subaList[1].subb.name 那 subb的path 为 suba.subb.name
	 * eg: main.suba.subb.name 那subb的path为 mian.suba.subb.name
	 * 子表会存在单独作用域所以查询到子表那里即可
	 * </pre>
	 * @param relation
	 * @return
	 */
	public String getScopePath(IBusTableRel relation) {
		if(relation.getType().equals(BusTableRelType.MAIN.getKey())) {
			return "data."+relation.getBusObj().getKey();
		}
		
		StringBuffer sb = new StringBuffer();
		// 一对一是对象名字 
		sb.append(relation.getTableKey());
		// 如果是一对多则添加List
		if(relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())){
			sb.append("List");
		}
		
		getParentPath(relation.getParent(),sb);
		
		return sb.toString();
	}

	private void getParentPath(IBusTableRel parent,StringBuffer sb) {
		if(parent == null) return;
		//上级是一对多则将scope的name 返回
		if(parent.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
			sb.insert(0, parent.getTableKey()+".");
			return ;
		}
		//一对一子表
		if(parent.getType().equals(BusTableRelType.ONE_TO_ONE.getKey())) {
			sb.insert(0, parent.getTableKey()+".");
		}
		// 主表则是boCode
		if(parent.getType().equals(BusTableRelType.MAIN.getKey())) {
			sb.insert(0, "data."+parent.getBusObj().getKey()+".");
		}
		
		getParentPath(parent.getParent(), sb);
	}
	
	// id="boCode-tableKey" ab-basic-permission="boCode.table.tableKey" ...
	public String getSubAttrs(IBusTableRel rel) {
		StringBuffer sb = new StringBuffer();
		sb.append( " id="+"\""+ rel.getBusObj().getKey()+"-"+rel.getTableKey()+"\" ");
		
		//一对多的三层情况下弹框展示
		if(rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) 
				&& !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
			sb.append(" hide ");
		}
		
		return sb.toString() ;
	}
	
}
