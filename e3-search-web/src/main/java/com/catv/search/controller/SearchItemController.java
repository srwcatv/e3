package com.catv.search.controller;

import com.catv.common.pojo.SearchResult;
import com.catv.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 查询结果
 */
@Controller
public class SearchItemController {

    @Autowired
    private SearchItemService searchItemService;

    @Value("${ROWS}")
    private int rows;

    @RequestMapping("search")
    public String search(String keyword, @RequestParam(defaultValue = "1") int page, Model model) throws Exception {
        SearchResult result = searchItemService.searchItemResult(keyword, page, rows);
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("recourdCount", result.getRecordCount());
        model.addAttribute("itemList", result.getItemList());
        return "search";
    }
}
