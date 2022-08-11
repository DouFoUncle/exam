package com.project.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.Subject;
import com.project.commons.model.TestPaper;
import com.project.commons.model.TestPaperSubjectReal;
import com.project.commons.service.SubjectService;
import com.project.commons.service.TestPaperSubjectRealService;
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
 * 题库控制器
 */
@RestController
@RequestMapping("/testPaperSubjectReal")
@AllArgsConstructor
public class TestPaperSubjectRealController {

    private TestPaperSubjectRealService testPaperSubjectRealService;

    /**
     * 分页获取表格数据
     * @param testPaperSubjectReal
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<Subject>> getListByPage(Integer page, Integer limit, TestPaperSubjectReal testPaperSubjectReal) {
        try {
            // 调用分页查询方法
            IPage<Subject> data = testPaperSubjectRealService.getSubjectListByPage(testPaperSubjectReal, page, limit);
            return new ResultMessage<List<Subject>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 新增或修改信息
     * @param testPaperSubjectReal
     * @return
     */
    @PostMapping("saveOrUpdate")
    public ResultMessage<Subject> saveOrUpdate(TestPaperSubjectReal testPaperSubjectReal) {
        try {
            // 调用方法
            boolean result = testPaperSubjectRealService.saveOrUpdate(testPaperSubjectReal);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<Subject>(0, "操作成功！");
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
     * @return
     */
    @PostMapping("saveInfo")
    public ResultMessage<TestPaperSubjectReal> saveInfo(@RequestBody List<TestPaperSubjectReal> dataList, HttpServletRequest request) {
        try {
            // 调用方法
            boolean result = testPaperSubjectRealService.insertInfo(dataList);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<TestPaperSubjectReal>(0, "操作成功！");
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
    public ResultMessage<Subject> deleteInfo(String ids) {
        try {
            // 调用方法
            boolean result = testPaperSubjectRealService.deleteInfo(ids);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<Subject>(0, "操作成功！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

}
