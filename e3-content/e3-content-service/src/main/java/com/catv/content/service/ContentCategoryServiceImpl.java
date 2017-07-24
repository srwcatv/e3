package com.catv.content.service;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUITreeNode;
import com.catv.mapper.TbContentCategoryMapper;
import com.catv.pojo.TbContentCategory;
import com.catv.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    public List<EasyUITreeNode> getEasyUITreeNode(Long parentId) {
        List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(example);
        for (TbContentCategory tbContentCategory : tbContentCategories) {
            EasyUITreeNode treeNode = new EasyUITreeNode();
            treeNode.setText(tbContentCategory.getName());
            treeNode.setId(tbContentCategory.getId());
            treeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            easyUITreeNodes.add(treeNode);
        }
        return easyUITreeNodes;
    }

    public E3Result addContentCategory(Long parentId, String name) {
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        parent.setIsParent(true);
        parent.setUpdated(new Date());
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setCreated(new Date());
        contentCategory.setIsParent(false);
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        contentCategory.setStatus(TbContentCategory.STATUS_NORMAL);
        contentCategory.setUpdated(new Date());
        contentCategory.setSortOrder(1);
        tbContentCategoryMapper.insert(contentCategory);
        tbContentCategoryMapper.updateByPrimaryKeySelective(parent);
        E3Result result = new E3Result();
        result.setData(contentCategory);
        result.setStatus(200);
        return result;
    }
}
