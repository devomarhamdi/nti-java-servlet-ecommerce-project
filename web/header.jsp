<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // Pages set request.setAttribute("pageTitle", "...") before including
    // this file; falls back to a sensible default otherwise.
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null) {
        pageTitle = "ShopEasy";
    }
    String ctx1 = request.getContextPath();
    Object loggedInUser = session.getAttribute("user");

    // Escaped inline: login.jsp/register.jsp declare their own esc() helper,
    // so declaring one here would clash when this file is included.
    String greetingName = "";
    if (loggedInUser instanceof model.User) {
        String username = ((model.User) loggedInUser).getUsername();
        if (username != null) {
            greetingName = username.replace("&", "&amp;").replace("<", "&lt;")
                    .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %></title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap">
    <link rel="stylesheet" href="<%= ctx1 %>/style.css">
</head>
<body>
<header class="site-header">
    <div class="container header-inner">
        <a class="brand" href="<%= ctx1 %>/">ShopEasy</a>
        <nav class="main-nav">
            <a href="<%= ctx1 %>/">Home</a>
            <a href="<%= ctx1 %>/products">Products</a>
            <a href="<%= ctx1 %>/cart">Cart</a>
            <% if (loggedInUser != null) { %>
                <span class="nav-greeting">Hello, <strong><%= greetingName %></strong></span>
                <a href="<%= ctx1 %>/orders">My Orders</a>
                <a href="<%= ctx1 %>/logout">Logout</a>
            <% } else { %>
                <a href="<%= ctx1 %>/login">Login</a>
                <a href="<%= ctx1 %>/register">Register</a>
            <% } %>
        </nav>
    </div>
</header>
<main class="container page-content">
