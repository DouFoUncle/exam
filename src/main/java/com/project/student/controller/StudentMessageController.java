package com.project.student.controller;

import cn.hutool.core.util.IdcardUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.Message;
import com.project.commons.model.User;
import com.project.commons.service.CourseService;
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
 * 学生留言相关控制器
 */
@RestController
@RequestMapping("/studentMessage")
@AllArgsConstructor
public class StudentMessageController {

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
            IPage<Message> data = messageService.getListByPage(message, page, limit);
            return new ResultMessage<List<Message>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 新增或修改信息
     * @param message
     * @return
     */
    @PostMapping("saveOrUpdate")
    public ResultMessage<User> saveOrUpdate(Message message, HttpServletRequest request) {
        try {
            // 判断是新增还是修改
            if(message.getId() == null) {
                User user = ServletUtils.getAdminInfo(request);
                // 新增赋值默认信息
                message.setUserId(user.getId());
                message.setCreateDate(new Date());
                message.setStudentName(user.getRealName());
            }
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

    /**
     * 删除信息
     * @return
     */
    @DeleteMapping("deleteInfo")
    public ResultMessage<User> deleteInfo(String ids) {
        try {
            // 调用方法
            boolean result = messageService.deleteInfo(ids);
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
