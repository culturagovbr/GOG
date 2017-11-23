package br.com.xti.ouvidoria.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.xti.ouvidoria.dao.AvisoDAO;
import br.com.xti.ouvidoria.model.TbAviso;

@WebServlet(description = "Verifica se existe algum aviso cadastrado e ativo para ser apresentado", urlPatterns = { "/aviso" })
public class AvisoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AvisoDAO dao;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = response.getOutputStream();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		TbAviso aviso = null;
		try {
			aviso = dao.getAvisoAtual();
			response.setStatus(HttpServletResponse.SC_OK);
			String json = getJson(aviso);
			out.write(json.getBytes("UTF-8"));
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	private String getJson(TbAviso aviso) {
		try {
			String titulo = aviso.getDsTitulo().replaceAll("\t", " ").replaceAll("\"", "\\\\\"");
			String conteudo = aviso.getDsConteudo().replaceAll("\t", " ").replaceAll("\"", "\\\\\"");
			return String.format("{\"titulo\":\"%s\",\"conteudo\":\"%s\"}", titulo, conteudo);
		} catch (NullPointerException e) {
			return "{}";
		}
	}
	
}
