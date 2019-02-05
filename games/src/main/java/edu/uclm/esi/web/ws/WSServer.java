package edu.uclm.esi.web.ws;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

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
		// byte[] bytes = message.getPayload().array();
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
			Integer coordinates = jso.getInt("coordinates");
			Manager.get().move(player, coordinates);
		} else if (jso.getString("TYPE").equals("joinGame")) {
			Manager.get().joinGame((Player) session.getAttributes().get("player"), jso.getString("game"));
		} else if (jso.getString("TYPE").equals("sudoku")) {
			if (jso.getString("text").equals("board")) {
				JSONObject obj = new JSONObject();
				obj.put("TYPE", "createBoard");
				String board = SudokuBoard.generateBoard();
				obj.put("text", board);
				WebSocketMessage<?> messageT = new TextMessage(obj.toString());
				session.sendMessage(messageT);
			} else if (jso.getString("text").equals("winner")) {
				JSONObject obj = new JSONObject();
				JSONObject obj2 = new JSONObject();

				String player0 = jso.getString("player0");
				String player1 = jso.getString("player1");
				Player player = (Player) session.getAttributes().get("player");
				if (player.getUserName().equals(player0)) {
					obj.put("TYPE", "winner");
					obj.put("text", "End of the game, you win");
					WebSocketSession sessionI = sessionsByPlayer.get(player0);
					WebSocketMessage<?> messageT = new TextMessage(obj.toString());
					sessionI.sendMessage(messageT);

					obj2.put("TYPE", "looser");
					obj2.put("text", "End of the game, you loose");
					WebSocketSession sessionII = sessionsByPlayer.get(player1);
					WebSocketMessage<?> messageTT = new TextMessage(obj2.toString());
					sessionII.sendMessage(messageTT);
				} else {

					obj.put("TYPE", "winner");
					obj.put("text", "End of the game, you win");
					WebSocketSession sessionI = sessionsByPlayer.get(player1);
					WebSocketMessage<?> messageT = new TextMessage(obj.toString());
					sessionI.sendMessage(messageT);

					obj2.put("TYPE", "looser");
					obj2.put("text", "End of the game, you loose");
					WebSocketSession sessionII = sessionsByPlayer.get(player0);
					WebSocketMessage<?> messageTT = new TextMessage(obj2.toString());
					sessionII.sendMessage(messageTT);
				}

			}

		} else if (jso.getString("TYPE").equals("joinChat")) {
			Player player = (Player) session.getAttributes().get("player");
			JSONObject obj = new JSONObject();
			obj.put("TYPE", "chat");
			obj.put("text", "This is a christian server so please no swearing:" +player.getUserName());
			WebSocketMessage<?> messageI = new TextMessage(obj.toString());
			for (Entry<String, WebSocketSession> entry : sessionsByPlayer.entrySet()) {
				entry.getValue().sendMessage(messageI);
			}	
			
		}else if(jso.getString("TYPE").equals("msg")) {
			Player player = (Player) session.getAttributes().get("player");
			JSONObject obj = new JSONObject();
			obj.put("TYPE", "msg");
			obj.put("text", jso.getString("text"));
			obj.put("nombre", player.getUserName());
			WebSocketMessage<?> messageI = new TextMessage(obj.toString());
			for (Entry<String, WebSocketSession> entry : sessionsByPlayer.entrySet()) {
				entry.getValue().sendMessage(messageI);
			}	
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

	public static void result(Player player0, Player player1, String string, String player0item, String player1item,
			int v0, int v1, int tirada) throws IOException {
		if (string.equals("empate")) {
			JSONObject obj = new JSONObject();
			obj.put("TYPE", "empate");
			obj.put("text", "Hooooo it was a tie");
			obj.put("selected", "Opponent choose: " + player0item);
			obj.put("status", "Result: " + v0 + ", " + v1);
			obj.put("tirada", tirada);
			WebSocketSession sessionI = sessionsByPlayer.get(player0.getUserName());
			WebSocketSession sessionII = sessionsByPlayer.get(player1.getUserName());
			WebSocketMessage<?> messageT = new TextMessage(obj.toString());
			sessionI.sendMessage(messageT);
			sessionII.sendMessage(messageT);
		} else if (string.equals("gana")) {
			JSONObject obj = new JSONObject();
			obj.put("TYPE", "gana");
			obj.put("text", "You win");
			obj.put("selected", "Opponent choose: " + player1item);
			obj.put("status", "Result: " + v0 + ", " + v1);
			obj.put("tirada", tirada);
			WebSocketSession sessionI = sessionsByPlayer.get(player0.getUserName());
			WebSocketMessage<?> messageT = new TextMessage(obj.toString());
			sessionI.sendMessage(messageT);

			JSONObject obj2 = new JSONObject();
			obj2.put("TYPE", "pierde");
			obj2.put("text", "You lose");
			obj2.put("selected", "Opponent choose: " + player0item);
			obj2.put("status", "Result: " + v1 + ", " + v0);
			obj.put("tirada", tirada);
			WebSocketSession sessionII = sessionsByPlayer.get(player1.getUserName());
			WebSocketMessage<?> messageTT = new TextMessage(obj2.toString());
			sessionII.sendMessage(messageTT);
		} else if (string.equals("espera")) {
			JSONObject obj = new JSONObject();
			obj.put("TYPE", "espera");
			obj.put("text", "Waithing 4 opponent choice");
			obj.put("tirada", tirada);
			WebSocketSession sessionI = sessionsByPlayer.get(player0.getUserName());
			WebSocketMessage<?> messageT = new TextMessage(obj.toString());
			sessionI.sendMessage(messageT);
		}

	}

	public static void endGame(Player player, Player player2, int v0, int v1) throws IOException {
		JSONObject obj = new JSONObject();
		obj.put("TYPE", "fin");
		obj.put("text", "End of the game, result: " + v0 + ", " + v1);
		WebSocketSession sessionI = sessionsByPlayer.get(player.getUserName());
		WebSocketMessage<?> messageT = new TextMessage(obj.toString());
		sessionI.sendMessage(messageT);

		JSONObject obj2 = new JSONObject();
		obj2.put("TYPE", "fin");
		obj2.put("text", "End of the game, result: " + v1 + ", " + v0);
		WebSocketSession sessionII = sessionsByPlayer.get(player2.getUserName());
		WebSocketMessage<?> messageTT = new TextMessage(obj2.toString());
		sessionII.sendMessage(messageTT);

	}
}
