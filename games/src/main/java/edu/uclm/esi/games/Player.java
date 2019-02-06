package edu.uclm.esi.games;

import javax.websocket.Session;

import org.bson.BsonDocument;
import org.bson.BsonString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.uclm.esi.mongolabels.dao.MongoBroker;
import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Player {

	@Bsonable
	private String userName;
	@Bsonable
	private String email;
	@Bsonable
	private String pwd;
	@Bsonable
	private String tipo;
	@Bsonable 
	private String id;
	private Session session;
	@JsonIgnore
	private Match currentMatch;
	
	@Override
	public String toString() {
		return "Player [userName=" + userName + ", email=" + email + ", pwd=" + pwd + ", tipo=" + tipo + ", id=" + id
				+ ", session=" + session + ", currentMatch=" + currentMatch + "]";
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session2) {
		this.session = session2;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	private void setPwd(String pwd) {
		this.pwd=pwd;
	}

	public static Player identify(String userName, String pwd, String tipo) throws Exception {
		BsonDocument criterion=new BsonDocument();
		criterion.append("userName", new BsonString(userName));
		criterion.append("pwd", new BsonString(pwd));
		criterion.append("tipo", new BsonString(tipo));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}
	public static Player identifyMail(String email, String tipo) throws Exception {
		BsonDocument criterion=new BsonDocument();
		criterion.append("email", new BsonString(email));
		criterion.append("tipo", new BsonString(tipo));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}
	public static Player identifyGoogle(String id, String userName, String email, String tipo) throws Exception {
		BsonDocument criterion=new BsonDocument();
		criterion.append("id", new BsonString(id)).put("userName", new BsonString(userName));
		criterion.append("email", new BsonString(email));
		criterion.append("tipo", new BsonString(tipo));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}

	public static Player register(String email, String userName, String pwd, String tipo) throws Exception {
		Player player=new Player();
		player.setEmail(email);
		player.setUserName(userName);
		player.setPwd(pwd);
		player.setTipo(tipo);
		MongoBroker.get().insert(player);
		return player;
	}
	
	public static Player registerGoogle(String email, String userName, String id, String tipo) throws Exception {
		Player player=new Player();
		player.setEmail(email);
		player.setUserName(userName);
		player.setId(id);
		player.setTipo(tipo);
		MongoBroker.get().insert(player);
		return player;
	}
	
	public void deletePlayer(String correo, String tipo) {
		BsonDocument criterion=new BsonDocument();
		criterion.append("email", new BsonString(email));
		criterion.append("tipo", new BsonString(tipo));
		MongoBroker.get().delete("Player", criterion);
	}

	public void setCurrentMatch(Match match) {
		this.currentMatch=match;
	}
	
	public Match getCurrentMatch() {
		return currentMatch;
	}

	public Match move(int coordinates) throws Exception {
		return this.currentMatch.move(this, coordinates);
	}

}
