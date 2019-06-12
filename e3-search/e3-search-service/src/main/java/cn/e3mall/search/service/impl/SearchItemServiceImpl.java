package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author XYQ
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public E3Result importAllItems() {
        List<SearchItem> itemList = itemMapper.getItemList();
        try {
            for (SearchItem item : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id", item.getId());
                document.addField("item_title", item.getTitle());
                document.addField("item_price", item.getPrice());
                document.addField("item_image", item.getImage());
                document.addField("item_category_name", item.getCategory_name());
                solrServer.add(document);
            }
            solrServer.commit();
            return E3Result.ok();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return E3Result.build(500, "数据导入时发生异常");
        }
    }
}
