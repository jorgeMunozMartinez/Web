<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.games.Player"%>
<%@page import="edu.uclm.esi.games.Manager"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String p=request.getParameter("p");
	JSONParser jsp=new JSONParser();
	JSONObject object=(JSONObject) jsp.parse(p);
	
	String name=object.get("name").toString();
	String pwd=objeto.get("pwd").toString();

	JSONObject result=new JSONObject();		
	try {
		Player player=Manager.get().logIn(email, pwd);
		result.put("type", "OK");
		result.put("email", player.getEmail());
		result.put("img", player.getImg());
		result.put("userName",player.getUserName());

		Cookie cookie=new Cookie("userName", ""+player.getUserName());
		response.addCookie(cookie);		
	}
	catch (Exception e) {
		result.put("type", "ERROR");
		result.put("text", e.toString());
	}
	out.println(result);
%>