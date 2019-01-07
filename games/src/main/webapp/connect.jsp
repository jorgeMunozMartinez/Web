<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.games.Manager"%>
<%@page import="edu.uclm.esi.games.Player"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	Cookie[] cookies=request.getCookies();
	Cookie UserID=null;
	if (cookies!=null) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("UserId")) {
				UserId=cookie;
				break;
			}
		}
	}
	
	JSONObject result=new JSONObject();
	if (UserID!=null) {
		Integer userId=Integer.parseInt(UserID.getValue());
		try {
			Player player=Manager.get().connect(idUsuario);
			result.put("type", "OK");
			result.put("userName", plaayer.getUserName());
			result.put("id", player.getId());
			result.put("email", player.getEmail());	
			result.put("pathFoto", player.getPathFoto());
			result.putValue("player", player);
		}
		catch (Exception e) {
			result.put("type", "ERROR");
			System.out.println(e.getMessage());
			result.put("text", e.getMessage());
		}
	} else {
		result.put("type", "NO_EXISTE");
	}
	out.println(result);
%>