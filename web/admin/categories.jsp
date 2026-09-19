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

<%
    // When ?edit=id is present the same form switches to "update" mode.
    Category editing = (Category) request.getAttribute("editing");
    String ctx = request.getContextPath();
%>
<form class="admin-form" action="<%= ctx %>/admin/categories" method="post">
    <% if (editing != null) { %>
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="<%= editing.getId() %>">
    <h2>Edit Category #<%= editing.getId() %></h2>
    <% } else { %>
    <input type="hidden" name="action" value="create">
    <h2>Add Category</h2>
    <% } %>
    <label for="name">Name</label>
    <input type="text" id="name" name="name" required maxlength="100"
           value="<%= editing != null ? editing.getName() : "" %>">
    <label for="description">Description</label>
    <textarea id="description" name="description" maxlength="255"><%= editing != null && editing.getDescription() != null ? editing.getDescription() : "" %></textarea>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary"><%= editing != null ? "Save" : "Create" %></button>
        <% if (editing != null) { %>
        <a class="btn" href="<%= ctx %>/admin/categories">Cancel</a>
        <% } %>
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
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% for (Category category : categories) { %>
    <tr>
        <td><%= category.getId() %></td>
        <td><%= category.getName() %></td>
        <td><%= category.getDescription() == null ? "" : category.getDescription() %></td>
        <td class="actions">
            <a class="btn btn-small" href="<%= ctx %>/admin/categories?edit=<%= category.getId() %>">Edit</a>
            <form action="<%= ctx %>/admin/categories" method="post"
                  onsubmit="return confirm('Delete category &quot;<%= category.getName() %>&quot;?');">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="<%= category.getId() %>">
                <button type="submit" class="btn btn-small btn-danger">Delete</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="../footer.jsp" %>
