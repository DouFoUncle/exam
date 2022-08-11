package com.project.commons.controller;

import cn.hutool.crypto.SecureUtil;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.User;
import com.project.commons.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private UserService userService;

    /**
     * 管理员登录验证
     * @param user
     * @return
     */
    @PostMapping("userLogin")
    public ResultMessage<Map<String, Object>> loginVerify(User user, HttpSession session) {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>(16);
            // 调用业务层管理员登录方法
            User loginUser = userService.selectByLogin(user);
            if(loginUser == null) {
                return ResultMessage.warn(1, "用户名或密码有误！");
            }
            session.setAttribute("userInfo", loginUser);
            // 判断用户类型, 设置登陆成功后的跳转路径
            if("0".equals(loginUser.getUserType())) {
                resultMap.put("skipUrl", "admin/index");
            } else if("1".equals(loginUser.getUserType())) {
                resultMap.put("skipUrl", "teacher/index");
            } else if("2".equals(loginUser.getUserType())) {
                resultMap.put("skipUrl", "student/index");
            }
            return new ResultMessage<Map<String, Object>>(0, "登录成功！", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

}
