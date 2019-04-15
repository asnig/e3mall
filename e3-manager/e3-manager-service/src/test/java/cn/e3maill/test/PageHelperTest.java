package cn.e3maill.test;

import cn.e3mall.common.pojo.TbItem;
import cn.e3mall.common.pojo.TbItemExample;
import cn.e3mall.mapper.TbItemMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author XYQ
 */
public class PageHelperTest {
    @Test
    public void fun1() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
        TbItemMapper mapper = context.getBean(TbItemMapper.class);
        TbItemExample example = new TbItemExample();
        PageHelper.startPage(1, 10);
        List<TbItem> tbItems = mapper.selectByExample(example);
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        System.out.println(pageInfo.getTotal());
        System.out.println(pageInfo.getPages());
    }
}
