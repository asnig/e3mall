package cn.e3mall.item.listener;

import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.pojo.TbItemDesc;
import cn.e3mall.item.pojo.Item;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XYQ
 */
public class HtmlGenListener implements MessageListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private String genHtmlPath;

    @Override
    public void onMessage(Message message) {
        try {
            // 从消息中获取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            // 等待事务
            Thread.sleep(1000);
            // 查询商品和查询商品描述
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc tbItemDesc = itemService.getTbItemDescByItemId(itemId);
            // 装载数据
            Map<String, Object> data = new HashMap<>(16);
            data.put("item", item);
            data.put("itemDesc", tbItemDesc);
            // 获取模板 生成html文件
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            FileWriter out = new FileWriter(genHtmlPath + itemId + ".html");
            template.process(data, out);
            // 关闭流
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
