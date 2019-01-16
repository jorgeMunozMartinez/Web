<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.games.Player"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String data=request.getParameter("p");
	JSONParser jsp=new JSONParser();
	JSONObject object=(JSONObject) jsp.parse(data);
	
	String name=object.get("name").toString();
	String pwd=object.get("psw").toString();

	JSONObject result=new JSONObject();		
	try {
		Player player= new Player();
		Player playerI = player.identify(name, pwd);
		result.put("type", "OK");
		result.put("email", playerI.getEmail());
		result.put("img", playerI.getImg());
		result.put("userName",playerI.getUserName());	
	}
	catch (Exception e) {
		result.put("type", "ERROR");
		result.put("text", e.toString());
	}
	out.println(result);
%>