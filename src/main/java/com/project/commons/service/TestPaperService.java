package com.project.commons.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.commons.mapper.*;
import com.project.commons.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 试卷业务层
 */
@Service
@AllArgsConstructor
public class TestPaperService extends ServiceImpl<TestPaperMapper, TestPaper> {

    private TestPaperMapper testPaperMapper;

    private TestPaperSubjectRealMapper testPaperSubjectRealMapper;

    private SubjectMapper subjectMapper;

    private ExaminationVisitMapper examinationVisitMapper;

    private UserMapper userMapper;

    private ExaminationDetailService examinationDetailService;

    /**
     * 删除信息
     * @param ids
     * @return
     */
    public boolean deleteInfo(String ids) {
        // 未发布的信息才可以删除
        // 判断是否有发布信息
        QueryWrapper<TestPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publish_status", "1");
        queryWrapper.in("id", StrUtil.split(ids, ',', true, true));
        List<TestPaper> testPapers = testPaperMapper.selectList(queryWrapper);
        // 如果有则不可以删除
        if(testPapers != null && testPapers.size() > 0) {
            throw new RuntimeException("已发布的试卷无法删除！");
        }
        // 删除关联的信息
        QueryWrapper<TestPaperSubjectReal> testPaperSubjectRealQueryWrapper = new QueryWrapper<>();
        testPaperSubjectRealQueryWrapper.in("test_paper_id", StrUtil.split(ids, ',', true, true));
        testPaperSubjectRealMapper.delete(testPaperSubjectRealQueryWrapper);
        // 删除试卷信息
        return this.removeBatchByIds(StrUtil.split(ids, ',', true, true));
    }

    /**
     * 分页查询数据
     * @param testPaper
     * @param page
     * @param limit
     * @return
     */
    public IPage<TestPaper> getListByPage(TestPaper testPaper, Integer page, Integer limit) {
        // 创建查询条件
        QueryWrapper<TestPaper> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StrUtil.isBlankOrUndefined(testPaper.getTitle()), "title", testPaper.getTitle());
        queryWrapper.eq(!StrUtil.isBlankOrUndefined(testPaper.getType()), "type", testPaper.getType());
        queryWrapper.eq(!StrUtil.isBlankOrUndefined(testPaper.getPublishStatus()), "publish_status", testPaper.getPublishStatus());
        queryWrapper.eq(testPaper.getUserId() != null, "user_id", testPaper.getUserId());
        queryWrapper.ne("user_type", "2");
        // 创建分页对象
        IPage<TestPaper> pageBean = new Page<>(page, limit);
        pageBean = testPaperMapper.selectPage(pageBean, queryWrapper);
        if(pageBean != null && pageBean.getRecords() != null && pageBean.getRecords().size() > 0) {
            // 循环查询发布人信息
            for (TestPaper record : pageBean.getRecords()) {
                User user = userMapper.selectById(record.getUserId());
                record.setUser(user);
            }
        }
        // 调用查询方法
        return pageBean;
    }

    /**
     * 学生用户查询考试列表信息
     * @param testPaper
     * @param page
     * @param limit
     * @return
     */
    public IPage<TestPaper> getStudentListByPage(TestPaper testPaper, Integer page, Integer limit, Integer studentId) {
        // 创建分页对象
        Page<TestPaper> pageParam = new Page<>(page, limit);
        // 创建分页对象
        IPage<TestPaper> pageBean = testPaperMapper.getStudentListByPage(pageParam, testPaper, studentId);
        // 循环处理判断考试状态
        if(pageBean.getRecords() != null && pageBean.getRecords().size() > 0) {
            for (TestPaper record : pageBean.getRecords()) {
                if(DateUtil.compare(DateUtil.parse(record.getStartDate()), new Date()) > 0) {
                    // 未开始
                    record.setBeginStatus("0");
                } else if(DateUtil.compare(DateUtil.parse(record.getStartDate()), new Date()) <= 0 &&
                          DateUtil.compare(DateUtil.parse(record.getEndDate()), new Date()) > 0) {
                    // 已开始
                    record.setBeginStatus("1");
                } else if(DateUtil.compare(DateUtil.parse(record.getEndDate()), new Date()) < 0) {
                    // 已结束
                    record.setBeginStatus("2");
                }
            }
        }
        // 调用查询方法
        return pageBean;
    }

    /**
     * 新增试卷信息 - 人工组卷
     * @param testPaper
     * @return
     */
    public boolean insertInfo(TestPaper testPaper) {
        // 判断是否选择了题目
        if(testPaper.getSubIds() == null || testPaper.getSubIds().size() == 0) {
            throw new RuntimeException("未选择题目！");
        }
        // 设置题目数量
        testPaper.setSubjectCount(testPaper.getSubIds().size());
        // 查询选择的题目信息
        List<Subject> subjectList = subjectMapper.selectList(new QueryWrapper<Subject>().in("id", testPaper.getSubIds()));
        // 循环计算题目总分
        Integer full = 0;
        for (Subject subject : subjectList) {
            full += subject.getScore();
        }
        testPaper.setFullScore(full);
        // 调用新增方法插入试卷信息
        this.save(testPaper);
        // 循环创建信息, 绑定试卷和试题关系
        for (Subject item : subjectList) {
            TestPaperSubjectReal testPaperSubjectReal = new TestPaperSubjectReal();
            testPaperSubjectReal.setSubjectId(item.getId());
            testPaperSubjectReal.setSubjectTitle(item.getTitle());
            testPaperSubjectReal.setTestPaperId(testPaper.getId());
            testPaperSubjectReal.setTitle(testPaper.getTitle());
            // 插入关系绑定信息
            testPaperSubjectRealMapper.insert(testPaperSubjectReal);
        }
        return true;
    }

    /**
     * 学生新增练习卷
     * @param testPaper
     * @return
     */
    public TestPaper insertStudentAutoTestPaperInfo(TestPaper testPaper, String courseIds) {
        // 查询所选课程下的所有题目
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("course_id", StrUtil.split(courseIds, ',', true, true));
        queryWrapper.eq("del_flag", "0");
        List<Subject> subjectList = subjectMapper.selectList(queryWrapper);
        if(subjectList == null || subjectList.size() <= 0) {
            throw new RuntimeException("题库没有题目可以生成！");
        }
        // 打乱顺序
        Collections.shuffle(subjectList);
        // 判断是否满足十道题, 不满足则直接有多少取多少
        List<Subject> finalSubject = new ArrayList<>(10);
        if(subjectList.size() < 10) {
            finalSubject.addAll(subjectList);
        } else {
            // 大于10则截取十条插入
            finalSubject.addAll(subjectList.subList(0, 10));
            // 再次打乱
            Collections.shuffle(finalSubject);
        }
        // 设置题目数量
        testPaper.setSubjectCount(finalSubject.size());
        // 循环计算题目总分
        Integer full = 0;
        for (Subject subject : finalSubject) {
            full += subject.getScore();
        }
        // 设置总分
        testPaper.setFullScore(full);
        // 调用新增方法插入试卷信息
        this.save(testPaper);
        // 循环创建信息, 绑定试卷和试题关系
        for (Subject item : finalSubject) {
            TestPaperSubjectReal testPaperSubjectReal = new TestPaperSubjectReal();
            testPaperSubjectReal.setSubjectId(item.getId());
            testPaperSubjectReal.setSubjectTitle(item.getTitle());
            testPaperSubjectReal.setTestPaperId(testPaper.getId());
            testPaperSubjectReal.setTitle(testPaper.getTitle());
            // 插入关系绑定信息
            testPaperSubjectRealMapper.insert(testPaperSubjectReal);
        }
        return testPaper;
    }

    /**
     * 新增试卷信息 - 自动组卷
     * @param testPaper
     * @return
     */
    public boolean insertAutoTestPaperInfo(TestPaper testPaper) {
        // 保存最终生成的所有题目
        List<Subject> subjects = new ArrayList<>(16);
        // 循环处理课程, 生成题目
        for (AutoTestPaperInfo item : testPaper.getAutoTestPaperInfoList()) {
            // 查询该课题下的所有该类型的题目
            QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("del_flag", "0");
            queryWrapper.eq("category", "0".equals(testPaper.getType()) ? "1" : "0");
            queryWrapper.eq("course_id", item.getCourseId());
            List<Subject> selectSubject = subjectMapper.selectList(queryWrapper);
            // 统计该题目所有的选择题和主观题, 判断数量是否足够生成
            // 单选题数量
            Integer selectCount = totalCount(selectSubject, "0");
            // 多选题数量
            Integer selectsCount = totalCount(selectSubject, "1");
            // 判断题数量
            Integer judgeCount = totalCount(selectSubject, "2");
            // 判断数量是否足够
            if(selectCount < item.getSelectCount()) {
                throw new RuntimeException(item.getCourseName() + " - 单选题数量不足以生成！");
            }
            if(selectsCount < item.getSelectsCount()) {
                throw new RuntimeException(item.getCourseName() + " - 多选题数量不足以生成！");
            }
            if(judgeCount < item.getJudgeCount()) {
                throw new RuntimeException(item.getCourseName() + " - 判断题数量不足以生成！");
            }
            // 数量足够
            // 生成单选题
            List<Subject> generateSelectSubject = generateSubject(selectSubject, "0", item.getSelectCount());
            // 生成多选题
            List<Subject> generateSelectsSubject = generateSubject(selectSubject, "1", item.getSelectsCount());
            // 生成判断题
            List<Subject> generateJudgeSubject = generateSubject(selectSubject, "2", item.getJudgeCount());
            // 将生成的题目保存进最终题目集合中
            subjects.addAll(generateSelectSubject);
            subjects.addAll(generateSelectsSubject);
            subjects.addAll(generateJudgeSubject);
        }
        // 设置题目数量
        testPaper.setSubjectCount(subjects.size());
        // 循环计算题目总分
        Integer full = 0;
        for (Subject subject : subjects) {
            full += subject.getScore();
        }
        testPaper.setFullScore(full);
        // 调用新增方法插入试卷信息
        this.save(testPaper);
        // 循环创建信息, 绑定试卷和试题关系
        for (Subject item : subjects) {
            TestPaperSubjectReal testPaperSubjectReal = new TestPaperSubjectReal();
            testPaperSubjectReal.setSubjectId(item.getId());
            testPaperSubjectReal.setSubjectTitle(item.getTitle());
            testPaperSubjectReal.setTestPaperId(testPaper.getId());
            testPaperSubjectReal.setTitle(testPaper.getTitle());
            // 插入关系绑定信息
            testPaperSubjectRealMapper.insert(testPaperSubjectReal);
        }
        return true;
    }
    
    /**
     * 生成题目
     * @param subjectList   原题目信息
     * @param type          生成什么类型的题目
     * @param num           生成多少条
     * @return
     */
    private List<Subject> generateSubject(List<Subject> subjectList, String type, Integer num) {
        List<Subject> list = new ArrayList<>(16);
        // 如果数量为0直接返回空集合
        if(num == 0) {
            return list;
        }
        // 根据生成的题目类型先将这类型题目筛选出来
        List<Subject> listByType = new ArrayList<>(16);
        for (Subject item : subjectList) {
            if(item.getType().equals(type)) {
                listByType.add(item);
            }
        }
        // 生成随机数, 生成规则
        // 1. 首先随机数不可超过题目总数
        // 2. 生成过的随机数不可以再次出现
        // 使用do-while循环, 循环的结束条件是当存放生成题目的集合长度等于 要生成的数量时就停止
        // 声明一个变量用来记录每次生成的随机数
        String randomStr = "";
        while (list.size() < num) {
            // 创建随机数对象
            Random randomBean = new Random();
            // 指定最大生成数, 也就是题目集合的总数
            int random = randomBean.nextInt(listByType.size());
            // 判断这个数是否已经生成过
            if(!"".equals(randomStr) && randomStr.contains(random+"")) {
                // 已经生成过, 直接进行下次循环
                continue;
            }
            // 没有生成过, 取出这个下标对应的题目存入最终返回的集合
            list.add(listByType.get(random));
            // 将这次生成的随机数保存在变量中, 用于下次判断是否生成过
            randomStr += random;
        }
        return list;
    }
    
    /**
     * 统计题目数量
     * @param subjects
     * @param type
     * @return
     */
    private Integer totalCount(List<Subject> subjects, String type) {
        Integer result = 0;
        for (Subject item : subjects) {
            if(type.equals(item.getType())) {
                result += 1;
            }
        }
        return result;
    }

    /**
     * 学生提交考试试卷
     * @param testPaper
     * @param studentId
     * @return
     */
    public boolean saveBySubmitExam(TestPaper testPaper, Integer studentId) {
        // 首先根据试题ID查询该试题下的所有题目信息
        QueryWrapper<TestPaperSubjectReal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("test_paper_id", testPaper.getId());
        List<TestPaperSubjectReal> testPaperSubjectReals = testPaperSubjectRealMapper.selectList(queryWrapper);
        // 获取用户的答题信息
        List<StartExamSubject> startExamSubjects = testPaper.getStartExamSubjects();
        // 记录用户获得的分数
        Integer examScore = 0;
        // 记录用户的答题详情
        List<ExaminationDetail> examinationDetails = new ArrayList<>(16);
        // 循环比对答题情况
        for (TestPaperSubjectReal testPaperSubjectReal : testPaperSubjectReals) {
            // 取出题目ID，查询题目信息
            Subject subject = subjectMapper.selectById(testPaperSubjectReal.getSubjectId());
            for (StartExamSubject startExamSubject : startExamSubjects) {
                // 如果ID一致，则判断是否回答正确
                if(startExamSubject.getSubjectId().equals(subject.getId())) {
                    // 记录用户的答题信息
                    ExaminationDetail examinationDetail = new ExaminationDetail();
                    examinationDetail.setTestPaperId(testPaper.getId());
                    examinationDetail.setUserId(studentId);
                    examinationDetail.setExamTitle(testPaper.getTitle());
                    examinationDetail.setSubjectId(startExamSubject.getSubjectId());
                    examinationDetail.setSubjectTitle(subject.getTitle());
                    examinationDetail.setCreateDate(new Date());
                    examinationDetail.setUserResult(startExamSubject.getResult());
                    // 判断用户选择的答案和题目最终答案是否一致
                    if(subject.getResult().equals(startExamSubject.getResult())) {
                        // 回答正确，加分
                        examScore += subject.getScore();
                        examinationDetail.setUserSelect("1");
                    } else {
                        // 回答不正确，记录错题
                        examinationDetail.setUserSelect("0");
                    }
                    // 将答题详情加入答题记录集合中
                    examinationDetails.add(examinationDetail);
                }
            }
        }
        // 加入考试记录
        ExaminationVisit examinationVisit = new ExaminationVisit();
        examinationVisit.setTestPaperId(testPaper.getId());
        examinationVisit.setUserId(studentId);
        examinationVisit.setTitle(testPaper.getTitle());
        examinationVisit.setScore(examScore);
        examinationVisit.setEndDate(new Date());
        // 调用插入方法
        examinationVisitMapper.insert(examinationVisit);
        // 插入考试详情
        examinationDetailService.saveBatch(examinationDetails);
        return true;
    }
}
