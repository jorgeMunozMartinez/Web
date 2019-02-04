package edu.uclm.esi.web.ws;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;
import edu.uclm.esi.games.sudoku.SudokuBoard;
import edu.uclm.esi.mongolabels.dao.MongoBroker;
import edu.uclm.esi.web.Manager;

@Component
public class WSServer extends TextWebSocketHandler {
	private static ConcurrentHashMap<String, WebSocketSession> sessionsById = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, WebSocketSession> sessionsByPlayer = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessionsById.put(session.getId(), session);
		Player player = (Player) session.getAttributes().get("player");
		String userName = player.getUserName();
		if (sessionsByPlayer.get(userName) != null) {
			sessionsByPlayer.remove(userName);
		}
		sessionsByPlayer.put(userName, session);
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		Player player = (Player) session.getAttributes().get("player");
		byte[] bytes = message.getPayload().array();
		// player.setFoto(bytes);
		BsonDocument criterion = new BsonDocument();
		criterion.append("userName", new BsonString(player.getUserName()));
		MongoBroker.get().delete("Player", criterion);
		try {
			MongoBroker.get().insert(player);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload());
		if (jso.getString("TYPE").equals("MOVEMENT")) {
			Player player = (Player) session.getAttributes().get("player");
			JSONArray coordinates = jso.getJSONArray("coordinates");
			Match match = Manager.get().move(player, coordinates);
			sendPpt(match.getPlayers(), match);
		} else if (jso.getString("TYPE").equals("joinGame")) {
			Manager.get().joinGame((Player) session.getAttributes().get("player"), jso.getString("game"));
		} else if (jso.getString("TYPE").equals("sudoku")) {
			if (jso.getString("text").equals("board")) {
				JSONObject obj = new JSONObject();
				obj.put("TYPE", "createBoard");
				String board = SudokuBoard.generateBoard();
				System.out.println(board);
				obj.put("text", board);
				WebSocketMessage<?> messageT = new TextMessage(obj.toString());
				session.sendMessage(messageT);
			} else if (jso.getString("text").equals("winner")) {
				JSONObject obj = new JSONObject();
				obj.put("TYPE", "winner");
				obj.put("text", "End of the game");
				String player0 = jso.getString("player0");
				String player1 = jso.getString("player1");
				WebSocketSession sessionI = sessionsByPlayer.get(player0);
				WebSocketSession sessionII = sessionsByPlayer.get(player1);
				WebSocketMessage<?> messageT = new TextMessage(obj.toString());
				sessionI.sendMessage(messageT);
				sessionII.sendMessage(messageT);

			}

		} else if (jso.getString("TYPE").equals("golbalChat")) {

		}
	}

	public static void sendPpt(Vector<Player> players, Match match) {
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jso;
		try {
			jso = new JSONObject(mapper.writeValueAsString(match));
			jso.put("TYPE", "match");
			jso.put("text", "W");
			for (Player player : players) {
				WebSocketSession session = sessionsByPlayer.get(player.getUserName());
				WebSocketMessage<?> message = new TextMessage(jso.toString());
				session.sendMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void send(Vector<Player> players, Match match) {
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jso;
		try {
			jso = new JSONObject(mapper.writeValueAsString(match));
			jso.put("TYPE", "match");
			jso.put("text", players.get(0).getUserName() + " VS " + players.get(1).getUserName());
			jso.put("player0", match.getPlayers().get(0).getUserName());
			jso.put("player1", match.getPlayers().get(1).getUserName());
			for (Player player : players) {
				WebSocketSession session = sessionsByPlayer.get(player.getUserName());
				WebSocketMessage<?> message = new TextMessage(jso.toString());
				session.sendMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void waitOponent(Player player) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("TYPE", "search");
			obj.put("text", "looking for a worthy opponent");
			WebSocketSession session = sessionsByPlayer.get(player.getUserName());
			WebSocketMessage<?> message = new TextMessage(obj.toString());
			session.sendMessage(message);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
