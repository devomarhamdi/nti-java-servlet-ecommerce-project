<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Product" %>
<%@ page import="model.Category" %>
<%@ page import="java.util.List" %>
<%
    // product == null -> create mode; product with id > 0 -> edit mode;
    // product with id == 0 -> create form re-shown after a validation error.
    Product product = (Product) request.getAttribute("product");
    boolean editing = product != null && product.getId() > 0;
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    request.setAttribute("pageTitle", (editing ? "Edit Product" : "New Product") + " - Admin - ShopEasy");
%>
<%@ include file="../header.jsp" %>
<%@ include file="../messages.jsp" %>

<section class="page-heading">
    <h1><%= editing ? "Edit Product #" + product.getId() : "New Product" %></h1>
    <a class="back-link" href="<%= ctx %>/admin/products">&larr; Products</a>
</section>

<form class="admin-form" action="<%= ctx %>/admin/products" method="post">
    <input type="hidden" name="action" value="<%= editing ? "update" : "create" %>">
    <% if (editing) { %>
    <input type="hidden" name="id" value="<%= product.getId() %>">
    <% } %>

    <label for="name">Name</label>
    <input type="text" id="name" name="name" required maxlength="100"
           value="<%= product != null && product.getName() != null ? product.getName() : "" %>">

    <label for="description">Description</label>
    <textarea id="description" name="description"><%= product != null && product.getDescription() != null ? product.getDescription() : "" %></textarea>

    <label for="price">Price</label>
    <input type="number" id="price" name="price" required min="0" step="0.01"
           value="<%= product != null && product.getPrice() != null ? product.getPrice() : "" %>">

    <label for="stock">Stock</label>
    <input type="number" id="stock" name="stock" required min="0" step="1"
           value="<%= product != null ? product.getStock() : 0 %>">

    <label for="categoryId">Category</label>
    <select id="categoryId" name="categoryId" required>
        <option value="">-- choose --</option>
        <% for (Category category : categories) { %>
        <option value="<%= category.getId() %>"
                <%= product != null && product.getCategoryId() == category.getId() ? "selected" : "" %>>
            <%= category.getName() %>
        </option>
        <% } %>
    </select>

    <div class="form-actions">
        <button type="submit" class="btn btn-primary"><%= editing ? "Save" : "Create" %></button>
        <a class="btn" href="<%= ctx %>/admin/products">Cancel</a>
    </div>
</form>

<%@ include file="../footer.jsp" %>
