package com.project.commons.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageParams;
import com.project.commons.mapper.ExaminationDetailMapper;
import com.project.commons.mapper.TestPaperSubjectRealMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.commons.model.Subject;
import com.project.commons.mapper.SubjectMapper;
/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 试题业务层
 */
@Service
@AllArgsConstructor
public class SubjectService extends ServiceImpl<SubjectMapper, Subject> {

    private SubjectMapper subjectMapper;

    /**
     * 删除信息
     * @param ids
     * @return
     */
    public boolean deleteInfo(String ids) {
        // 删除其实是将删除状态改为 1
        List<String> split = StrUtil.split(ids, ',', true, true);
        List<Subject> updateList = new ArrayList<>(16);
        for (String item : split) {
            Subject subject = new Subject();
            subject.setId(Integer.parseInt(item));
            subject.setDelFlag("1");
            updateList.add(subject);
        }
        return this.updateBatchById(updateList);
    }

    /**
     * 分页查询信息
     * @param subject
     * @param page
     * @param limit
     * @return
     */
    public IPage<Subject> getListByPage(Subject subject, Integer page, Integer limit) {
        // 分页查询
        List<Subject> subjects = subjectMapper.getListByPage(((page - 1) * limit), limit, subject);
        IPage<Subject> pageBean = new Page<>();
        pageBean.setRecords(subjects);
        pageBean.setTotal(subjectMapper.getDataCount(subject));
        return pageBean;
    }

    /**
     * 根据条件查询题目信息
     * @param subjectQueryWrapper
     * @return
     */
    public List<Subject> getListByParam(QueryWrapper<Subject> subjectQueryWrapper) {
        return subjectMapper.selectList(subjectQueryWrapper);
    }

    public boolean insertByExcel(InputStream inputStream) throws Exception {
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(1);
        importParams.setTitleRows(0);
        // 读取Excel转为List
        List<Subject> listData = ExcelImportUtil.importExcel(inputStream, Subject.class, importParams);
        if(listData == null || listData.size() == 0) {
            throw new RuntimeException("读取Excel中的数据为空！请检查！");
        }
        // 循环批量处理数据
        listData.forEach(item -> {
            // 判断是否有数据为空
            if(StringUtils.isAllBlank(item.getCourseId()) || StringUtils.isAllBlank(item.getType())
                    || StringUtils.isAllBlank(item.getTitle()) || StringUtils.isAllBlank(item.getResult())
                    || StringUtils.isAllBlank(item.getCategory()) || StringUtils.isAllBlank(item.getOptionOne())
                    || StringUtils.isAllBlank(item.getOptionTwo()) || StringUtils.isAllBlank(item.getOptionThree())
                    || StringUtils.isAllBlank(item.getOptionFour()) || item.getScore() == null) {
                throw new RuntimeException("有数据未填写！请检查Excel内容！");
            }
            // 默认未删除
            item.setDelFlag("0");
        });
        // 数据处理完成, 调用批量插入方法
        return this.saveBatch(listData);
    }
}
