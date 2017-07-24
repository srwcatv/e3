package com.catv.controller;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUIDataGridResult;
import com.catv.content.service.ContentService;
import com.catv.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理
 */
@Controller
public class ContentController {

    @Autowired
    ContentService contentService;

    /**
     * 内容管理列表
     * @param page
     * @param rows
     * @param categoryId
     * @return
     */
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult contentList(Integer page, Integer rows,Long categoryId){
        return contentService.getContentList(page, rows,categoryId);
    }

    /**
     * 保存
     * @param content
     * @return
     */
    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result contentSave(TbContent content){
        return contentService.save(content);
    }
}
