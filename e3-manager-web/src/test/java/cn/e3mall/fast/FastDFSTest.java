package cn.e3mall.fast;

import cn.e3mall.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.util.Date;

/**
 * @author XYQ
 */
public class FastDFSTest {
    @Test
    public void fun1() throws Exception {
        ClientGlobal.init("C:\\Users\\10727\\IdeaProjects\\e3mall\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = null;
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        String[] strings = storageClient.upload_file("F:\\整理桌面用\\常用文件夹\\Meier\\TIM图片20190103163747.jpg", "jpg", null);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void fun2() throws Exception {
        FastDFSClient f = new FastDFSClient("C:\\Users\\10727\\IdeaProjects\\e3mall\\e3-manager-web\\src\\main\\resources\\conf\\client.conf");
        String s = f.uploadFile("F:\\整理桌面用\\常用文件夹\\Meier\\TIM图片20190103163747.jpg");
        System.out.println(s);
    }

    @Test
    public void fun3() {
        System.out.println(new Date());
    }

}
