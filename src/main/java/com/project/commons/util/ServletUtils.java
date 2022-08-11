package com.project.commons.util;

import com.project.commons.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 斗佛Uncle
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
public class ServletUtils {

    /**
     * 获取项目网络地址
     * http://ip:port/projectName/
     * @param request
     * @return
     */
    public static String getProjectHttpUrl(HttpServletRequest request) {
        // 获取项目名称
        String contextPath = request.getContextPath();
        // 获取协议
        String scheme = request.getScheme();
        // 获取ip
        String serverName = request.getServerName();
        // 获取端口
        int serverPort = request.getServerPort();
        String showPath = scheme + "://" + serverName + ":" + serverPort + contextPath + "/";
        return showPath;
    }

    /**
     * 获取登陆用户的信息
     * @param request
     * @return
     */
    public static User getAdminInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User webUserInfo = (User) session.getAttribute("userInfo");
        return webUserInfo;
    }

    /**
     * 获取当前前台已登录用户的信息
     * @param request
     * @return
     */
    public static Integer getAdminIdInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User webUserInfo = (User) session.getAttribute("userInfo");
        if(webUserInfo == null) {
            return null;
        }
        return webUserInfo.getId();
    }

}
