<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Category" %>
<%@ page import="java.util.List" %>
<% request.setAttribute("pageTitle", "Categories - Admin - ShopEasy"); %>
<%@ include file="../header.jsp" %>
<%@ include file="../messages.jsp" %>

<section class="page-heading">
    <h1>Categories</h1>
    <a class="back-link" href="<%= request.getContextPath() %>/admin/dashboard.jsp">&larr; Dashboard</a>
</section>

<form class="admin-form" action="<%= request.getContextPath() %>/admin/categories" method="post">
    <input type="hidden" name="action" value="create">
    <h2>Add Category</h2>
    <label for="name">Name</label>
    <input type="text" id="name" name="name" required maxlength="100">
    <label for="description">Description</label>
    <textarea id="description" name="description" maxlength="255"></textarea>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary">Create</button>
    </div>
</form>

<%
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<% if (categories == null || categories.isEmpty()) { %>
<p class="empty-state">No categories yet.</p>
<% } else { %>
<table class="data-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
    </tr>
    </thead>
    <tbody>
    <% for (Category category : categories) { %>
    <tr>
        <td><%= category.getId() %></td>
        <td><%= category.getName() %></td>
        <td><%= category.getDescription() == null ? "" : category.getDescription() %></td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="../footer.jsp" %>
