<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.games.Player"%>
<%@page import="edu.uclm.esi.games.Manager"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String p=request.getParameter("p");
	JSONParser jsp=new JSONParser();
	JSONObject objeto=(JSONObject) jsp.parse(p);
	
	String email=objeto.get("email").toString();
	String pwd=objeto.get("pwd").toString();

	JSONObject resultado=new JSONObject();		
	try {
		Usuario usuario=Manager.get().identificar(email, pwd);
		resultado.put("tipo", "OK");
		resultado.put("email", email);
		resultado.put("id", usuario.getId());
		resultado.put("pathFoto", usuario.getPathFoto());
		session.putValue("usuario", usuario);
		Cookie cookie=new Cookie("idUsuario", ""+usuario.getId());
		cookie.setMaxAge(365*24*3600);
		response.addCookie(cookie);		
	}
	catch (Exception e) {
		resultado.put("tipo", "ERROR");
		resultado.put("texto", e.getMessage());
	}
	out.println(resultado);
%>