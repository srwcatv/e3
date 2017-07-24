package com.catv.controller;

import com.catv.content.service.ContentService;
import com.catv.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 前台首页
 */
@Controller
public class IndexController {

    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;

    @Autowired
    private ContentService contentService;

    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model){
        //设置轮播图
        List<TbContent> contents = contentService.getListByContentCategoryId(CONTENT_LUNBO_ID);
        System.out.println(contents.size());
        model.addAttribute("ad1List", contents);
        return "index";
    }
}
