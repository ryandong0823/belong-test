package com.belong.telecom.filter;

import com.belong.telecom.biz.ResultInfo;
import com.belong.telecom.service.ISecurityCheckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

@WebFilter(urlPatterns = {"/customer/*", "/phone_numbers/*"}, filterName = "AuthenticationFilter")
public class AuthenticationFilter implements Filter {
    @Autowired
    private ISecurityCheckService securityCheckService;

    public AuthenticationFilter() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "authToken, Origin, X-Requested-With, Content-Type, Accept");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json; charset=utf-8");
        String token = req.getHeader("authToken");
        if (token != null && !"".equals(token)) {
            if (!this.securityCheckService.authenticated(token)) {
                resp.setStatus(403);
                servletResponse.getOutputStream().write((new ObjectMapper()).writer().writeValueAsString(ResultInfo.builder().success(false).message("Unauthenticated User").build()).getBytes(StandardCharsets.UTF_8));
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            resp.setStatus(401);
            servletResponse.getOutputStream().write((new ObjectMapper()).writer().writeValueAsString(ResultInfo.builder().success(false).message("Token missed").build()).getBytes(StandardCharsets.UTF_8));
        }
    }
}
