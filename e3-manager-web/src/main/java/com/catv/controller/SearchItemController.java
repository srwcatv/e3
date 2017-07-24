package com.catv.controller;

import com.catv.common.pojo.E3Result;
import com.catv.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品导入索引库
 */
@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importSearchItems(){
        E3Result e3Result = searchItemService.importSearchItems();
        return e3Result;
    }
}
