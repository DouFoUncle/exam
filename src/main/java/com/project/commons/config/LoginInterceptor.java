package com.project.commons.config;

import com.project.commons.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @user: Mr.Wang
 * @date: 2021/9/28
 * @time: 17:00
 * @comment: 登陆拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        User user = (User) request.getSession().getAttribute("userInfo");
        if(user == null) {
            // 判断请求是否是AJax请求
            if(isAjaxRequest(request)) {
                PrintWriter writer = response.getWriter();
                // 是Ajax请求， 写出数据
                writer.write("{\"result\":\"IsAdminAjax\"}");
                return false;
            }
            // 不是Ajax请求，直接跳转
            response.sendRedirect(request.getContextPath()+"/system/toTimeOutPage");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 判断请求是否是Ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
