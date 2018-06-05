package com.dstz.platform.servlet;

import com.dstz.base.rest.util.HttpUtil;
import com.dstz.base.rest.util.RequestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 跨域代理请求资源servlet：
 * 通过这个代理，根据传入的URL返回第三方系统的数据。
 * 传入的参数为url。
 * url：支持三种方式：
 * 1.本应用内的的页面。
 * 2.支持支持http和https连接。
 */
public class CrossOriginRequestProxyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CrossOriginRequestProxyServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = RequestUtil.getString(request, "url");
        PrintWriter writer = response.getWriter();
        String urlLower = url.toLowerCase();
        if (urlLower.startsWith("https://")) {
            String str = HttpUtil.sendHttpsRequest(url, "", "GET");
            writer.print(str);
        } else if (urlLower.startsWith("http://")) {
            String str = HttpUtil.getContentByUrl(url, "utf-8");
            writer.print(str);
        } else {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

}
