package com.dstz.platform.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 编码过滤器， 设置当前系统的编码。
 *
 * @author dstz
 */
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";
    private String contentType = "text/html;charset=UTF-8";

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse httpresponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) httpresponse;
        HttpServletRequest req = (HttpServletRequest) request;
        
        
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", -1);
        
        //此处我们忽略跨域问题由 @RefererCsrfFilter.java 来处理
        String origin = req.getHeader("Origin");
        
        // 支持的Header
        response.setHeader("Access-Control-Allow-Headers", "accept, origin, content-type");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET");
        chain.doFilter(request, response);
        
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        String _encoding = config.getInitParameter("encoding");
        String _contentType = config.getInitParameter("contentType");
        // String ext=config.getInitParameter("ext");
        if (_encoding != null)
            encoding = _encoding;
        if (_contentType != null)
            contentType = _contentType;

    }

}
