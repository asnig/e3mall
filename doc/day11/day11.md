# 实现注册功能时的技术要点

* 数据校验的方法

  ```java
  /**
       * 表单验证
       *
       * @param param 需要校验的数据
       * @param type  校验的数据类型 1、用户名 2、手机号 3、邮箱
       * @return E3Result
       */
      @Override
      public E3Result checkData(String param, int type) {
          TbUserExample example = new TbUserExample();
          TbUserExample.Criteria criteria = example.createCriteria();
          // 根据数据类型的不同设置不同的条件
          if (type == USERNAME_TYPE) {
              criteria.andUsernameEqualTo(param);
          } else if (type == PHONE_TYPE) {
              criteria.andPhoneEqualTo(param);
          } else if (type == EMAIL_TYPE) {
              criteria.andEmailEqualTo(param);
          } else {
              return E3Result.build(400, "数据类型错误");
          }
          // 按条件查询数据库
          List<TbUser> tbUsers = userMapper.selectByExample(example);
          if (tbUsers != null && tbUsers.size() > 0) {
              return E3Result.ok(false);
          }
  
          return E3Result.ok(true);
      }
  ```

* 注册功能的实现

  ```java
  /**
   * 注册用户
   *
   * @param user 用户填写的表单数据封装到user对象中
   * @return E3Result
   */
  @Override
  public E3Result doRegister(TbUser user) {
      // 非空校验
      boolean isBlank = (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
              || StringUtils.isBlank(user.getEmail()));
      if (isBlank) {
          return E3Result.build(400, "用户数据不完整，注册失败！");
      }
      // 后端表单验证
      E3Result result = checkData(user.getUsername(), 1);
      if (!(boolean) result.getData()) {
          return E3Result.build(400, "此用户名已被占用");
      }
      result = checkData(user.getPhone(), 2);
      if (!(boolean) result.getData()) {
          return E3Result.build(400, "此电话号码已被占用");
      }
      // 补全user对象
      user.setCreated(new Date());
      user.setUpdated(new Date());
      //对密码进行md5加密
      String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
      user.setPassword(md5Pass);
      userMapper.insert(user);
      return E3Result.ok();
  }
  ```

# 实现登录功能时的技术要点

* 登录的实现方法，使用md5加密密码，使用UUID生成token

  ```java
  /**
       * 用户登录业务
       *
       * @param username 用户名
       * @param password 密码
       * @return E3Result
       */
      @Override
      public E3Result doLogin(String username, String password) {
          TbUserExample example = new TbUserExample();
          TbUserExample.Criteria criteria = example.createCriteria();
          criteria.andUsernameEqualTo(username);
          // 判断用户名是否正确
          List<TbUser> users = userMapper.selectByExample(example);
          if (users == null || users.size() == 0) {
              // 返回用户名错误
              return E3Result.build(400, "用户名或密码错误!");
          }
          // 获取用户信息
          TbUser tbUser = users.get(0);
          // 判断密码是否正确
          if (DigestUtils.md5DigestAsHex(password.getBytes()).equals(tbUser.getPassword())) {
              // 返回密码错误
              return E3Result.build(400, "用户名或密码错误！");
          }
          // 生成token
          String token = UUID.randomUUID().toString().replace("-", "");
          // 不能把用户密码放入token
          tbUser.setPassword(null);
          jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(tbUser));
          // 设置token过期时间
          jedisClient.expire("SESSION:" + token, sessionExpireTime);
          //返回token
          return E3Result.ok(token);
      }
  ```

* 写在客户端的JS脚本，实现跨域访问数据

  ```java
  var E3MALL = {
  	checkLogin : function(){
  		var _ticket = $.cookie("token");
  		if(!_ticket){
  			return ;
  		}
  		$.ajax({
  			url : "http://localhost:8088/user/token/" + _ticket,
  			dataType : "jsonp",
  			type : "GET",
  			success : function(data){
  				if(data.status === 200){
  					var username = data.data.username;
  					var html = username + "，欢迎来到宜立方购物网！<a href=\"http://www.e3mall.cn/user/logout.html\" class=\"link-logout\">[退出]</a>";
  					$("#loginbar").html(html);
  				}
  			}
  		});
  	}
  };
  
  $(function(){
  	// 查看是否已经登录，如果已经登录查询登录信息
  	E3MALL.checkLogin();
  });
  ```

  