package com.project.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.commons.model.ExaminationDetail;
import com.project.commons.model.Subject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
public interface ExaminationDetailMapper extends BaseMapper<ExaminationDetail> {

    /**
     * 分页查询考试详情
     * @param pageParam
     * @param examinationDetail
     * @return
     */
    IPage<ExaminationDetail> getSubjectListByPage(Page<ExaminationDetail> pageParam,
                                              @Param("item") ExaminationDetail examinationDetail);
}