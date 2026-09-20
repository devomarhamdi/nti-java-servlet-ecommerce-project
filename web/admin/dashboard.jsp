<%@ page contentType="text/html;charset=UTF-8" %>
<% request.setAttribute("pageTitle", "Admin Dashboard - ShopEasy"); %>
<%@ include file="../header.jsp" %>
<%@ include file="../messages.jsp" %>

<section class="admin-dashboard">
    <h1>Admin Dashboard</h1>
    <div class="admin-menu">
        <a class="admin-tile" href="<%= request.getContextPath() %>/admin/products">
            <h2>Products</h2>
            <p>Create, update, delete and search products.</p>
        </a>
        <a class="admin-tile" href="<%= request.getContextPath() %>/admin/categories">
            <h2>Categories</h2>
            <p>Manage product categories.</p>
        </a>
        <a class="admin-tile" href="<%= request.getContextPath() %>/admin/orders">
            <h2>Orders</h2>
            <p>View and update customer orders.</p>
        </a>
        <a class="admin-tile" href="<%= request.getContextPath() %>/admin/customers">
            <h2>Customers</h2>
            <p>View registered customers.</p>
        </a>
        <a class="admin-tile" href="<%= request.getContextPath() %>/logout">
            <h2>Logout</h2>
            <p>End your admin session.</p>
        </a>
    </div>
</section>

<%@ include file="../footer.jsp" %>
