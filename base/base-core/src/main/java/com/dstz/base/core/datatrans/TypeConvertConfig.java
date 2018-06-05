package com.dstz.base.core.datatrans;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 描述：转换器类型注册容器。
 * </pre>
 */
public class TypeConvertConfig {

    private Map<Class, ITypeConvert> map = new HashMap<Class, ITypeConvert>();

    public void regeisterConvert(Class cls, ITypeConvert convert) {
        map.put(cls, convert);
    }

    public Map<Class, ITypeConvert> getConverts() {
        return map;
    }

}
