package com.project.commons.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.commons.model.Subject;
import com.project.commons.model.TestPaperSubjectReal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
public interface SubjectMapper extends BaseMapper<Subject> {

    /**
     * 分页查询信息
     * @param page
     * @param limit
     * @param subject
     * @return
     */
    List<Subject> getListByPage(@Param("page") Integer page,
                                @Param("limit") Integer limit,
                                @Param("subject") Subject subject);

    /**
     * 查询数据数量
     * @param subject
     * @return
     */
    Long getDataCount(@Param("subject") Subject subject);

    /**
     * 根据试卷和试题绑定信息查询试题
     * @param page
     * @param limit
     * @param testPaperSubjectReal
     * @return
     */
    List<Subject> getListByPageTwo(@Param("page") Integer page,
                                   @Param("limit") Integer limit,
                                   @Param("item") TestPaperSubjectReal testPaperSubjectReal);

    /**
     * 根据试卷和试题绑定信息查询试题数量
     * @param testPaperSubjectReal
     * @return
     */
    long getDataCountTwo(@Param("item") TestPaperSubjectReal testPaperSubjectReal);
}