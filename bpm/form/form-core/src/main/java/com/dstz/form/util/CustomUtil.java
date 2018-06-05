/**
 * 描述：TODO
 * 包名：com.dstz.platform.util
 * 文件名：CustomUtil.java
 */
package com.dstz.form.util;

import com.alibaba.fastjson.JSONObject;
import com.dstz.base.core.util.AppUtil;
import com.dstz.base.core.util.string.StringPool;
import com.dstz.base.core.util.string.StringUtil;
import com.dstz.base.core.util.time.DateFormatUtil;
import com.dstz.base.core.util.time.TimeUtil;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;

import java.util.Map;

/**
 * <pre> 
 * 描述：自定义查询和对话框的相关工具类
 * </pre>
 */
public class CustomUtil {
    public static final String DEFAULTTYPE_INPUT_PARAM = "1";
    public static final String DEFAULTTYPE_FIX_VALUE = "2";
    public static final String DEFAULTTYPE_SCRIPT = "3";
    public static final String DEFAULTTYPE_INPUT_TRENDS = "4";

    /**
     * 构建value
     *
     * @param field
     *            字段名
     * @param defaultType
     *            默认类型
     * @param defaultValue
     *            默认值
     * @param params
     *            页面传来的参数
     * @return Object
     * @exception
     * @since 1.0.0
     */
    public static Object buildValue(String field, String defaultType, String defaultValue, Map<String, Object> params) {
        Object value = null;
        if (defaultType.equals(DEFAULTTYPE_INPUT_PARAM) || defaultType.equals(DEFAULTTYPE_INPUT_TRENDS)) {// 参数输入
            if (params.containsKey(field) && StringUtil.isNotEmpty(params.get(field).toString())) {
                value = params.get(field).toString();
            }
        } else if (defaultType.equals(DEFAULTTYPE_FIX_VALUE)) {// 固定值
            value = defaultValue;
        } else if (defaultType.equals(DEFAULTTYPE_SCRIPT)) {// 脚本
            String script = defaultValue;
            if (StringUtil.isNotEmpty(script)) {
                IGroovyScriptEngine groovyScriptEngine = (IGroovyScriptEngine) AppUtil.getBean(IGroovyScriptEngine.class);
                value = groovyScriptEngine.executeObject(script, null);
            }
        }
        return value;
    }

    /**
     * 处理between的value，把它按"|"分割成startDate,endDate的json
     * @param value
     * @return
     * Object
     * @exception
     * @since 1.0.0
     */
    public static Object handleDateBetweenValue(Object value) {
        String[] aryDate = value.toString().split("\\|");
        String startDate = aryDate[0];
        String endDate = aryDate[1];
        java.util.Date d = DateFormatUtil.parse(endDate, StringPool.DATE_FORMAT_DATETIME, StringPool.DATE_FORMAT_DATE, StringPool.DATE_FORMAT_TIMESTAMP);
        endDate = DateFormatUtil.format(TimeUtil.getNextDays(d, 1), StringPool.DATE_FORMAT_DATETIME);
        JSONObject tjo = new JSONObject();
        tjo.put("startDate", startDate);
        tjo.put("endDate", endDate);
        return tjo;
    }
}
