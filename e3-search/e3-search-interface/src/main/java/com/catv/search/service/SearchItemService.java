package com.catv.search.service;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.SearchResult;

/**
 * 搜索
 */
public interface SearchItemService {

    /**
     * 导入商品到索引库
     * @return
     */
    E3Result importSearchItems();

    /**
     * 返回查询结果
     * @param keyword
     * @param page
     * @param rows
     * @return
     */
    SearchResult searchItemResult(String keyword,int page,int rows) throws Exception;
}
