package com.project.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.commons.model.Course;
import com.project.commons.model.Subject;
import com.project.commons.model.ResultMessage;
import com.project.commons.service.CourseService;
import com.project.commons.service.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 斗佛
 * @Date 2022/3/27
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 * 题库控制器
 */
@RestController
@RequestMapping("/subject")
@AllArgsConstructor
public class SubjectController {

    private SubjectService subjectService;

    private CourseService courseService;

    /**
     * 分页获取表格数据
     * @param subject
     * @return
     */
    @GetMapping("getListByPage")
    public ResultMessage<List<Subject>> getListByPage(Integer page, Integer limit, Subject subject) {
        try {
            // 调用分页查询方法
            IPage<Subject> data = subjectService.getListByPage(subject, page, limit);
            return new ResultMessage<List<Subject>>(0, "查询成功！", data.getTotal(), data.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 新增或修改信息
     * @param subject
     * @return
     */
    @PostMapping("saveOrUpdate")
    public ResultMessage<Subject> saveOrUpdate(Subject subject) {
        try {
            // 如果是新增, 默认不删除
            if(subject.getId() == null) {
                subject.setDelFlag("0");
            }
            // 调用方法
            boolean result = subjectService.saveOrUpdate(subject);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<Subject>(0, "操作成功！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 删除信息
     * @return
     */
    @DeleteMapping("deleteInfo")
    public ResultMessage<Subject> deleteInfo(String ids) {
        try {
            // 调用方法
            boolean result = subjectService.deleteInfo(ids);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            return new ResultMessage<Subject>(0, "操作成功！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }

    /**
     * 生成试题Excel并提供下载
     */
    @GetMapping("/downLoadFile")
    public void downLoadFile(HttpServletResponse response) {
        try {
            List<Object> exportData = new ArrayList<>(16);
            exportData.add(CollectionUtil.newArrayList(
                    "所属课程的ID, 参考sheet2中的数据",
                    "题目标题, 不超过300字",
                    "最少输入两个选项",
                    "最少输入两个选项",
                    "最少输入两个选项",
                    "最少输入两个选项",
                    "答案, 直接输入选项的编号, 例如1, 多个用逗号隔开, 例如: 1,2,3",
                    "分值, 正整数",
                    "题目类型: 0单选题 1多选题 2判断题",
                    "题目类别: 0练习题 1考试题"
            ));
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.setColumnWidth(0, 30);
            writer.setColumnWidth(1, 23);
            writer.setColumnWidth(2, 18);
            writer.setColumnWidth(3, 18);
            writer.setColumnWidth(4, 18);
            writer.setColumnWidth(5, 18);
            writer.setColumnWidth(6, 25);
            writer.setColumnWidth(7, 16);
            writer.setColumnWidth(8, 35);
            writer.setColumnWidth(9, 25);
            writer.setDefaultRowHeight(21);
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.writeHeadRow(CollectionUtil.newArrayList("所属课程ID", "题目标题", "选项1", "选项2", "选项3",
                    "选项4", "题目答案", "分值", "题目类型", "题目类别"));
            writer.write(exportData, false);
            // 创建Sheet2
            ExcelWriter writerTypeSheet = writer.setSheet("sheet2");
            writerTypeSheet.setDefaultRowHeight(21);
            writerTypeSheet.setColumnWidth(0, 30);
            writerTypeSheet.setColumnWidth(1, 30);
            writerTypeSheet.writeHeadRow(CollectionUtil.newArrayList("课程ID", "课程名"));
            // 查询所有课程信息
            List<Course> getAllTypeInfo = courseService.getListData(null);
            List<Object> typeData = new ArrayList<>(16);
            getAllTypeInfo.forEach(item -> {
                typeData.add(CollectionUtil.newArrayList(
                        item.getId(), item.getCourseName()
                ));
            });
            writerTypeSheet.write(typeData, false);
            //out为OutputStream，需要写出到的目标流
            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            String fileName = "题库导入模板文件" + DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + ".xls";
            // 中文字处理, 防止文件名乱码
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
            ServletOutputStream out=response.getOutputStream();
            writer.flush(out, true);
            // 关闭writer，释放内存
            writer.close();
            //此处记得关闭输出Servlet流
            IoUtil.close(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Excel数据导入
     * @return
     */
    @PostMapping("/importExcelData")
    public ResultMessage importExcelData(@RequestParam("file") MultipartFile file) {
        try {
            if(file == null) {
                return ResultMessage.warn(2, "文件接收失败");
            }
            // 接收到文件,调用Service层方法进行数据导入处理
            boolean result = subjectService.insertByExcel(file.getInputStream());
            if(!result) {
                return ResultMessage.warn(2, "操作失败！");
            }
            return ResultMessage.success("操作成功！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }
}
