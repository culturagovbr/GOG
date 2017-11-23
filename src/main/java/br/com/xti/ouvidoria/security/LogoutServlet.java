package br.com.xti.ouvidoria.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(description = "Servlet responsável por efetuar o logout do usuário", urlPatterns = { "/logout" })
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", new java.util.Date().toString());

		if (request.getSession(false) != null) {
			request.getSession(false).invalidate();
		}
		
		if (request.getSession() != null) {
			request.getSession().invalidate();
		}

		request.logout();
		
		response.sendRedirect(request.getContextPath() + "/");
	}

}
