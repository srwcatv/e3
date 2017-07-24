package com.catv.controller;

import com.catv.common.pojo.EasyUITreeNode;
import com.catv.manager.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品分类管理
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> itemCatList(@RequestParam(name = "id",defaultValue = "0") Long parentId) {

        return itemCatService.getEasyUITreeNode(parentId);
    }
}
