package com.catv.controller;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUITreeNode;
import com.catv.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理
 */
@Controller
public class ContentCategoryController {

    @Autowired
    ContentCategoryService contentCategoryService;

    /**
     * 分类列表
     * @param parentId
     * @return
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> contentCategoryList(@RequestParam(name = "id",defaultValue = "0") Long parentId){
        return contentCategoryService.getEasyUITreeNode(parentId);
    }

    /**
     * 添加分类节点
     */
    @RequestMapping("/content/category/create")
    @ResponseBody
    public E3Result createContentCategory(Long parentId, String name) {
        //调用服务添加节点
        return contentCategoryService.addContentCategory(parentId, name);
    }
}
