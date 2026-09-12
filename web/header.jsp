<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // Pages set request.setAttribute("pageTitle", "...") before including
    // this file; falls back to a sensible default otherwise.
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null) {
        pageTitle = "ShopEasy";
    }
    String ctx = request.getContextPath();
    Object loggedInUser = session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %></title>
    <link rel="stylesheet" href="<%= ctx %>/style.css">
</head>
<body>
<header class="site-header">
    <div class="container header-inner">
        <a class="brand" href="<%= ctx %>/">ShopEasy</a>
        <nav class="main-nav">
            <a href="<%= ctx %>/">Home</a>
            <a href="<%= ctx %>/products">Products</a>
            <a href="<%= ctx %>/cart">Cart</a>
            <% if (loggedInUser != null) { %>
                <a href="<%= ctx %>/orders">My Orders</a>
                <a href="<%= ctx %>/logout">Logout</a>
            <% } else { %>
                <a href="<%= ctx %>/login.jsp">Login</a>
                <a href="<%= ctx %>/register.jsp">Register</a>
            <% } %>
        </nav>
    </div>
</header>
<main class="container page-content">
