<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Product" %>
<%
    Product product = (Product) request.getAttribute("product");
    request.setAttribute("pageTitle", product.getName() + " - ShopEasy");
%>
<%@ include file="header.jsp" %>
<%@ include file="messages.jsp" %>

<section class="product-details">
    <div class="product-details-image">
        <img src="<%= request.getContextPath() %>/images/products/<%= product.getImage() %>"
             alt="<%= product.getName() %>"
             onerror="this.onerror=null;this.src='<%= request.getContextPath() %>/images/placeholder.svg';">
    </div>
    <div class="product-details-info">
        <h1><%= product.getName() %></h1>
        <p class="product-category">Category: <%= product.getCategoryName() %></p>
        <p class="product-description"><%= product.getDescription() %></p>
        <p class="product-price">$<%= product.getPrice() %></p>
        <p class="product-stock <%= product.getStock() == 0 ? "out-of-stock" : "" %>">
            <%= product.getStock() == 0 ? "Out of stock" : product.getStock() + " available" %>
        </p>

        <% if (product.getStock() > 0) { %>
        <form class="add-to-cart-form" action="<%= request.getContextPath() %>/cart" method="post">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="productId" value="<%= product.getId() %>">
            <label for="quantity">Quantity:</label>
            <input type="number" id="quantity" name="quantity" value="1" min="1" max="<%= product.getStock() %>">
            <button type="submit" class="btn btn-primary">Add to Cart</button>
        </form>
        <% } else { %>
        <button class="btn btn-disabled" disabled>Out of Stock</button>
        <% } %>

        <a class="back-link" href="<%= request.getContextPath() %>/products">&larr; Back to Products</a>
    </div>
</section>

<%@ include file="footer.jsp" %>
