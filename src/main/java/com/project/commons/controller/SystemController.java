package com.project.commons.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.commons.model.*;
import com.project.commons.service.*;
import com.project.commons.util.ServletUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台页面控制器， 控制页面之间的跳转，以及登录等操作
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Controller
@AllArgsConstructor
@RequestMapping("system")
public class SystemController {

    private UserService userService;

    private CourseService courseService;

    private ExaminationDetailService examinationDetailService;

    private ExaminationVisitService examinationVisitService;

    private MessageService messageService;

    private SubjectService subjectService;

    private TestPaperService testPaperService;

    private TestPaperSubjectRealService testPaperSubjectRealService;

    /**
     * 跳转到登录页
     * @return
     */
    @GetMapping("/")
    public ModelAndView toLoginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("login");
        return modelAndView;
    }

    /**
     * 跳转到登陆过期页面
     * @return
     */
    @GetMapping("/toTimeOutPage")
    public ModelAndView toTimeOutPage(ModelAndView modelAndView) {
        modelAndView.setViewName("timeOut");
        return modelAndView;
    }

    /**
     * 跳转到管理员后台首页
     * @param modelAndView
     * @return
     */
    @GetMapping("/index")
    public ModelAndView toIndexPage(ModelAndView modelAndView, String skipUrl) {
        modelAndView.setViewName(skipUrl);
        return modelAndView;
    }

    /**
     * 跳转到管理员后台欢迎页
     * @param modelAndView
     * @return
     */
    @GetMapping("/console")
    public ModelAndView toWelcomePage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/console/welcome");
        return modelAndView;
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public ResultMessage<Object> toLogout(HttpSession session) {
        session.removeAttribute("userInfo");
        return ResultMessage.success("操作成功！");
    }

    // -----------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------- 控制后台管理页面跳转的控制器 Start -------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------------

    /**
     * 跳转到教师信息管理
     * @param modelAndView
     * @return
     */
    @GetMapping("/teacher/toDataPage")
    public ModelAndView toTeacherPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/teacher/table");
        return modelAndView;
    }

    /**
     * 跳转到学生信息管理
     * @param modelAndView
     * @return
     */
    @GetMapping("/student/toDataPage")
    public ModelAndView toStudentPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/student/table");
        return modelAndView;
    }

    /**
     * 跳转到课程信息管理
     * @param modelAndView
     * @return
     */
    @GetMapping("/course/toDataPage")
    public ModelAndView toUserPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/course/table");
        return modelAndView;
    }

    /**
     * 跳转到题库管理
     * @param modelAndView
     * @return
     */
    @GetMapping("/subject/toDataPage")
    public ModelAndView toDeptPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/subject/table");
        return modelAndView;
    }

    /**
     * 跳转到考试管理
     * @param modelAndView
     * @return
     */
    @GetMapping("/testPaper/toDataPage")
    public ModelAndView toDiseasePage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/testPaper/table");
        return modelAndView;
    }

    /**
     * 跳转到学生我要考试页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/studentTestPaper/toDataPage")
    public ModelAndView toStudentTestPaperPage(ModelAndView modelAndView) {
        modelAndView.setViewName("student/testPaper/table");
        return modelAndView;
    }

    /**
     * 跳转到考试记录
     * @param modelAndView
     * @return
     */
    @GetMapping("/examinationVisit/toDataPage")
    public ModelAndView toPositionPage(ModelAndView modelAndView) {
        modelAndView.setViewName("student/examinationVisit/table");
        return modelAndView;
    }

    /**
     * 跳转到考试记录
     * @param modelAndView
     * @return
     */
    @GetMapping("/examVisit/toDataPage")
    public ModelAndView toExamVisitPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/examinationVisit/table");
        return modelAndView;
    }

    /**
     * 跳转到我的错题记录
     * @param modelAndView
     * @return
     */
    @GetMapping("/examinationDetail/toDataPage")
    public ModelAndView toExaminationDetailPage(ModelAndView modelAndView) {
        modelAndView.setViewName("student/examinationDetail/table");
        return modelAndView;
    }

    /**
     * 跳转到管理员管理页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/admin/toDataPage")
    public ModelAndView toAdminPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/admin/table");
        return modelAndView;
    }

    /**
     * 跳转到学生留言页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/studentMessage/toDataPage")
    public ModelAndView toStudentMessagePage(ModelAndView modelAndView) {
        modelAndView.setViewName("student/message/table");
        return modelAndView;
    }

    /**
     * 跳转到老师留言管理页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/teacherMessage/toDataPage")
    public ModelAndView toTeacherMessagePage(ModelAndView modelAndView) {
        modelAndView.setViewName("teacher/message/table");
        return modelAndView;
    }

    /**
     * 跳转到老师回复留言页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/teacherMessage/toEditPage")
    public ModelAndView toTeacherEditMessagePage(ModelAndView modelAndView, Integer id) {
        // 查询修改的信息
        modelAndView.addObject("obj", messageService.getById(id));
        modelAndView.setViewName("teacher/message/edit");
        return modelAndView;
    }

    /**
     * 跳转到学生新增留言页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/studentMessage/toAddPage")
    public ModelAndView toStudentAddMessagePage(ModelAndView modelAndView) {
        // 查询所有教师信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", "1");
        modelAndView.addObject("teacherList", userService.getListDataByParam(queryWrapper));
        modelAndView.setViewName("student/message/add");
        return modelAndView;
    }

    /**
     * 跳转到学生修改留言页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/studentMessage/toEditPage")
    public ModelAndView toStudentEditMessagePage(ModelAndView modelAndView, Integer id) {
        // 查询修改的信息
        modelAndView.addObject("obj", messageService.getById(id));
        // 查询所有教师信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", "1");
        modelAndView.addObject("teacherList", userService.getListDataByParam(queryWrapper));
        modelAndView.setViewName("student/message/edit");
        return modelAndView;
    }

    /**
     * 跳转到新增管理员页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/admin/toAddPage")
    public ModelAndView toAdminAddPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/admin/add");
        return modelAndView;
    }

    /**
     * 跳转到新增教师页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/adminTeacher/toAddPage")
    public ModelAndView toTeacherAddPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/teacher/add");
        return modelAndView;
    }

    /**
     * 跳转到修改教师页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/adminTeacher/toEditPage")
    public ModelAndView toTeacherAddPage(ModelAndView modelAndView, Integer id) {
        // 查询要修改的信息
        modelAndView.addObject("obj", userService.getById(id));
        modelAndView.setViewName("admin/teacher/edit");
        return modelAndView;
    }

    /**
     * 跳转到新增学生页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/adminStudent/toAddPage")
    public ModelAndView toStudentAddPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/student/add");
        return modelAndView;
    }

    /**
     * 跳转到修改学生页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/adminStudent/toEditPage")
    public ModelAndView toStudentAddPage(ModelAndView modelAndView, Integer id) {
        // 查询要修改的信息
        modelAndView.addObject("obj", userService.getById(id));
        modelAndView.setViewName("admin/student/edit");
        return modelAndView;
    }

    /**
     * 跳转到新增课程页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/course/toAddPage")
    public ModelAndView toCourseAddPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/course/add");
        return modelAndView;
    }

    /**
     * 跳转到修改课程页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/course/toEditPage")
    public ModelAndView toCourseAddPage(ModelAndView modelAndView, Integer id) {
        // 查询要修改的信息
        modelAndView.addObject("obj", courseService.getById(id));
        modelAndView.setViewName("admin/course/edit");
        return modelAndView;
    }

    /**
     * 跳转到新增试题页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/subject/toAddPage")
    public ModelAndView toSubjectAddPage(ModelAndView modelAndView) {
        // 查询所有课程用来选择
        modelAndView.addObject("courseList", courseService.getListData(null));
        modelAndView.setViewName("admin/subject/add");
        return modelAndView;
    }

    /**
     * 跳转到批量新增试题页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/subject/toAddAllPage")
    public ModelAndView toSubjectAddAllPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/subject/addAll");
        return modelAndView;
    }

    /**
     * 跳转到修改试题页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/subject/toEditPage")
    public ModelAndView toSubjectAddPage(ModelAndView modelAndView, Integer id) {
        // 查询要修改的信息
        modelAndView.addObject("obj", subjectService.getById(id));
        // 查询所有课程用来选择
        modelAndView.addObject("courseList", courseService.getListData(null));
        modelAndView.setViewName("admin/subject/edit");
        return modelAndView;
    }

    /**
     * 跳转到新增试卷页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/testPaper/toAddPage")
    public ModelAndView toTestPaperAddPage(ModelAndView modelAndView) {
        modelAndView.addObject("minDate", DateUtil.now());
        modelAndView.setViewName("admin/testPaper/add");
        return modelAndView;
    }

    /**
     * 跳转到组卷页面
     * @param modelAndView
     * @param selectType        组卷类型 0人工组卷  1自动组卷
     * @param testPaper         试卷基本信息
     *
     * @return
     */
    @GetMapping("/testPaper/toTestAddPage")
    public ModelAndView toTestPaperTestAddPage(ModelAndView modelAndView, Integer selectType, TestPaper testPaper) {
        // 将试卷基本信息也传入页面
        modelAndView.addObject("obj", testPaper);
        if(selectType == 0) {
            // 跳转人工组卷页面
            modelAndView.setViewName("admin/testPaper/person_select");
        } else {
            // 查询所有有试题的课程信息  根据试卷类型判断查询什么类型的题目
            // 试卷类型 0考试 1练习, 试题类型 0练习 1考试
            List<Course> courseList = courseService.getCourseInfoBySubjectInfo(testPaper.getType().equals("0") ? "1" : "0");
            modelAndView.addObject("courseList", courseList);
            // 跳转自动组卷页面
            modelAndView.setViewName("admin/testPaper/auto_select");
        }
        return modelAndView;
    }

    /**
     * 跳转到自动组卷输入条件页面
     * @param modelAndView
     * @param courseIds         选中的要组卷的课程ID
     * @param testPaper         试卷基本信息
     *
     * @return
     */
    @GetMapping("/testPaper/toAutoCreatePage")
    public ModelAndView toAutoTestPaperCreatePage(ModelAndView modelAndView, TestPaper testPaper, String courseIds) {
        // 将试卷基本信息也传入页面
        modelAndView.addObject("obj", testPaper);
        // 查询课程信息返回到页面上
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", StrUtil.split(courseIds, ',', true, true));
        modelAndView.addObject("courseList", courseService.getListData(queryWrapper));
        // 跳转自动组卷页面
        modelAndView.setViewName("admin/testPaper/auto_create");
        return modelAndView;
    }

    /**
     * 跳转到修改试卷页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/testPaper/toEditPage")
    public ModelAndView toTestPaperAddPage(ModelAndView modelAndView, Integer id) {
        // 查询要修改的信息
        modelAndView.addObject("obj", testPaperService.getById(id));
        modelAndView.setViewName("admin/testPaper/edit");
        return modelAndView;
    }

    /**
     * 跳转到查看试卷页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/testPaper/toFindPage")
    public ModelAndView toTestPaperFindPage(ModelAndView modelAndView, Integer id) {
        // 查询试题的信息
        modelAndView.addObject("obj", testPaperService.getById(id));
        modelAndView.setViewName("admin/testPaper/find");
        return modelAndView;
    }

    /**
     * 跳转到查看答题详情页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/examinationDetail/toFindPage")
    public ModelAndView toExaminationDetailFindPage(ModelAndView modelAndView, Integer id, Integer userId) {
        // 查询试题的信息
        modelAndView.addObject("obj", testPaperService.getById(id));
        modelAndView.addObject("userId", userId);
        modelAndView.setViewName("student/examinationDetail/find");
        return modelAndView;
    }

    /**
     * 跳转到开始考试页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/studentTestPaper/toStartExam")
    public ModelAndView toStartExam(ModelAndView modelAndView, Integer testPaperId) {
        // 查询试卷信息
        TestPaper testPaper = testPaperService.getById(testPaperId);
        // 根据试卷ID查询该试卷下的题目信息
        // 首先查询题目和试卷绑定信息
        QueryWrapper<TestPaperSubjectReal> testPaperSubjectRealQueryWrapper = new QueryWrapper<>();
        testPaperSubjectRealQueryWrapper.eq("test_paper_id", testPaperId);
        List<TestPaperSubjectReal> testPaperSubjectReals = testPaperSubjectRealService.getListByParam(testPaperSubjectRealQueryWrapper);
        // 循环获取查询到的题目信息ID
        List<Integer> subIds = new ArrayList<>(16);
        for (TestPaperSubjectReal testPaperSubjectReal : testPaperSubjectReals) {
            subIds.add(testPaperSubjectReal.getSubjectId());
        }
        QueryWrapper<Subject> subjectQueryWrapper = new QueryWrapper<Subject>();
        subjectQueryWrapper.in("id", subIds);
        List<Subject> subjectList = subjectService.getListByParam(subjectQueryWrapper);
        // 保存试卷信息和试题信息
        modelAndView.addObject("testPaper", testPaper);
        modelAndView.addObject("subjectList", subjectList);
        modelAndView.setViewName("student/testPaper/exam");
        return modelAndView;
    }

    /**
     * 跳转到修改试卷内试题的页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/testPaperSubjectReal/toEditPage")
    public ModelAndView toTestPaperSubjectRealEditPage(ModelAndView modelAndView, Integer testPaperId) {
        // 查询要修改的信息
        modelAndView.addObject("obj", testPaperService.getById(testPaperId));
        modelAndView.setViewName("admin/testPaper/edit_person_select");
        return modelAndView;
    }

    /**
     * 跳转到修改管理员信息页面
     * @param modelAndView
     * @return
     */
    @GetMapping("/user/toEditPage")
    public ModelAndView toEditAdminPage(ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.addObject("obj", ServletUtils.getAdminInfo(request));
        // 设置跳转页面
        modelAndView.setViewName("editInfo");
        return modelAndView;
    }

    /**
     * 学生自动生成练习卷
     * @param modelAndView
     *
     * @return
     */
    @GetMapping("/studentTestPaper/toTestAddPage")
    public ModelAndView toStudentTestPaperTestAddPage(ModelAndView modelAndView) {
        // 查询所有有试题的课程信息  根据试卷类型判断查询什么类型的题目
        // 试题类型 0练习 1考试
        List<Course> courseList = courseService.getCourseInfoBySubjectInfo("0");
        modelAndView.addObject("courseList", courseList);
        // 跳转自动组卷页面
        modelAndView.setViewName("student/testPaper/auto_select");
        return modelAndView;
    }

    // -----------------------------------------------------------------------------------------------------------------------------
    // -------------------------------------------- 控制后台管理页面跳转的控制器 End -------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------------------

}
