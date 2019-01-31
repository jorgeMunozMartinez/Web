package edu.uclm.esi.web.ws;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uclm.esi.games.Player;

@ServerEndpoint(value = "/servidorDeChat", configurator = HttpSessionConfigurator.class)
public class WSPartidas {
	private static ConcurrentHashMap<String, Session> sessionName = new ConcurrentHashMap<>();

	@OnOpen
	public void open(Session session, EndpointConfig config) {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		Player player = (Player) httpSession.getAttribute("player");
		player.setSession(session);
		sessionName.put(player.getUserName(), session);
		broadcast("This is a christian server so please no swearing: " + player.getUserName());
	}

	@OnClose


	@OnMessage
	private void broadcast(String string) {
		Enumeration<Session> sesiones = sessionName.elements();
		while (sesiones.hasMoreElements()) {
			Session sesion=sesiones.nextElement();
			try {
				JSONObject jso=new JSONObject();
				jso.put("type", "DIFUSION");
				jso.put("message", string);
				sesion.getBasicRemote().sendText(jso.toString());
			} catch (IOException e) {
				sessionName.remove(sesion.getId());
			}
		}
	}
}
