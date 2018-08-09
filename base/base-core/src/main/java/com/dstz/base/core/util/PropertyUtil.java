package com.dstz.base.core.util;

import com.dstz.base.core.spring.CustPropertyPlaceholderConfigurer;

/**
 * 属性工具类。
 */
public class PropertyUtil {

    //private static Environment env = null;
    private static CustPropertyPlaceholderConfigurer custPlaceHolder = null;

    /**
     * 根据键值获取属性文件中配置的值。
     *
     * @param property     属性键值
     * @param defaultValue 默认值
     * @return String
     */
    public static String getProperty(String property, String defaultValue) {
        getEnvironment();

        String v = custPlaceHolder.getValue(property);
        if (StringUtil.isEmpty(v)) {
            return defaultValue;
        }

        return v;
    }


    private static synchronized void getEnvironment() {
        if (custPlaceHolder == null) {
            custPlaceHolder = (CustPropertyPlaceholderConfigurer) AppUtil.getBean("custPlaceHolder");
        }
    }


    /**
     * 获取系统属性值。
     *
     * @param property 属性键值。
     * @return String
     */
    public static String getProperty(String property) {
        return getProperty(property, "");
    }

    /**
     * 获取整形值。
     *
     * @param property
     * @return
     */
    public static Integer getIntProperty(String property) {
        String str = getProperty(property, "");
        if (StringUtil.isEmpty(str)) {
            return 0;
        }
        return Integer.valueOf(str);
    }

    /**
     * 获取整形值，可以指定默认值。
     *
     * @param property
     * @param defaultValue
     * @return
     */
    public static Integer getIntProperty(String property, Integer defaultValue) {
        String str = getProperty(property, "");
        if (StringUtil.isEmpty(str)) {
            return defaultValue;
        }
        return Integer.valueOf(str);
    }


    /**
     * 获取布尔值。如果配置值为 true，则返回true。这里不区分大小写，可以配置成True等。
     *
     * @param property
     * @return
     */
    public static boolean getBoolProperty(String property) {
        String str = getProperty(property, "");
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        return str.equalsIgnoreCase("true");
    }

    /**
     * 获取当前数据库的类型。
     *
     * @return
     */
    public static String getJdbcType() {
        String str = getProperty("jdbc.dbType");
        return str;
    }
    
    /**
     * <pre>
     * 获取表单备份地址
     * </pre>
     * @return
     */
    public static String getFormDefBackupPath() {
    	return getProperty("formDefBackupPath");
    }

}
