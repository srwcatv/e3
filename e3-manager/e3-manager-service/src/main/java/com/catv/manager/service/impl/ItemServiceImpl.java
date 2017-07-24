package com.catv.manager.service.impl;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUIDataGridResult;
import com.catv.common.utils.IDUtils;
import com.catv.common.utils.JsonUtils;
import com.catv.common.utils.jedis.JedisClient;
import com.catv.manager.service.ItemService;
import com.catv.mapper.TbItemDescMapper;
import com.catv.mapper.TbItemMapper;
import com.catv.pojo.TbItem;
import com.catv.pojo.TbItemDesc;
import com.catv.pojo.TbItemExample;
import com.catv.pojo.TbItemExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * 商品管理Service
 * <p>Title: ItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 *
 * @version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ITEM_INFO_PRE}")
    private String ITEM_INFO_PRE;

    @Value("${ITEM_CACHE_EXPIRE}")
    private int ITEM_INFO_EXPIRE;

    @Resource
    private Destination topicDestination;

    public TbItem getItemById(long itemId) {
        try {
            //查询缓存
            String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                //把json转换为java对象
                TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据主键查询
        //TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example = new TbItemExample();
        Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            TbItem item = list.get(0);
            try {
                //把数据保存到缓存
                jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
                //设置缓存的有效期
                jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return item;
        }
        return null;
    }

    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //创建一个返回值对象
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        //取分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        //取总记录数
        long total = pageInfo.getTotal();
        result.setTotal(total);
        return result;
    }

    public E3Result addItem(TbItem item, String desc) {
        //生成商品id
        long itemId = IDUtils.genItemId();
        //补全item的属性
        item.setId(itemId);
        //1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(item);
        //创建一个商品描述表对应的pojo对象。
        TbItemDesc itemDesc = new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        int ret = itemDescMapper.insert(itemDesc);
        if (ret > 0) {
            //发送一个商品添加消息
            jmsTemplate.send(topicDestination, new MessageCreator() {

                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(itemId + "");
                    return textMessage;
                }
            });
        }
        //返回成功
        return E3Result.ok();
    }

    public TbItemDesc getItemDescById(Long itemId) {
        try {
            String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
            //判断缓存是否命中
            if (StringUtils.isNotBlank(json)) {
                //转换为java对象
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return itemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //根据主键查询
        TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);

        try {
            jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
            //设置过期时间
            jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemDesc;

    }
}
