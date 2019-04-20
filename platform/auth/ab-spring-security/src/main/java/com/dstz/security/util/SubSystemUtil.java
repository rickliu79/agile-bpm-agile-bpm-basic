package com.dstz.security.util;

import com.dstz.base.core.util.StringUtil;
import com.dstz.base.rest.util.CookieUitl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubSystemUtil {

    /**
     * 获取上下文系统ID
     *
     * @param req
     * @return
     */
    public static String getSystemId(HttpServletRequest req) {
<<<<<<< HEAD
        String systemAlias = CookieUitl.getValueByName("system", req);
        if (StringUtil.isEmpty(systemAlias)) return "agilebpm";
        return systemAlias;
    }

    /**
     * 设置系统id。
     *
     * @param req
     * @param response
     * @param systemId
     */
    public static void setSystemId(HttpServletRequest req, HttpServletResponse response, String systemAlias) {
        CookieUitl.addCookie("system", systemAlias,CookieUitl.cookie_no_expire);
=======
        String systemId = CookieUitl.getValueByName("systemId", req);
        if (StringUtil.isEmpty(systemId)) return "1";
        return systemId;
    }

    /**
     * 设置系统id。
     *
     * @param req
     * @param response
     * @param systemId
     */
    public static void setSystemId(HttpServletRequest req, HttpServletResponse response, String systemId) {
        CookieUitl.addCookie("systemId", systemId,CookieUitl.cookie_no_expire);
>>>>>>> branch 'master' of https://gitee.com/agile-bpm/agile-bpm-basic.git
    }
}
