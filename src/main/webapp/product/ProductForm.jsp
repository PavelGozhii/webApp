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
    <c:if test="${product != null}">
        <title>Edit product</title>
    </c:if>
    <c:if test="${product == null}">
        <title>New product</title>
    </c:if>
</head>
<body>
<div align="center">
    <c:if test="${product != null}">
        <h1>Edit product</h1>
    </c:if>
    <c:if test="${product == null}">
        <h1>New product</h1>
    </c:if>
    <h2>
        <a href="/product/create">Add New Product</a>
        &nbsp;&nbsp;&nbsp;
        <a href="/product/view">List All Product</a>
    </h2>

</div>
<div align="center">
    <c:if test="${product != null}">
    <form action="edit" method="post">
        </c:if>
        <c:if test="${product == null}">
        <form action="create" method="post">
            </c:if>
            <table border="1" cellpadding="5">
                <caption>
                    <h2>
                        <c:if test="${product != null}">
                            Edit Product
                        </c:if>
                        <c:if test="${product == null}">
                            Add New Product
                        </c:if>
                    </h2>
                </caption>
                <c:if test="${product!=null}">
                    <input type="hidden" name="id"
                           value="<c:out value='${product.id}'/>"
                    />
                </c:if>
                <tr>
                    <th>Name</th>
                    <td>
                        <input type="text" name="name" size="45"
                               value="<c:out value='${product.name}'/>"
                        />
                    </td>
                </tr>
                <tr>
                    <th>Price:</th>
                    <td>
                        <input type="text" name="price" size="15"
                               value="<c:out value='${product.price}' />"
                        />
                    </td>
                </tr>
                <tr>
                    <th>Category</th>
                    <td>
                        <select name="category" size="1">
                            <c:forEach var="category" items="${categories}">
                                <option><c:out value="${category.name}"/></option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Save"/>
                    </td>
                </tr>
            </table>
        </form>
    </form>
    <a href="/home">Home</a>
</div>
</body>
</html>