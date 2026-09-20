<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<% request.setAttribute("pageTitle", "Products - ShopEasy"); %>
<%@ include file="header.jsp" %>
<%@ include file="messages.jsp" %>

<section class="page-heading">
    <h1>Our Products</h1>
    <form class="search-form" action="<%= request.getContextPath() %>/products" method="get">
        <input type="text" name="keyword" placeholder="Search products by name..."
               value="<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>">
        <button type="submit" class="btn">Search</button>
    </form>
</section>

<%
    List<Product> products = (List<Product>) request.getAttribute("products");
%>
<% if (products == null || products.isEmpty()) { %>
<p class="empty-state">No products found.</p>
<% } else { %>
<div class="product-grid">
    <% for (Product product : products) { %>
    <div class="product-card">
        <a href="<%= request.getContextPath() %>/product?id=<%= product.getId() %>">
            <img class="product-thumb"
                 src="<%= request.getContextPath() %>/images/products/<%= product.getImage() %>"
                 alt="<%= product.getName() %>"
                 onerror="this.onerror=null;this.src='<%= request.getContextPath() %>/images/placeholder.svg';">
            <h3><%= product.getName() %></h3>
        </a>
        <p class="product-category"><%= product.getCategoryName() %></p>
        <p class="product-description"><%= product.getDescription() %></p>
        <p class="product-price">$<%= product.getPrice() %></p>
        <p class="product-stock <%= product.getStock() == 0 ? "out-of-stock" : "" %>">
            <%= product.getStock() == 0 ? "Out of stock" : product.getStock() + " in stock" %>
        </p>
        <a class="btn" href="<%= request.getContextPath() %>/product?id=<%= product.getId() %>">View Details</a>
    </div>
    <% } %>
</div>
<% } %>

<%@ include file="footer.jsp" %>
