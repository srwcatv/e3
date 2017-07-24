package com.catv.search.mapper;

import com.catv.common.pojo.SearchItem;

import java.util.List;

/**
 * 搜索
 */
public interface SearchItemMapper {
    List<SearchItem> getItemList();
    SearchItem getSearchItemByItemId(Long itemId);
}
