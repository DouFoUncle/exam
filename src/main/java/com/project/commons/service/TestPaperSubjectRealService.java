package com.project.commons.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.commons.mapper.SubjectMapper;
import com.project.commons.mapper.TestPaperMapper;
import com.project.commons.model.Subject;
import com.project.commons.model.TestPaper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.commons.model.TestPaperSubjectReal;
import com.project.commons.mapper.TestPaperSubjectRealMapper;
/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Service
@AllArgsConstructor
public class TestPaperSubjectRealService extends ServiceImpl<TestPaperSubjectRealMapper, TestPaperSubjectReal> {

    private TestPaperSubjectRealMapper testPaperSubjectRealMapper;

    private SubjectMapper subjectMapper;

    private TestPaperMapper testPaperMapper;

    /**
     * 删除信息
     * @param ids
     * @return
     */
    public boolean deleteInfo(String ids) {
        return false;
    }

    /**
     * 查询关联的题目信息
     * @param testPaperSubjectReal
     * @param page
     * @param limit
     * @return
     */
    public IPage<Subject> getSubjectListByPage(TestPaperSubjectReal testPaperSubjectReal, Integer page, Integer limit) {
        // 分页查询
        List<Subject> subjects = subjectMapper.getListByPageTwo(((page - 1) * limit), limit, testPaperSubjectReal);
        IPage<Subject> pageBean = new Page<>();
        pageBean.setRecords(subjects);
        pageBean.setTotal(subjectMapper.getDataCountTwo(testPaperSubjectReal));
        return pageBean;
    }

    /**
     * 增加新的绑定关系
     * @param dataList
     * @return
     */
    public boolean insertInfo(List<TestPaperSubjectReal> dataList) {
        // 首先删除改试卷下的所有题目
        QueryWrapper<TestPaperSubjectReal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("test_paper_id", dataList.get(0).getTestPaperId());
        testPaperSubjectRealMapper.delete(queryWrapper);
        // 重新计算试卷总分
        Integer fullScore = 0;
        // 循环处理信息
        for (TestPaperSubjectReal testPaperSubjectReal : dataList) {
            Subject subject = subjectMapper.selectById(testPaperSubjectReal.getSubjectId());
            TestPaper testPaper = testPaperMapper.selectById(testPaperSubjectReal.getTestPaperId());
            testPaperSubjectReal.setSubjectTitle(subject.getTitle());
            testPaperSubjectReal.setTitle(testPaper.getTitle());
            // 计算试卷总分
            fullScore += subject.getScore();
            // 批量插入信息
            testPaperSubjectRealMapper.insert(testPaperSubjectReal);
        }
        // 更新试卷中的题目数量和总分
        TestPaper testPaper = new TestPaper();
        testPaper.setId(dataList.get(0).getTestPaperId());
        testPaper.setFullScore(fullScore);
        testPaper.setSubjectCount(dataList.size());
        // 调用更新试卷方法
        testPaperMapper.updateById(testPaper);
        return true;
    }

    /**
     * 根据条件查询信息
     * @param testPaperSubjectRealQueryWrapper
     * @return
     */
    public List<TestPaperSubjectReal> getListByParam(QueryWrapper<TestPaperSubjectReal> testPaperSubjectRealQueryWrapper) {
        return testPaperSubjectRealMapper.selectList(testPaperSubjectRealQueryWrapper);
    }
}
