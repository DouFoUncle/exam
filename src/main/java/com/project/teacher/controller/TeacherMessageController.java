package com.project.teacher.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.Message;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.User;
import com.project.commons.service.MessageService;
import com.project.commons.util.ServletUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 教师留言相关控制器
 */
@RestController
@RequestMapping("/teacherMessage")
@AllArgsConstructor
public class TeacherMessageController {

    private MessageService messageService;

    /**
     * 分页获取表格数据
     * @param message
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<Message>> getListByPage(Integer page, Integer limit, Message message, HttpServletRequest request) {
        try {
            // 调用分页查询方法
            // 只查询和本教师有关的信息
            message.setUserTeacherId(ServletUtils.getAdminIdInfo(request));
            IPage<Message> data = messageService.getListByPage(message, page, limit);
            return new ResultMessage<List<Message>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 回复留言
     * @param message
     * @return
     */
    @PostMapping("updateInfo")
    public ResultMessage<User> updateInfo(Message message, HttpServletRequest request) {
        try {
            User user = ServletUtils.getAdminInfo(request);
            message.setTeacherName(user.getRealName());
            message.setReplyDate(new Date());
            // 调用方法
            boolean result = messageService.saveOrUpdate(message);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<User>(0, "操作成功！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }
}
