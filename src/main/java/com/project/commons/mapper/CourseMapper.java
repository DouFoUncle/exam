package com.project.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.commons.model.Course;

import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 根据试题类型查询该类型下有试题的课程信息
     * @param type
     * @return
     */
    List<Course> getCourseInfoBySubjectType(String type);
}