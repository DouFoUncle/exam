package com.project.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.LayuiTreeBean;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.TestPaper;
import com.project.commons.model.User;
import com.project.commons.service.CourseService;
import com.project.commons.service.TestPaperService;
import com.project.commons.util.ServletUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Servlet;
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
@RequestMapping("/testPaper")
@AllArgsConstructor
public class TestPaperController {

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
            // 只查询自己发的考试
            // testPaper.setUserId(ServletUtils.getAdminIdInfo(request));
            // 调用分页查询方法
            IPage<TestPaper> data = testPaperService.getListByPage(testPaper, page, limit);
            return new ResultMessage<List<TestPaper>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 查询树状组件数据
     * @return
     */
    @GetMapping("getTreeData")
    public ResultMessage<List<LayuiTreeBean>> getTreeData(Integer category, Integer id) {
        try {
            // 查询所有课程和课程下的题目用来生成树状组件 1代表查询考试题
            List<LayuiTreeBean> treeDataList = courseService.getTreeDataByTestPaperId(category, id);
            return new ResultMessage<List<LayuiTreeBean>>(0, "查询成功！", treeDataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 修改信息
     * @param testPaper
     * @return
     */
    @PostMapping("updateInfo")
    public ResultMessage<TestPaper> updateInfo(TestPaper testPaper) {
        try {
            // 调用方法
            boolean result = testPaperService.updateById(testPaper);
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
     * 新增试卷
     * @param testPaper
     * @return
     */
    @PostMapping("saveInfo")
    public ResultMessage<TestPaper> saveInfo(@RequestBody TestPaper testPaper, HttpServletRequest request) {
        try {
            if(testPaper.getId() == null) {
                User user = ServletUtils.getAdminInfo(request);
                testPaper.setCreateDate(new Date());
                testPaper.setUserId(ServletUtils.getAdminIdInfo(request));
                // 默认未发布 0未发布 1已发布
                testPaper.setPublishStatus("0");
                testPaper.setUserType(user.getUserType());
            }
            // 调用方法
            boolean result = testPaperService.insertInfo(testPaper);
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
     * 新增自动组卷的数据试卷
     * @param testPaper
     * @return
     */
    @PostMapping("saveAutoTestPaperInfo")
    public ResultMessage<TestPaper> saveAutoTestPaperInfo(TestPaper testPaper, HttpServletRequest request) {
        try {
            if(testPaper.getId() == null) {
                User user = ServletUtils.getAdminInfo(request);
                testPaper.setCreateDate(new Date());
                testPaper.setUserId(ServletUtils.getAdminIdInfo(request));
                // 默认未发布 0未发布 1已发布
                testPaper.setPublishStatus("0");
                testPaper.setUserType(user.getUserType());
            }
            // 调用方法
            boolean result = testPaperService.insertAutoTestPaperInfo(testPaper);
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
     * 删除信息
     * @return
     */
    @DeleteMapping("deleteInfo")
    public ResultMessage<TestPaper> deleteInfo(String ids) {
        try {
            // 调用方法
            boolean result = testPaperService.deleteInfo(ids);
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

}
