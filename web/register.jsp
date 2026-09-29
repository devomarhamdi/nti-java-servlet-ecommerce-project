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
    request.setAttribute("pageTitle", "Register - ShopEasy");

    // Set by SignupServlet when validation or registration fails, so the
    // user doesn't have to retype everything (passwords are never echoed).
    String error = (String) request.getAttribute("error");
%>
<%@ include file="header.jsp" %>
<%@ include file="messages.jsp" %>

<section class="auth-card">
    <h1>Create an account</h1>

    <% if (error != null) { %>
    <div class="alert alert-error"><%= esc(error) %></div>
    <% } %>

    <form class="auth-form" action="<%= ctx1 %>/register" method="post">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required
               minlength="3" maxlength="50" autocomplete="username"
               value="<%= esc(request.getAttribute("username")) %>">

        <label for="email">Email</label>
        <input type="email" id="email" name="email" required autocomplete="email"
               value="<%= esc(request.getAttribute("email")) %>">

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required
               minlength="6" autocomplete="new-password">

        <label for="confirmPassword">Confirm password</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required
               minlength="6" autocomplete="new-password">

        <label for="name">Full name</label>
        <input type="text" id="name" name="name" required autocomplete="name"
               value="<%= esc(request.getAttribute("name")) %>">

        <label for="phone">Phone</label>
        <input type="tel" id="phone" name="phone" required maxlength="20" autocomplete="tel"
               value="<%= esc(request.getAttribute("phone")) %>">

        <label for="address">Address</label>
        <textarea id="address" name="address" required maxlength="255"
                  autocomplete="street-address"><%= esc(request.getAttribute("address")) %></textarea>

        <button type="submit" class="btn btn-primary">Register</button>
    </form>

    <p class="auth-switch">
        Already have an account? <a href="<%= ctx1 %>/login">Login</a>
    </p>
</section>

<%@ include file="footer.jsp" %>
