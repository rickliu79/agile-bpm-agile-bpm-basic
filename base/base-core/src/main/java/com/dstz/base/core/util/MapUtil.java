package com.dstz.base.core.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static Map<String, Object> buildMap(String key, Object val) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, val);
        return map;
    }
}
