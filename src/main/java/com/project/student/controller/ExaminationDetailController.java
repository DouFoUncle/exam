package com.project.student.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.ExaminationDetail;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.Subject;
import com.project.commons.service.ExaminationDetailService;
import com.project.commons.service.SubjectService;
import com.project.commons.util.ServletUtils;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 考试详情控制器
 */
@RestController
@RequestMapping("/examinationDetail")
@AllArgsConstructor
public class ExaminationDetailController {

    private ExaminationDetailService examinationDetailService;

    private SubjectService subjectService;

    /**
     * 分页获取表格数据
     * @param examinationDetail
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<ExaminationDetail>> getListByPage(Integer page, Integer limit, ExaminationDetail examinationDetail, HttpServletRequest request) {
        try {
            // 调用分页查询方法
            examinationDetail.setUserId(ServletUtils.getAdminIdInfo(request));
            IPage<ExaminationDetail> data = examinationDetailService.getListByPage(examinationDetail, page, limit);
            return new ResultMessage<List<ExaminationDetail>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 根据考试记录ID查询该记录下的考试详情
     * @param examinationDetail
     * @return
     */
    @GetMapping("getSubjectListByPage")
    public ResultMessage<List<ExaminationDetail>> getSubjectListByPage(Integer page, Integer limit, ExaminationDetail examinationDetail, HttpServletRequest request) {
        try {
            IPage<ExaminationDetail> data = examinationDetailService.getSubjectListByPage(examinationDetail, page, limit);
            return new ResultMessage<List<ExaminationDetail>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }
}
