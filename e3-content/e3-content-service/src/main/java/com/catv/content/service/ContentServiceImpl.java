package com.catv.content.service;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.EasyUIDataGridResult;
import com.catv.common.utils.JsonUtils;
import com.catv.common.utils.jedis.JedisClient;
import com.catv.mapper.TbContentMapper;
import com.catv.pojo.TbContent;
import com.catv.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/18.
 * 内容管理
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper ContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;
    
    public EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {
        PageHelper.startPage(page, rows);
        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(ContentMapper.selectByExample(contentExample));
        return new EasyUIDataGridResult(pageInfo.getTotal(),pageInfo.getList());
    }

    public E3Result save(TbContent content) {
        jedisClient.hdel(CONTENT_LIST,content.getCategoryId().toString());
        content.setCreated(new Date());
        content.setUpdated(new Date());
        ContentMapper.insertSelective(content);
        E3Result result = new E3Result();
        result.setStatus(200);
        return result;
    }

    public List<TbContent> getListByContentCategoryId(Long content_lunbo_id) {
        //判断缓存中是否有对应的数据
        try {
            String contentList = jedisClient.hget(CONTENT_LIST,content_lunbo_id+"");
            if (StringUtils.hasLength(contentList)){
                return JsonUtils.jsonToList(contentList,TbContent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbContentExample contentExample = new TbContentExample();
        TbContentExample.Criteria criteria = contentExample.createCriteria();
        criteria.andCategoryIdEqualTo(content_lunbo_id);
        List<TbContent> contents = ContentMapper.selectByExample(contentExample);
        try {
            jedisClient.hset(CONTENT_LIST,content_lunbo_id+"",JsonUtils.objectToJson(contents));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }
}
