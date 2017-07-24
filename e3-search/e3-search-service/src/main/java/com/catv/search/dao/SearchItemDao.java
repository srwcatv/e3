package com.catv.search.dao;

import com.catv.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * 查询结果
 */
public interface SearchItemDao {

    SearchResult querySearchItems(SolrQuery query) throws  Exception;
}
