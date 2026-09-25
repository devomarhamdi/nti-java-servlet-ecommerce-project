<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Order" %>
<%@ page import="model.OrderItem" %>
<%
    Order order = (Order) request.getAttribute("order");
    request.setAttribute("pageTitle", "Order #" + order.getId() + " - ShopEasy");
%>
<%@ include file="../header.jsp" %>
<%@ include file="../messages.jsp" %>

<section class="page-heading">
    <h1>Order #<%= order.getId() %></h1>
    <p>Placed on <%= order.getOrderDate() %> &mdash; Status: <%= order.getStatus() %></p>
</section>

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
    <% for (OrderItem item : order.getItems()) { %>
    <tr>
        <td><%= item.getProductName() %></td>
        <td>$<%= item.getPrice() %></td>
        <td><%= item.getQuantity() %></td>
        <td>$<%= item.getSubtotal() %></td>
    </tr>
    <% } %>
    </tbody>
</table>

<div class="cart-summary">
    <p class="cart-total">Total: $<%= order.getTotalAmount() %></p>
    <a class="btn" href="<%= request.getContextPath() %>/orders">&larr; Back to My Orders</a>
</div>

<%@ include file="../footer.jsp" %>
