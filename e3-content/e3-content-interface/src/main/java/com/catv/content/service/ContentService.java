package com.catv.content.service;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUIDataGridResult;
import com.catv.pojo.TbContent;

import java.util.List;

/**
 * 内容管理
 */
public interface ContentService {
    /**
     * 内容管理分页列表
     * @param page
     * @param rows
     * @param categoryId
     * @return
     */
    EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId);

    /**
     * 保存一条内容
     * @param content
     * @return
     */
    E3Result save(TbContent content);

    List<TbContent> getListByContentCategoryId(Long content_lunbo_id);
}
