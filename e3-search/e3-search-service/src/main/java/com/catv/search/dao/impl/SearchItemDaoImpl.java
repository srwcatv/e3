package com.catv.search.dao.impl;

import com.catv.common.pojo.SearchItem;
import com.catv.common.pojo.SearchResult;
import com.catv.search.dao.SearchItemDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 返回查询结果
 */
@Repository
public class SearchItemDaoImpl implements SearchItemDao {

    @Autowired
    private SolrServer solrServer;

    /**
     * 返回查询结果
     * @param query 查询条件
     * @return
     * @throws Exception
     */
    public SearchResult querySearchItems(SolrQuery query) throws Exception {
        List<SearchItem> searchItems = new ArrayList<>();
        SearchResult result = new SearchResult();
        //执行查询
        QueryResponse response = solrServer.query(query);
        //取出查询结果
        SolrDocumentList documents = response.getResults();
        //取出高亮显示结果
        Map<String,Map<String,List<String>>> highlightings = response.getHighlighting();
        //遍历
        for (SolrDocument document : documents) {
            SearchItem item = new SearchItem();
            item.setId((String) document.get("id"));
            item.setCategory_name((String) document.get("item_category_name"));
            item.setPrice((Long) document.get("item_price"));
            item.setImage((String) document.get("item_image"));
            item.setSell_point((String) document.get("item_sell_point"));
            List<String> titles = highlightings.get(document.get("id")).get("item_title");
            if (titles != null && !titles.isEmpty() ){
                item.setTitle(titles.get(0));
            } else {
                item.setTitle((String) document.get("item_title"));
            }
            searchItems.add(item);
        }
        result.setItemList(searchItems);
        result.setRecordCount((int) documents.getNumFound());
        return result;
    }

    public SearchItem querySearchItemByItemId(Long itemId) {
        return null;
    }
}
