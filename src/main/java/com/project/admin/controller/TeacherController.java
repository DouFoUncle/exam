package com.project.admin.controller;

import cn.hutool.core.util.IdcardUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.User;
import com.project.commons.service.UserService;
import com.project.commons.util.ServletUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 教师控制器
 */
@RestController
@RequestMapping("/adminTeacher")
@AllArgsConstructor
public class TeacherController {

    private UserService userService;

    /**
     * 分页获取表格数据
     * @param user
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<User>> getListByPage(Integer page, Integer limit, User user, HttpServletRequest request) {
        try {
            // 查询教师信息
            user.setUserType("1");
            // 调用分页查询方法
            IPage<User> data = userService.getListByPage(user, page, limit);
            return new ResultMessage<List<User>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 新增或修改信息
     * @param user
     * @return
     */
    @PostMapping("saveOrUpdate")
    public ResultMessage<User> saveOrUpdate(User user) {
        try {
            // 判断身份证号是否合法
            if(!IdcardUtil.isValidCard(user.getIdCardNum())) {
                throw new RuntimeException("身份证格式有误！");
            }
            if(user.getId() == null) {
                // 设置用户类型为教师
                user.setUserType("1");
            }
            // 解析性别
            user.setSex(IdcardUtil.getGenderByIdCard(user.getIdCardNum()) + "");
            // 调用方法
            boolean result = userService.insertOrUpdate(user);
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
            boolean result = userService.deleteInfo(ids);
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
