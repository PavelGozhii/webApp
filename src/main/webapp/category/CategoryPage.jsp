<%--
  Created by IntelliJ IDEA.
  User: Павел
  Date: 03.06.2019
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>CategoryPage</title>
</head>
<body>
<div style="text-align: center;">
    <h1>Category</h1>
    <h2>
        <a href="/category/create">Add New Category</a>
        &nbsp; &nbsp; &nbsp;
        <a href="/category/view">List All Category</a>
    </h2>
</div>
<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of Category</h2></caption>
        <tr>
            <th>Name</th>
            <th>Description</th>
        </tr>
        <c:forEach var="category" items="${categories}">
            <tr>
                <td><c:out value="${category.name}"/></td>
                <td><c:out value="${category.description}"/></td>
                <td>
                    <a href="/category/edit?id=<c:out value='${category.id}' />">Edit</a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="/category/delete?id=<c:out value='${category.id}' />">Delete</a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="/product/view?id=<c:out value='${category.id}'/>">View Product</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="/home">Home</a>
</div>
</body>
</html>
