<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // Shared, user-friendly feedback banner (NFR-21). Any servlet can use
    // it two ways:
    //   - request.setAttribute("errorMessage"/"successMessage", "...")
    //     before a forward (message shows on that same response).
    //   - session.setAttribute("flashError"/"flashSuccess", "...") before
    //     a redirect (message shows once on the next request, then clears).
    String errorMessage = (String) request.getAttribute("errorMessage");
    if (errorMessage == null) {
        errorMessage = (String) session.getAttribute("flashError");
        if (errorMessage != null) {
            session.removeAttribute("flashError");
        }
    }

    String successMessage = (String) request.getAttribute("successMessage");
    if (successMessage == null) {
        successMessage = (String) session.getAttribute("flashSuccess");
        if (successMessage != null) {
            session.removeAttribute("flashSuccess");
        }
    }
%>
<% if (errorMessage != null) { %>
<div class="alert alert-error"><%= errorMessage %></div>
<% } %>
<% if (successMessage != null) { %>
<div class="alert alert-success"><%= successMessage %></div>
<% } %>
