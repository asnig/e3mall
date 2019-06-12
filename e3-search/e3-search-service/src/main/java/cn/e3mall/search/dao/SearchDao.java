package cn.e3mall.search.dao;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
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
 * 搜索索引库Dao
 *
 * @author XYQ
 */
@Repository
public class SearchDao {
    @Autowired
    private SolrServer solrServer;

    public SearchResult search(SolrQuery query) throws Exception {
        SearchResult result = new SearchResult();
        QueryResponse queryResponse = solrServer.query(query);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        List<SearchItem> itemList = new ArrayList<>();
        //获取高亮
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem item = new SearchItem();
            item.setId((String) solrDocument.get("id"));
            item.setPrice((Long) solrDocument.get("item_price"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            itemList.add(item);
        }
        result.setItemList(itemList);
        //设置总记录数
        result.setRecordCount(solrDocumentList.getNumFound());
        return result;
    }
}
