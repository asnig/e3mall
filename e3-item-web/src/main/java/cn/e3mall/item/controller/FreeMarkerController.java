package cn.e3mall.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试生成FreeMarker
 *
 * @author XYQ
 */
@Controller
public class FreeMarkerController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genHTML")
    @ResponseBody
    public String  genHtml() throws Exception {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "aaa", 18));
        list.add(new Student(2, "bbb", 19));
        list.add(new Student(3, "ccc", 20));
        list.add(new Student(4, "ddd", 21));
        list.add(new Student(5, "eee", 22));
        // 准备数据
        Map<String, Object> data = new HashMap<>(16);
        data.put("list", list);
        Writer out = new FileWriter(new File("E:/test.html"));
        template.process(data, out);
        out.close();
        return "OK";
    }
}
