package com.project.commons.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.commons.model.ExaminationVisit;
import com.project.commons.mapper.ExaminationVisitMapper;
/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 *
 */
@Service
@AllArgsConstructor
public class ExaminationVisitService extends ServiceImpl<ExaminationVisitMapper, ExaminationVisit> {

    private ExaminationVisitMapper examinationVisitMapper;

    /**
     * 分页获取表格数据
     * @param examinationVisit
     * @param page
     * @param limit
     * @return
     */
    public IPage<ExaminationVisit> getListByPage(ExaminationVisit examinationVisit, Integer page, Integer limit) {
        // 创建查询条件
        Page<ExaminationVisit> pageParam = new Page<>(page, limit);
        // 创建分页对象
        IPage<ExaminationVisit> pageBean = examinationVisitMapper.getListByPage(pageParam, examinationVisit);
        // 调用查询方法
        return pageBean;
    }
}
