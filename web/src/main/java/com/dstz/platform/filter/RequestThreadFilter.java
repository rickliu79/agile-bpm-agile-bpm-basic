package com.dstz.platform.filter;

import com.dstz.base.db.datasource.DbContextHolder;
import com.dstz.base.rest.util.RequestContext;
import com.dstz.bpm.act.cache.ActivitiDefCache;
import com.dstz.bpm.api.engine.context.BpmContext;
import com.dstz.sys.util.ContextUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 向 http请求中，设置当前request信息,以便在线程中使用request
 * 清除http中线程变量
 */
public class RequestThreadFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            cleanThreadLocal();
            RequestContext.setHttpServletRequest((HttpServletRequest) request);
            RequestContext.setHttpServletResponse((HttpServletResponse) response);
            chain.doFilter(request, response);
        } finally {
            cleanThreadLocal();
        }
    }

    private void cleanThreadLocal() {
        RequestContext.clearHttpReqResponse();
        ContextUtil.clearAll();
        DbContextHolder.setDefaultDataSource();
        ActivitiDefCache.clearLocal();
        BpmContext.cleanTread();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
