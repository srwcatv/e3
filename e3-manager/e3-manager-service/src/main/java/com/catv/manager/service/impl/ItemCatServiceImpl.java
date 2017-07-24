package com.catv.manager.service.impl;

import com.catv.common.pojo.EasyUITreeNode;
import com.catv.mapper.TbItemCatMapper;
import com.catv.pojo.TbItemCat;
import com.catv.pojo.TbItemCatExample;
import com.catv.manager.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    public List<EasyUITreeNode> getEasyUITreeNode(Long parentId) {
        List<EasyUITreeNode> easyUITreeNodes = new ArrayList<>();
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> itemCats =  itemCatMapper.selectByExample(example);
        for (TbItemCat itemCat:itemCats){
           EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
           easyUITreeNode.setId(itemCat.getId());
           easyUITreeNode.setState(!itemCat.getIsParent()?"open":"closed");
           easyUITreeNode.setText(itemCat.getName());
           easyUITreeNodes.add(easyUITreeNode);
        }
        return easyUITreeNodes;
    }
}
