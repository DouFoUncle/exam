package com.project.admin.controller;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.Course;
import com.project.commons.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 课程控制器
 */
@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

    private CourseService courseService;

    /**
     * 分页获取表格数据
     * @param course
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<Course>> getListByPage(Integer page, Integer limit, Course course, HttpServletRequest request) {
        try {
            // 调用分页查询方法
            IPage<Course> data = courseService.getListByPage(course, page, limit);
            return new ResultMessage<List<Course>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 新增或修改信息
     * @param course
     * @return
     */
    @PostMapping("saveOrUpdate")
    public ResultMessage<Course> saveOrUpdate(Course course) {
        try {
            if(course.getId() == null) {
                course.setCreateDate(new Date());
            }
            // 调用方法
            boolean result = courseService.saveOrUpdate(course);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<Course>(0, "操作成功！");
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
    public ResultMessage<Course> deleteInfo(String ids) {
        try {
            // 调用方法
            boolean result = courseService.deleteInfo(ids);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<Course>(0, "操作成功！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

}
