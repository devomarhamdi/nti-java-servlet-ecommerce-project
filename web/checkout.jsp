<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Cart" %>
<%@ page import="model.CartItem" %>
<% request.setAttribute("pageTitle", "Checkout - ShopEasy"); %>
<%@ include file="header.jsp" %>
<%@ include file="messages.jsp" %>

<section class="page-heading">
    <h1>Checkout</h1>
</section>

<%
    String ctx = request.getContextPath();
    Cart cart = (Cart) request.getAttribute("cart");
%>
<table class="cart-table">
    <thead>
    <tr>
        <th>Product</th>
        <th>Price</th>
        <th>Quantity</th>
        <th>Subtotal</th>
    </tr>
    </thead>
    <tbody>
    <% for (CartItem item : cart.getItems()) { %>
    <tr>
        <td><%= item.getProduct().getName() %></td>
        <td>$<%= item.getProduct().getPrice() %></td>
        <td><%= item.getQuantity() %></td>
        <td>$<%= item.getProduct().getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())) %></td>
    </tr>
    <% } %>
    </tbody>
</table>

<div class="cart-summary">
    <p class="cart-total">Total: $<%= cart.getTotal() %></p>
    <form action="<%= ctx %>/checkout" method="post">
        <button type="submit" class="btn btn-primary">Place Order</button>
    </form>
    <a class="btn btn-secondary" href="<%= ctx %>/cart">Back to Cart</a>
</div>

<%@ include file="footer.jsp" %>
