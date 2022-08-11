package com.project.commons.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.commons.mapper.SubjectMapper;
import com.project.commons.mapper.TestPaperSubjectRealMapper;
import com.project.commons.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.commons.mapper.CourseMapper;
/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 课程业务层
 */
@Service
@AllArgsConstructor
public class CourseService extends ServiceImpl<CourseMapper, Course> {

    private CourseMapper courseMapper;

    private SubjectMapper subjectMapper;

    private TestPaperSubjectRealMapper testPaperSubjectRealMapper;

    /**
     * 分页查询信息
     * @param course
     * @param page
     * @param limit
     * @return
     */
    public IPage<Course> getListByPage(Course course, Integer page, Integer limit) {
        // 创建查询条件对象
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        // 根据课程名查询查询
        queryWrapper.like(!StrUtil.isBlankOrUndefined(course.getCourseName()), "course_name", course.getCourseName());
        // 创建page对象
        IPage<Course> pageBean = new Page<Course>(page, limit);
        return courseMapper.selectPage(pageBean, queryWrapper);
    }

    /**
     * 删除课程信息
     * @param ids
     * @return
     */
    public boolean deleteInfo(String ids) {
        // 判断课程是否绑定了课程
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<>();
        subjectQueryWrapper.in("course_id", StrUtil.split(ids, ',', true, true));
        List<Subject> subjects = subjectMapper.selectList(subjectQueryWrapper);
        if(subjects != null && subjects.size() > 0) {
            throw new RuntimeException("删除的课程中还有绑定试题！");
        }
        return this.removeBatchByIds(StrUtil.split(ids, ',', true, true));
    }

    /**
     * 根据条件构造器查询列表
     * @param queryWrapper
     * @return
     */
    public List<Course> getListData(QueryWrapper<Course> queryWrapper) {
        return courseMapper.selectList(queryWrapper);
    }

    /**
     * 查询树状组件需要的数据展示
     * @param id
     * @return
     */
    public List<LayuiTreeBean> getTreeDataByTestPaperId(Integer category, Integer id) {
        // 查询所有课程信息
        List<Course> courseList = courseMapper.selectList(null);
        // ID不为空的话根据试卷ID查询该试卷下已有的试题
        List<TestPaperSubjectReal> dataList = null;
        if(id != null) {
            // 根据试卷ID查询该试卷下包含的题目
            QueryWrapper<TestPaperSubjectReal> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("test_paper_id", id);
            dataList = testPaperSubjectRealMapper.selectList(queryWrapper);
        }
        return handleTreeData(courseList, dataList, category);
    }

    /**
     * 处理数据, 将数据处理成树状组件数据
     * @return
     */
    private List<LayuiTreeBean> handleTreeData(List<Course> courseList, List<TestPaperSubjectReal> dataList, Integer category) {
        List<LayuiTreeBean> result = new ArrayList<>();
        // 循环处理数据
        for (Course course : courseList) {
            // 创建一级树状组件实体类
            LayuiTreeBean layuiTreeBean = new LayuiTreeBean();
            layuiTreeBean.setId(UUID.randomUUID().toString());
            layuiTreeBean.setTitle(course.getCourseName());
            layuiTreeBean.setField("courseName");
            layuiTreeBean.setChecked(false);
            layuiTreeBean.setSpread(true);
            // 根据课程ID查询课程下的指定题目类型
            QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", course.getId());
            queryWrapper.eq("category", category);
            queryWrapper.eq("del_flag", "0");
            List<Subject> subjectList = subjectMapper.selectList(queryWrapper);
            if(subjectList == null || subjectList.size() == 0) {
                continue;
            }
            // 创建一个子集合用于保存课程节点下的题目节点
            List<LayuiTreeBean> subTreeData = new ArrayList<>(16);
            // 题目不等于空时, 循环题目信息
            for (Subject subject : subjectList) {
                LayuiTreeBean subTreeBean = new LayuiTreeBean();
                subTreeBean.setTitle(subject.getTitle());
                subTreeBean.setId(subject.getId() + "");
                subTreeBean.setField("title");
                // 试卷内容不为空时, 循环试卷内容, 将已加入试卷的题目进行标注
                if(dataList != null && dataList.size() > 0) {
                    for (TestPaperSubjectReal item : dataList) {
                        if(item.getSubjectId().equals(subject.getId())) {
                            subTreeBean.setChecked(true);
                            break;
                        }
                    }
                }
                // 将处理好的数据加入子集合中
                subTreeData.add(subTreeBean);
            }
            // 将处理好的子集合加入父级树组件中
            layuiTreeBean.setChildren(subTreeData);
            // 将处理好的父组件加入最终的集合中
            result.add(layuiTreeBean);
        }
        // 返回数据
        return result;
    }

    /**
     * 根据试题类型查询该类型下有试题的课程信息
     * @param type
     * @return
     */
    public List<Course> getCourseInfoBySubjectInfo(String type) {
        return courseMapper.getCourseInfoBySubjectType(type);
    }
}
