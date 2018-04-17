<%--
  Created by IntelliJ IDEA.
  User: hangx
  Date: 2018/4/17
  Time: 20:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆页面</title>
</head>
<body>
<h1>登陆</h1>
<form action="/loginuser" method="post">
    <input type="text" name="username" />
    <input type="password" name="password" />
    <input type="submit" value="提交" />
</form>
</body>
</html>
