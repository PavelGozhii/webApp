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
    <title>ProductPage</title>
</head>
<body>
<div style="text-align: center;">
    <h1>Product</h1>
    <h2>
        <a href="${pageContext.request.contextPath}/product/create">Add New Product</a>
        &nbsp; &nbsp; &nbsp;
        <a href="${pageContext.request.contextPath}/product/view">List All Product</a>
    </h2>
</div>
<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of Product</h2></caption>
        <tr>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td><c:out value="${product.name}"/></td>
                <td><c:out value="${product.category}"/></td>
                <td><c:out value="${product.price}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/product/edit?id=<c:out value='${product.id}' />">Edit</a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="${pageContext.request.contextPath}/product/delete?id=<c:out value='${product.id}' />">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <a href="/home">Home</a>
</div>
</body>
</html>
