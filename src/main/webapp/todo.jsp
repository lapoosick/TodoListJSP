<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TodoList JSP</title>
</head>
<body>
<h1>Список дел</h1>

<form action="${pageContext.request.contextPath}/" method="POST">
    <input type="text" name="text" size=40>
    <button type="submit" name="action" value="create">Добавить</button>
    <c:if test="${not empty emptyCreateItem}">
        <div>${emptyCreateItem}</div>
    </c:if>
</form>

<ul>
    <c:forEach var="item" items="${todoItems}">
        <li>
            <form action="${pageContext.request.contextPath}/" method="POST">
                <input type="text" name="text" value="${item.text}" size=40>
                <button type="submit" name="action" value="update">Сохранить</button>
                <button type="submit" name="action" value="delete">Удалить</button>
                <c:if test="${not empty emptyUpdateItemId}">
                    <c:if test="${emptyUpdateItemId == item.id}">
                        <div>${emptyUpdateItem}</div>
                    </c:if>
                </c:if>
                <input type="hidden" name="id" value="${item.id}">
            </form>
        </li>
    </c:forEach>
</ul>
</body>
</html>
