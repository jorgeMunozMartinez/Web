<%@ page language="java" contentType="application/json"
	pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.games.Player"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String data = request.getParameter("p");
	JSONParser jsp = new JSONParser();
	JSONObject object = (JSONObject) jsp.parse(data);

	String name = object.get("userName").toString();
	String psw1 = object.get("pwd1").toString();
	String psw2 = object.get("pwd2").toString();
	String email = object.get("email").toString();
	String img = object.get("img").toString();

	JSONObject result = new JSONObject();
	if (psw1.equals(psw2)) {
		try {
			Player player = new Player();
			Player playerI = player.createPlayer(name, email, psw1, img, null);
			result.put("type", "OK");
			result.put("email", playerI.getEmail());
			result.put("img", playerI.getImg());
			result.put("userName", playerI.getUserName());
		} catch (Exception e) {
			result.put("type", "ERROR");
			result.put("text", e.toString());
		}
	} else {
		result.put("type", "PSW ERROR");
	}
	out.println(result);
%>