package com.project.commons.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.project.commons.model.ResultMessage;
import com.project.commons.model.User;
import com.project.commons.service.UserService;
import com.project.commons.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 斗佛Uncle
 * 公共的控制器
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Controller
@RequestMapping("/public")
public class PublicController {

    @Value("${me.uploadPath:null}")
    private String uploadPath;

    @Value("${me.showPath:null}")
    private String showPath;

    @Autowired
    private UserService userService;

    /**
     * 上传到服务器
     * @param file
     * @return
     */
    @PostMapping("/uploadImages")
    @ResponseBody
    public ResultMessage toUploadImagesOverride(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(16);
        String fileName = "";
        try {
            // 设置文件存放路径
            String path = ResourceUtils.getURL("classpath:").getPath().replace("%20"," ")+"/static/upload/";
            if(!StrUtil.isBlankOrUndefined(this.uploadPath)) {
                path = uploadPath;
            }
            System.out.println("=============path==============：" + path);
            // 判断文件是否为空
            if (file.isEmpty() || file.getSize() <= 0) {
                return new ResultMessage(207, "上传文件为空！");
            }
            // 获取上传文件的名字
            fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
            // 创建文件
            File dest = new File(path + fileName);
            // 检测目录是否存在
            if (!dest.getParentFile().exists()) {
                // 不存在就创建
                dest.getParentFile().mkdir();
            }
            // 文件写入
            file.transferTo(dest);
            if(StrUtil.isBlankOrUndefined(this.showPath)) {
                result.put("imgUrl", ServletUtils.getProjectHttpUrl(request) + "upload/" + fileName);
                result.put("location", ServletUtils.getProjectHttpUrl(request) + "upload/" + fileName);
            } else {
                result.put("imgUrl", this.showPath + fileName);
                result.put("location", this.showPath + fileName);
            }
            result.put("imgName", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(0, "出现异常：" + e.getMessage());
        }
        return new ResultMessage<Map<String, Object>>(1, "上传成功！", result);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PostMapping("userUpdateInfo")
    @ResponseBody
    public ResultMessage<User> saveOrUpdate(User user, HttpSession session) {
        try {
            // 判断身份证号是否合法
            if(!IdcardUtil.isValidCard(user.getIdCardNum())) {
                throw new RuntimeException("身份证格式有误！");
            }
            // 解析性别
            user.setSex(IdcardUtil.getGenderByIdCard(user.getIdCardNum()) + "");
            // 调用方法
            boolean result = userService.insertOrUpdate(user);
            if(!result) {
                return ResultMessage.warn(2, "操作失败！请重试！");
            }
            session.removeAttribute("userInfo");
            return new ResultMessage<User>(1, "操作成功！请重新登录！");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResultMessage.warn(2, "出现错误：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessage.danger(500, "接口出现异常: " + e.getMessage());
        }
    }
}
