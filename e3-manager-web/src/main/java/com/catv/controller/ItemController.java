package com.catv.controller;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUIDataGridResult;
import com.catv.pojo.TbItem;
import com.catv.manager.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品
 */
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 商品列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        //调用服务查询商品列表
        return itemService.getItemList(page, rows);
    }

    /**
     * 商品添加功能
     */
    @RequestMapping(value="/item/save", method= RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item, String desc) {
        E3Result result = itemService.addItem(item, desc);
        return result;
    }
}
