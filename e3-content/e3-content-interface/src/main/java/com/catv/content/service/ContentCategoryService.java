package com.catv.content.service;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 内容管理
 */
public interface ContentCategoryService {
    /**
     * 内容分类树
     * @param parentId
     * @return
     */
    List<EasyUITreeNode> getEasyUITreeNode(Long parentId);

    /**
     * 内容分类创建
     * @param parentId
     * @param name
     * @return
     */
    E3Result addContentCategory(Long parentId, String name);
}
