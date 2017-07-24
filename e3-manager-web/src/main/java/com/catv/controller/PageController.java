package com.catv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转
 */
@Controller
public class PageController {

    /**
     * 首页
     * @return
     */
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    /**
     * 跳转到请求页面
     * @param page 请求页面
     * @return
     */
    @RequestMapping("{page}")
    public String page(@PathVariable String page){
        return page;
    }
}
