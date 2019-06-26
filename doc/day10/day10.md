# FreeMarker的使用

* 引入jar包

  ```xml
  <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context-support</artifactId>
      <version>${spring.version}</version>
  </dependency>
  ```

* 在springmvc.xml文件中添加配置：

  ```xml
  <!--    FreeMaker-->
  <bean class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
      <property name="templateLoaderPath" value="/WEB-INF/ftl"/>
      <property name="defaultEncoding" value="UTF-8"/>
  </bean>
  ```

* 使用FreeMarker

  ```java
  // 注入FreeMarkerConfigurer
  @Autowired
  private FreeMarkerConfigurer freeMarkerConfigurer;
  ```

  ```java
  // 获取模板 生成html文件
  Configuration configuration = freeMarkerConfigurer.getConfiguration();
  Template template = configuration.getTemplate("item.ftl");
  FileWriter out = new FileWriter(genHtmlPath + itemId + ".html");
  template.process(data, out);
  // 关闭流
  out.close();
  ```

  