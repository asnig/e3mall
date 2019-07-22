* ### 解决请求*.html后缀无法返回json数据的问题

  > 在SpringMVC中请求*.html不可以返回json数据。
  >
  > 修改web.xml，添加url拦截格式。

  AJAX请求jsonp格式的数据：

  ```javascript
  $.post("/cart/update/num/" + _thisInput.attr("itemId") + "/" + _thisInput.val() + ".action", function (data) {
      CART.refreshTotalPrice();
  });
  ```

  修改web.xml

  ```xml
  <servlet-mapping>
    <servlet-name>e3-cart-web</servlet-name>
    <url-pattern>*.html</url-pattern>
  </servlet-mapping>
  <servlet-mapping>
    <servlet-name>e3-cart-web</servlet-name>
    <url-pattern>*.action</url-pattern>
  </servlet-mapping>
  ```

* ### 登录拦截器的实现方式

  ```java
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      // 从cookie中取token
      String token = CookieUtils.getCookieValue(request, "token", true);
      // 没有token，未登录。放行
      if (StringUtils.isBlank(token)) {
          return true;
      }
      // 有token，调用sso服务判断是否session过期
      E3Result result = tokenService.getUserByToken(token);
      // 过期。未登录。放行
      if (result.getStatus() == EXPIRE_CODE) {
          return true;
      }
      // 没有过期。把用户信息放入request域中。放行
      TbUser user = (TbUser) result.getData();
      request.setAttribute("user", user);
      return true;
  }
  ```

  