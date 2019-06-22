package cn.e3mall.search.message;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author XYQ
 */
public class ItemAddMessageListener implements MessageListener {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try {
            // 获取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = Long.valueOf(text);
            // 等待事务提交
            Thread.sleep(100);
            // 通过商品id获取SearchItem对象
            SearchItem searchItem = itemMapper.getSearchItemByItemId(itemId);
            // 同步到solr索引库
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            solrServer.add(document);
            // 提交
            solrServer.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
