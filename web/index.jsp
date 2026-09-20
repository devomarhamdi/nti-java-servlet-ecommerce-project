<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<% request.setAttribute("pageTitle", "ShopEasy - Home"); %>
<%@ include file="header.jsp" %>
<%@ include file="messages.jsp" %>

<section class="hero">
    <h1>Welcome to ShopEasy</h1>
    <p>Quality products, simple shopping.</p>
    <a class="btn btn-primary" href="<%= request.getContextPath() %>/products">Browse All Products</a>
</section>

<section class="featured-products">
    <h2>Featured Products</h2>
    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
    %>
    <% if (products == null || products.isEmpty()) { %>
    <p class="empty-state">No products available right now.</p>
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
            <p class="product-price">$<%= product.getPrice() %></p>
        </div>
        <% } %>
    </div>
    <% } %>
</section>

<%@ include file="footer.jsp" %>
