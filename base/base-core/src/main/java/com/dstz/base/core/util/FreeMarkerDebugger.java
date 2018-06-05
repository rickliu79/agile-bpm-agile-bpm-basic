package com.dstz.base.core.util;

public class FreeMarkerDebugger {
    public Object debug(Object... objs) {
        System.out.println("-------------------------->");
        for (Object obj : objs) {
            System.out.println(obj);
        }
        System.out.println("-------------------------->");
        return "";
    }
}
