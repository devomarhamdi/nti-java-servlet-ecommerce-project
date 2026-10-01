<%@ page contentType="text/html;charset=UTF-8" %>
<%!
    // Escape user-supplied values before echoing them back into the page.
    private static String esc(Object value) {
        if (value == null) {
            return "";
        }
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
<%
    request.setAttribute("pageTitle", "Login - ShopEasy");

    // Set by LoginServlet on a failed attempt.
    String error = (String) request.getAttribute("error");
    String login = (String) request.getAttribute("login");

    // Set by SignupServlet / AuthFilter redirects.
    boolean justRegistered = "1".equals(request.getParameter("registered"));
    boolean authRequired = "required".equals(request.getParameter("auth"));
%>
<%@ include file="header.jsp" %>
<%@ include file="messages.jsp" %>

<section class="auth-card">
    <h1>Login</h1>

    <% if (justRegistered) { %>
    <div class="alert alert-success">Your account was created. Please log in.</div>
    <% } %>
    <% if (authRequired && error == null) { %>
    <div class="alert alert-error">Please log in to continue.</div>
    <% } %>
    <% if (error != null) { %>
    <div class="alert alert-error"><%= esc(error) %></div>
    <% } %>

    <form class="auth-form" action="<%= ctx1 %>/login" method="post">
        <label for="login">Username or email</label>
        <input type="text" id="login" name="login" required autofocus
               autocomplete="username" value="<%= esc(login) %>">

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required
               autocomplete="current-password">

        <button type="submit" class="btn btn-primary">Login</button>
    </form>

    <p class="auth-switch">
        Don't have an account? <a href="<%= ctx1 %>/register">Register</a>
    </p>
</section>

<%@ include file="footer.jsp" %>
