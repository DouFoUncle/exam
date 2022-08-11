package com.project.student.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.LayuiTreeBean;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.TestPaper;
import com.project.commons.model.User;
import com.project.commons.service.CourseService;
import com.project.commons.service.TestPaperService;
import com.project.commons.util.ServletUtils;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 学生控制器
 */
@RestController
@RequestMapping("/studentTestPaper")
@AllArgsConstructor
public class StudentTestPaperController {

    private TestPaperService testPaperService;

    private CourseService courseService;

    /**
     * 分页获取表格数据
     * @param testPaper
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<TestPaper>> getListByPage(Integer page, Integer limit, TestPaper testPaper, HttpServletRequest request) {
        try {
            // 学生专用, 只查询已发布的考试
            testPaper.setPublishStatus("1");
            testPaper.setUserId(ServletUtils.getAdminIdInfo(request));
            // 调用分页查询方法
            IPage<TestPaper> data = testPaperService.getStudentListByPage(testPaper, page, limit, ServletUtils.getAdminIdInfo(request));
            return new ResultMessage<List<TestPaper>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 判断考试是否已截止
     * @return
     */
    @GetMapping("examIsOver")
    public ResultMessage<TestPaper> examIsOver(Integer id, HttpServletRequest request) {
        try {
            // 调用方法
            TestPaper result = testPaperService.getById(id);
            if(DateUtil.compare(DateUtil.parse(result.getEndDate()), new Date()) < 0) {
                // 已过期
                return new ResultMessage<TestPaper>(2, "考试已截止参加！");
            }
            return new ResultMessage<TestPaper>(0, "考试未过期！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 提交考卷
     * @param testPaper
     * @return
     */
    @PostMapping("submitExam")
    public ResultMessage<TestPaper> submitExam(TestPaper testPaper, HttpServletRequest request) {
        try {
            // 调用方法
            boolean result = testPaperService.saveBySubmitExam(testPaper, ServletUtils.getAdminIdInfo(request));
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<TestPaper>(0, "操作成功！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 新增自动生成
     * @return
     */
    @PostMapping("saveAutoTestPaperInfo")
    public ResultMessage<TestPaper> saveAutoTestPaperInfo(String courseIds, HttpServletRequest request) {
        try {
            TestPaper testPaper = new TestPaper();
            if(testPaper.getId() == null) {
                User user = ServletUtils.getAdminInfo(request);
                testPaper.setCreateDate(new Date());
                testPaper.setUserId(ServletUtils.getAdminIdInfo(request));
                // 默认已发布
                testPaper.setPublishStatus("1");
                testPaper.setUserType(user.getUserType());
                // 标题
                testPaper.setTitle(user.getRealName() + "在线练习-" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"));
                // 创建时间
                testPaper.setCreateDate(DateUtil.parse(DateUtil.now()));
                // 开始时间当前时间
                testPaper.setStartDate(DateUtil.formatDateTime(new Date()));
                // 截止参加时间当前时间+10分钟
                testPaper.setEndDate(DateUtil.formatDateTime(DateUtil.offsetMinute(new Date(), 10)));
                // 考试时间默认20分钟
                testPaper.setTestTime("20");
                // 默认练习题
                testPaper.setType("1");
            }
            // 调用方法
            TestPaper result = testPaperService.insertStudentAutoTestPaperInfo(testPaper, courseIds);
            if(result == null || result.getId() == null) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<TestPaper>(0, "操作成功！", result);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }
}
