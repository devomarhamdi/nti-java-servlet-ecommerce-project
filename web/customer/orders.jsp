<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Order" %>
<%@ page import="java.util.List" %>
<% request.setAttribute("pageTitle", "My Orders - ShopEasy"); %>
<%@ include file="../header.jsp" %>
<%@ include file="../messages.jsp" %>

<section class="page-heading">
    <h1>My Orders</h1>
</section>

<%
    String ctx = request.getContextPath();
    List<Order> orders = (List<Order>) request.getAttribute("orders");
%>
<% if (orders == null || orders.isEmpty()) { %>
<p class="empty-state">You haven't placed any orders yet.</p>
<a class="btn" href="<%= ctx %>/products">Start Shopping</a>
<% } else { %>
<table class="cart-table">
    <thead>
    <tr>
        <th>Order #</th>
        <th>Date</th>
        <th>Total</th>
        <th>Status</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <% for (Order order : orders) { %>
    <tr>
        <td>#<%= order.getId() %></td>
        <td><%= order.getOrderDate() %></td>
        <td>$<%= order.getTotalAmount() %></td>
        <td><%= order.getStatus() %></td>
        <td><a class="btn btn-small" href="<%= ctx %>/order?id=<%= order.getId() %>">View Details</a></td>
    </tr>
    <% } %>
    </tbody>
</table>
<% } %>

<%@ include file="../footer.jsp" %>
