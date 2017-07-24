package com.catv.manager.service;

import com.catv.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 商品分类
 */
public interface ItemCatService {

    /**
     * 商品分类树节点
     * @param parentId
     * @return
     */
	List<EasyUITreeNode> getEasyUITreeNode(Long parentId);
}
