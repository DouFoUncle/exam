package com.project.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.ExaminationVisit;
import com.project.commons.model.ResultMessage;
import com.project.commons.service.ExaminationVisitService;
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
 * 考试记录控制器
 */
@RestController
@RequestMapping("/examVisit")
@AllArgsConstructor
public class AdminExaminationVisitController {

    private ExaminationVisitService examinationVisitService;

    /**
     * 分页获取表格数据
     * @param examinationVisit
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<ExaminationVisit>> getListByPage(Integer page, Integer limit, ExaminationVisit examinationVisit, HttpServletRequest request) {
        try {
            // 调用分页查询方法
            IPage<ExaminationVisit> data = examinationVisitService.getListByPage(examinationVisit, page, limit);
            return new ResultMessage<List<ExaminationVisit>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }
}
