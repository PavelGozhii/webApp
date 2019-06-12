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
    <c:if test="${category != null}">
        <title>Edit category</title>
    </c:if>
    <c:if test="${category == null}">
        <title>New category</title>
    </c:if>
</head>
<body>
<div align="center">
    <c:if test="${categoty != null}">
        <h1>Edit category</h1>
    </c:if>
    <c:if test="${category == null}">
        <h1>New category</h1>
    </c:if>
    <h2>
        <a href="/category/create">Add New Category</a>
        &nbsp;&nbsp;&nbsp;
        <a href="/category/view">List All Category</a>
    </h2>

</div>
<div align="center">
    <c:if test="${category != null}">
    <form action="edit" method="post">
        </c:if>
        <c:if test="${category == null}">
        <form action="create" method="post">
            </c:if>
            <table border="1" cellpadding="5">
                <caption>
                    <h2>
                        <c:if test="${category != null}">
                            Edit Product
                        </c:if>
                        <c:if test="${category == null}">
                            Add New Product
                        </c:if>
                    </h2>
                </caption>
                <c:if test="${category!=null}">
                    <input type="hidden" name="id"
                           value="<c:out value='${category.id}'/>"
                    />
                </c:if>
                <tr>
                    <th>Name</th>
                    <td>
                        <input type="text" name="name" size="45"
                               value="<c:out value='${category.name}'/>"
                        />
                    </td>
                </tr>
                <tr>
                    <th>Price:</th>
                    <td>
                        <input type="text" name="description" size="45"
                               value="<c:out value='${category.description}'/>"
                        />
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