<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
</head>
<body>

<div class="container">
    <form action="/memberInput" method="post">
        <input type="text" name="id">
        <input type="text" name="pw">
        <input type="text" name="userName">
    </form>
</div>

</body>
</html>