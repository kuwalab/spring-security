<%@page contentType="text/html; charset=utf-8" %><%--
--%><!DOCTYPE html>
<html>
 <head>
  <meta charset="utf-8">
  <title>ログイン</title>
 </head>
 <body>
  <form action="<c:url value='j_spring_security_check'/>" method="post">
   ユーザーID: <input type="text" name="j_username"><br>
   パスワード: <input type="text" name="j_password"><br>
   <input type="submit" value="ログイン">
  </form>
 </body>
</html>
