<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Cart" %>
<%@ page import="model.CartItem" %>
<% request.setAttribute("pageTitle", "Your Cart - ShopEasy"); %>
<%@ include file="header.jsp" %>
<%@ include file="messages.jsp" %>

<section class="page-heading">
    <h1>Your Cart</h1>
</section>

<%
    String ctx = request.getContextPath();
    Cart cart = (Cart) request.getAttribute("cart");
%>
<% if (cart == null || cart.getItems().isEmpty()) { %>
<p class="empty-state">Your cart is empty.</p>
<a class="btn" href="<%= ctx %>/products">Continue Shopping</a>
<% } else { %>
<table class="cart-table">
    <thead>
    <tr>
        <th>Product</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Subtotal</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <% for (CartItem item : cart.getItems()) { %>
    <tr>
        <td>
            <a href="<%= ctx %>/product?id=<%= item.getProduct().getId() %>">
                <%= item.getProduct().getName() %>
            </a>
        </td>
        <td>$<%= item.getProduct().getPrice() %></td>
        <td>
            <form class="cart-update-form" action="<%= ctx %>/cart" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="productId" value="<%= item.getProduct().getId() %>">
                <input type="number" name="quantity" value="<%= item.getQuantity() %>" min="1"
                       max="<%= item.getProduct().getStock() %>">
                <button type="submit" class="btn btn-small">Update</button>
            </form>
        </td>
        <td>$<%= item.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())) %></td>
        <td>
            <form action="<%= ctx %>/cart" method="post">
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="productId" value="<%= item.getProduct().getId() %>">
                <button type="submit" class="btn btn-small btn-danger">Remove</button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>

<div class="cart-summary">
    <p class="cart-total">Total: $<%= cart.getTotal() %></p>
    <form action="<%= ctx %>/cart" method="post">
        <input type="hidden" name="action" value="clear">
        <button type="submit" class="btn btn-secondary">Clear Cart</button>
    </form>
    <a class="btn" href="<%= ctx %>/checkout">Checkout</a>
</div>
<% } %>

<%@ include file="footer.jsp" %>
