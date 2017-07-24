package com.catv.manager.service;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUIDataGridResult;
import com.catv.pojo.TbItem;
import com.catv.pojo.TbItemDesc;

public interface ItemService {

	TbItem getItemById(long itemId);

	EasyUIDataGridResult getItemList(int page, int rows);

    E3Result addItem(TbItem item, String desc);

    TbItemDesc getItemDescById(Long itemId);
}
