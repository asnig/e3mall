package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author XYQ
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;
    @Autowired
    private SolrServer solrServer;

    @Override
    public SearchResult search(String keyword, int page, int rows) throws Exception {
        // 设置查询条件
        SolrQuery query = new SolrQuery();
        // 设置关键字
        query.setQuery(keyword);
        // 设置分页
        if (page <= 0) {
            page = 1;
        }
        query.setStart((page - 1) * rows);
        query.setRows(rows);
        // 设置默认域
        query.set("df", "item_title");
        // 设置高亮
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        // 查询结果
        SearchResult searchResult = searchDao.search(query);
        // 获取总记录数
        long recordCount = searchResult.getRecordCount();
        // 计算总页数
        int totalPages = (int) (recordCount % rows == 0 ? recordCount / rows : recordCount / rows + 1);
        searchResult.setTotalPages(totalPages);
        // 返回结果
        return searchResult;
    }
}
