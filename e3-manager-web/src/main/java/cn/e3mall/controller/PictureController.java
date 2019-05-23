package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XYQ
 */
@Controller
public class PictureController {

    @Value("${FAST_IP_ADDRESS}")
    private String FAST_IP_ADDRESS;

    @RequestMapping(value = "/pic/upload")
    @ResponseBody
    public Map uploadPic(MultipartFile uploadFile) {
        Map<String, Object> result = new HashMap<>(16);
        try {
            FastDFSClient client = new FastDFSClient("classpath:conf/client.conf");
            String orgName = uploadFile.getOriginalFilename();
            String exname = orgName.substring(orgName.lastIndexOf('.') + 1);
            String url = client.uploadFile(uploadFile.getBytes(), exname);
            url = FAST_IP_ADDRESS + url;
            result.put("error", 0);
            result.put("url", url);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", 1);
            result.put("message", "图片上传失败!");
        }
        return result;
    }
}
