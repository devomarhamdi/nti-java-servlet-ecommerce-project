<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<% request.setAttribute("pageTitle", "Products - Admin - ShopEasy"); %>
<%@ include file="../header.jsp" %>
<%@ include file="../messages.jsp" %>

<section class="page-heading">
    <h1>Products</h1>
    <form class="search-form" action="<%= ctx %>/admin/products" method="get">
        <input type="text" name="keyword" placeholder="Search products by name..."
               value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>">
        <button type="submit" class="btn">Search</button>
    </form>
    <a class="btn btn-primary" href="<%= ctx %>/admin/products?action=new">+ New Product</a>
    <a class="back-link" href="<%= ctx %>/admin/dashboard.jsp">&larr; Dashboard</a>
</section>

<%
    List<Product> products = (List<Product>) request.getAttribute("products");
%>
<% if (products == null || products.isEmpty()) { %>
<p class="empty-state">No products found.</p>
<% } else { %>
<table class="data-table">
    <thead>
    <tr>
        <th>ID</th>
        <th>Image</th>
        <th>Name</th>
        <th>Category</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% for (Product product : products) { %>
    <tr>
        <td><%= product.getId() %></td>
        <td>
            <img class="admin-thumb"
                 src="<%= ctx %>/images/products/<%= product.getImage() %>"
                 alt="<%= product.getName() %>"
                 onerror="this.onerror=null;this.src='<%= ctx %>/images/placeholder.svg';">
        </td>
        <td><%= product.getName() %></td>
        <td><%= product.getCategoryName() %></td>
        <td>$<%= product.getPrice() %></td>
        <td class="<%= product.getStock() == 0 ? "out-of-stock" : "" %>"><%= product.getStock() %></td>
        <td class="actions">
            <a class="btn btn-small" href="<%= ctx %>/admin/products?action=edit&id=<%= product.getId() %>">Edit</a>
            <form action="<%= ctx %>/admin/products" method="post"
                  onsubmit="return confirm('Delete product &quot;<%= product.getName() %>&quot;?');">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="<%= product.getId() %>">
                <button type="submit" class="btn btn-small btn-danger">Delete</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="../footer.jsp" %>
