package com.project.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.commons.model.ExaminationVisit;
import org.apache.ibatis.annotations.Param;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
public interface ExaminationVisitMapper extends BaseMapper<ExaminationVisit> {

    /**
     * 分页查询信息
     * @param pageParam
     * @param examinationVisit
     * @return
     */
    IPage<ExaminationVisit> getListByPage(Page<ExaminationVisit> pageParam,
                                          @Param("item") ExaminationVisit examinationVisit);
}