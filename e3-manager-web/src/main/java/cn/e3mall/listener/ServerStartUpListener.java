package cn.e3mall.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author XYQ
 */
public class ServerStartUpListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 将web应用名称（路径）保存到application范围中
        ServletContext application = sce.getServletContext();
        String path = application.getContextPath();
        application.setAttribute("APP_PATH", path);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
