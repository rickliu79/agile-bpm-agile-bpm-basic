package com.dstz.bo;
//package com.dstz.bo;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//
//import com.dstz.sys.api.groovy.IGroovyScriptEngine;
//import com.dstz.base.core.util.BeanUtils;
//import com.dstz.bo.api.constant.BodefConstants;
//import com.dstz.bo.api.instance.BOInstanceService;
//import com.dstz.bo.api.model.DataObject;
//
//public class DataObjectTest extends BOBaseTest{
//	@Resource
//	BOInstanceService bOInstanceService;
//	@Resource
//	IGroovyScriptEngine groovyScriptEngine;
//	
//	@Test
//	public void testRemove(){
//		String id = "10000023920039";
//		String code = "qjd";
//		DataObject dataObject = bOInstanceService.getDataObject(BodefConstants.SAVE_MODE_DB, id, code);
//		System.out.println("[dsaidsaodjioa]" + dataObject.getInstData());
//		//dataObject.removeKeyAndValue("qjd.sub_qjmx");
//		
//		String abc = "{\"qjbm\":\"深圳分公司\",\"qjrID\":\"10000019480033\",\"qjbmID\":\"10000019380259\",\"my_id_\":\"10000023920039\",\"lbsx\":[\"1\",\"2\"],\"qjyy\":\"去玩\",\"qjr\":\"Hunk2\"}";
//		bOInstanceService.megerDataObject(dataObject, abc);
//		System.out.println("[dsaidsaodjioa]" + dataObject.getInstData());
//	}
//	
////	@Test
//	public void testArray(){
//		String key = "10000016080015";
//		String data = "{\"phone\":15899966714,\"area\":\"\",\"name\":2,\"sub_workexp\":[{\"position\":\"岗位一\",\"start\":\"2014-09-23 00:00:00\",\"name\":\"\",\"parent_id_\":\"10000016120105\",\"my_id_\":\"10000016120106\",\"end\":\"2014-09-23 00:00:00\"},{\"position\":\"\",\"start\":\"2014-09-23 00:00:00\",\"name\":\"\",\"parent_id_\":\"10000016120105\",\"my_id_\":\"10000016120106\",\"end\":\"2014-09-23 00:00:00\"}],\"my_id_\":\"10000016120105\"}";
//		DataObject dataObject = bOInstanceService.createDataObject(key,data);
//		setDataObject(dataObject);
//		System.out.println(dataObject);
//	}
//	
//	/**
//	 * 修改BO数据。
//	 * @param fieldInitSettings
//	 * @param dataObject 
//	 * void
//	 */
//	private void setDataObject(DataObject dataObject){
////		String script = "{person.name} * 3";
////		String fieldName = "person.area";
////		Object data=getData(script, dataObject);
////		dataObject.set(fieldName, data);
//		
////		String script = "myTestScript.max({person.sub_workexp[]})";
////		String fieldName = "person.area";
////		Object data=getData(script, dataObject);
////		dataObject.set(fieldName, data);
//		
//		String script = "myTestScript.max({person.sub_workexp[].position})";
//		String fieldName = "person.area";
//		Object data=getData(script, dataObject);
//		dataObject.put(fieldName, data);
//		
////		String script2 = "myTestScript.max({person.sub_workexp[i].position})";
////		String fieldName2 = "person.sub_workexp[i].name";
////		columnCalc(fieldName2, script2, dataObject);
//	}
//	
//	//行运算
//	private void columnCalc(String fieldName,String script,DataObject dataObject){
//		Pattern regex = Pattern.compile("^(\\w+\\.\\w+)\\[i\\]\\.(\\w+)$");
//		Matcher regexMatcher = regex.matcher(fieldName);
//		if(regexMatcher.matches()){
//			String path = regexMatcher.group(1);
//			String field = regexMatcher.group(2);
//			
//			List<DataObject> dataObjects = dataObject.getDataObjects(path);
//			
//			if(BeanUtils.isNotEmpty(dataObjects)){
//				for (DataObject subDataObject : dataObjects) {
//					Object data = getData(script, subDataObject);
//					subDataObject.put(subDataObject.getBoDef().getName() + "." + field,data);
//				}
//				dataObject.setDataObjects(path,dataObjects);
//			}
//		}
//	}
//	
//	/**
//	 * 根据脚本产生计算结果。
//	 * <pre>
//	 * 	脚本如下：
//	 *  script.sum("[子表名.子表字段]");
//	 *  在脚本中获取字段bo如下。
//	 *  bo.get("字段名");
//	 * </pre>
//	 * @param script
//	 * @param dataObject
//	 * @return Object
//	 */
//	private Object getData(String script,DataObject dataObject){
//		Map<String, Object> map=new HashMap<String, Object>();
//		StringBuilder resultString = new StringBuilder();
//		Pattern regex = Pattern.compile("\\{(.*)\\}");
//		Matcher regexMatcher = regex.matcher(script);
//		while (regexMatcher.find()) {
//			String field=regexMatcher.group(1); 
//			String mapKey = buildScriptMap(field, dataObject, map);
//			if(BeanUtils.isNotEmpty(mapKey)){
//				regexMatcher.appendReplacement(resultString, mapKey);
//			}
//		}
//		regexMatcher.appendTail(resultString);
//		String newScript = resultString.toString();
//		Object obj= groovyScriptEngine.executeObject(newScript, map);
//		return obj;
//	}
//	
//	//构建groovy脚本执行的上下文
//	private String buildScriptMap(String boScript,DataObject dataObject,Map<String,Object> map){
//		Pattern regex = Pattern.compile("^\\w+\\.\\w+\\[i\\]\\.(\\w+)$");
//		Matcher regexMatcher = regex.matcher(boScript);
//		//行运算 a.b[i].c
//		while(regexMatcher.matches()){
//			String key = regexMatcher.group(1);
//			Object obj = dataObject.get(key);
//			map.put("bo_field_path_" + key, obj);
//			return "bo_field_path_" + key;
//		}
//		regex = Pattern.compile("^(\\w+\\.(\\w+))\\[\\]$");
//		regexMatcher = regex.matcher(boScript);
//		//取子模型所有记录 a.b[]
//		while(regexMatcher.matches()){
//			String path = regexMatcher.group(1);
//			String key = regexMatcher.group(2);
//			List<DataObject> dataObjects = dataObject.getDataObjects(path);
//			map.put("bo_field_path_" + key, dataObjects);
//			return "bo_field_path_" + key;
//		}
//		regex = Pattern.compile("^(\\w+\\.\\w+)\\[\\]\\.(\\w+)$");
//		regexMatcher = regex.matcher(boScript);
//		//取子模型某列所有记录 a.b[].c
//		while(regexMatcher.matches()){
//			String path = regexMatcher.group(1);
//			String key = regexMatcher.group(2);
//			List<Object> list = new ArrayList<Object>();
//			List<DataObject> dataObjects = dataObject.getDataObjects(path);
//			for (DataObject subDataObject : dataObjects) {
//				Object obj = subDataObject.get(key);
//				list.add(obj);
//			}
//			map.put("bo_field_path_" + key, list);
//			return "bo_field_path_" + key;
//		}
//		regex = Pattern.compile("^\\w+\\.(\\w+)$");
//		regexMatcher = regex.matcher(boScript);
//		//取主模型字段 a.b
//		while(regexMatcher.matches()){
//			String key = regexMatcher.group(1);
//			Object obj = dataObject.get(key);
//			map.put("bo_field_path_" + key, obj);
//			return "bo_field_path_" + key;
//		}
//		return null;
//	}
//}
