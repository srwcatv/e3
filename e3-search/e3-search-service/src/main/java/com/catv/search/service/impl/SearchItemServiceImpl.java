package com.catv.search.service.impl;

import com.catv.common.pojo.E3Result;
import com.catv.common.pojo.SearchItem;
import com.catv.common.pojo.SearchResult;
import com.catv.search.dao.SearchItemDao;
import com.catv.search.mapper.SearchItemMapper;
import com.catv.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private SearchItemMapper searchItemMapper;

    @Autowired
    private SolrServer solrServer;

    @Autowired
    private SearchItemDao searchItemDao;

    @Value("${default_field}")
    private String default_field;

    public E3Result importSearchItems() {
        try {
            //商品列表
            List<SearchItem> searchItemList = searchItemMapper.getItemList();
            //遍历商品列表
            for (SearchItem searchItem : searchItemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                //写入索引库
                solrServer.add(document);
            }
            //提交
            solrServer.commit();
            return E3Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return E3Result.build(500, "写入索引库失败");
        }
    }

    public SearchResult searchItemResult(String keyword, int page, int rows) throws Exception {
        //创建一个SolrQuery对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(keyword);
        //设置分页条件
        query.setStart((page - 1) * rows);
        //设置rows
        query.setRows(rows);
        //设置默认搜索域
        query.set("df", default_field);
        //设置高亮显示
        query.setHighlight(true);
        query.addHighlightField(default_field);
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        //执行查询
        SearchResult searchResult = searchItemDao.querySearchItems(query);
        //计算总页数
        int recourdCount = searchResult.getRecordCount();
        int pages = recourdCount / rows;
        if (recourdCount % rows > 0) {
            pages++;
        }
        //设置到返回结果
        searchResult.setTotalPages(pages);
        return searchResult;
    }
}
