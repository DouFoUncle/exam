package com.project.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 自定义的异常跳转页
 * @Description 下一位读我代码的人, 有任何疑问请联系我, qq: 943701114
 */
@Controller
@RequestMapping("/error")
public class MyErrorPageController {

    /**
     * 404错误的跳转地址
     * @return
     */
    @RequestMapping("error-404")
    public String toPage404(){
        return "error/404";
    }

    /**
     * 403错误的跳转地址
     * @return
     */
    @RequestMapping("error-403")
    public String toPage403(){
        return "error/403";
    }

    /**
     * 500错误的跳转地址
     * @return
     */
    @RequestMapping("error-500")
    public String toPage500(){
        return "error/500";
    }
}
