package com.project.commons.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.commons.model.Subject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.commons.model.ExaminationDetail;
import com.project.commons.mapper.ExaminationDetailMapper;
/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 考试详情表业务层
 */
@Service
@AllArgsConstructor
public class ExaminationDetailService extends ServiceImpl<ExaminationDetailMapper, ExaminationDetail> {

    private ExaminationDetailMapper examinationDetailMapper;

    /**
     * 分页查询考试详情
     * @param examinationDetail
     * @param page
     * @param limit
     * @return
     */
    public IPage<ExaminationDetail> getListByPage(ExaminationDetail examinationDetail, Integer page, Integer limit) {
        QueryWrapper<ExaminationDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StrUtil.isBlankOrUndefined(examinationDetail.getExamTitle()), "exam_title", examinationDetail.getExamTitle());
        queryWrapper.like(!StrUtil.isBlankOrUndefined(examinationDetail.getSubjectTitle()), "subject_title", examinationDetail.getSubjectTitle());
        queryWrapper.eq(!StrUtil.isBlankOrUndefined(examinationDetail.getUserSelect()), "user_select", examinationDetail.getUserSelect());
        queryWrapper.eq(examinationDetail.getUserId() != null, "user_id", examinationDetail.getUserId());
        queryWrapper.orderByDesc("create_date");
        // 创建分页对象
        IPage<ExaminationDetail> pageBean = new Page<>(page, limit);
        return examinationDetailMapper.selectPage(pageBean, queryWrapper);
    }

    /**
     * 根据考试记录ID查询该记录下的考试详情
     * @param examinationDetail
     * @param page
     * @param limit
     * @return
     */
    public IPage<ExaminationDetail> getSubjectListByPage(ExaminationDetail examinationDetail, Integer page, Integer limit) {
        // 创建分页对象
        Page<ExaminationDetail> pageParam = new Page<>(page, limit);
        IPage<ExaminationDetail> pageBean = examinationDetailMapper.getSubjectListByPage(pageParam, examinationDetail);
        // 替换选项
        if(pageBean != null && pageBean.getRecords() != null && pageBean.getRecords().size() > 0) {
            for (ExaminationDetail record : pageBean.getRecords()) {
                if(!StrUtil.isBlankOrUndefined(record.getUserResult())) {
                    // 替换选项
                    record.setUserResult(record.getUserResult().replace("1", "A"));
                    record.setUserResult(record.getUserResult().replace("2", "B"));
                    record.setUserResult(record.getUserResult().replace("3", "C"));
                    record.setUserResult(record.getUserResult().replace("4", "D"));
                }
                if(!StrUtil.isBlankOrUndefined(record.getFinalResult())) {
                    // 替换选项
                    record.setFinalResult(record.getFinalResult().replace("1", "A"));
                    record.setFinalResult(record.getFinalResult().replace("2", "B"));
                    record.setFinalResult(record.getFinalResult().replace("3", "C"));
                    record.setFinalResult(record.getFinalResult().replace("4", "D"));
                }
            }
        }
        return pageBean;
    }
}
