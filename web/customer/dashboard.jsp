<%@ page contentType="text/html;charset=UTF-8" %>
<% request.setAttribute("pageTitle", "My Account - ShopEasy"); %>
<%@ include file="../header.jsp" %>
<%@ include file="../messages.jsp" %>

<section class="admin-dashboard">
    <h1>My Account</h1>
    <div class="admin-menu">
        <a class="admin-tile" href="<%= request.getContextPath() %>/products">
            <h2>Shop</h2>
            <p>Continue browsing products.</p>
        </a>
        <a class="admin-tile" href="<%= request.getContextPath() %>/cart">
            <h2>Cart</h2>
            <p>Review the items in your cart.</p>
        </a>
        <a class="admin-tile" href="<%= request.getContextPath() %>/orders">
            <h2>My Orders</h2>
            <p>View your past orders.</p>
        </a>
        <a class="admin-tile" href="<%= request.getContextPath() %>/logout">
            <h2>Logout</h2>
            <p>End your session.</p>
        </a>
    </div>
</section>

<%@ include file="../footer.jsp" %>
