package edu.uclm.esi.games;

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
	private byte[] img;
	@Bsonable
	private String id;
	@Bsonable
	private String tipo;
	@JsonIgnore
	private Match currentMatch;

	public Player() {

	}

	public void modificarPlayer(Player player, String userName, String email, String pwd, Match curreny) {
		BsonDocument criterion = new BsonDocument();
		criterion.append("email", new BsonString(email));
		MongoBroker.get().delete("Player", criterion);
		
		player.setUserName(userName);
		player.setEmail(email);
		player.setPwd(pwd);
		player.setCurrentMatch(currentMatch);
		try {
			player.register(player);
		} catch (Exception e) {
			System.out.println("Error modificar player: " + e.getMessage());
		}
	}

	public Player createPlayerNormal(String userName, String email, String pwd, Match currentMatch) throws Exception {
		Player player = new Player();
		player.setUserName(userName);
		player.setEmail(email);
		player.setPwd(pwd);
		player.setCurrentMatch(currentMatch);
		try {
			player.register(player);
		} catch (Exception e) {
			System.out.println("Error create player normal: " + e.getMessage());
		}
		return player;
	}

	public Player createPlayerGoogle(String id, String userName, String email, Match currentMatch) {
		Player player = new Player();
		player.setId(id);
		player.setUserName(userName);
		player.setEmail(email);
		player.setTipo("Google");
		player.setCurrentMatch(currentMatch);
		try {
			player.register(player);
		} catch (Exception e) {
			System.out.println("Error create player Google: " + e.getMessage());
		}
		return player;
	}

	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
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

	public void setPwd(String pwd) {
		this.pwd = pwd;
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

	public String getPwd() {
		return pwd;
	}

	public Player identify(String userName, String pwd) throws Exception {
		BsonDocument criterion = new BsonDocument();
		criterion.append("userName", new BsonString(userName));
		criterion.append("pwd", new BsonString(pwd));
		Player player = (Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}

	public Player identifyGoogle(String id, String userName, String email) throws Exception {
		BsonDocument criterion = new BsonDocument();
		criterion.append("id", new BsonString(id)).put("userName", new BsonString(userName));
		criterion.append("email", new BsonString(email));
		criterion.append("tipo", new BsonString("Google"));
		Player player = (Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}

	public Player identifyToken(String email) {
		BsonDocument criterion = new BsonDocument();
		criterion.append("email", new BsonString(email));
		return null;
	}

	public void register(Player player) throws Exception {
		MongoBroker.get().insert(player);
	}

	public void setCurrentMatch(Match match) {
		this.currentMatch = match;
	}

	public Match getCurrentMatch() {
		return currentMatch;
	}

	public Match move(int[] coordinates) throws Exception {
		return this.currentMatch.move(this, coordinates);
	}

	/*
	 * public Player solictarToken(String email) { Player player= null; try {
	 * BsonDocument criterion = new BsonDocument(); criterion.append("email", new
	 * BsonString(email)); player=(Player) MongoBroker.get().loadOne(Player.class,
	 * criterion ); player.createToken(email); }catch (Exception e) { // TODO:
	 * handle exception } return null; }
	 * 
	 * private void createToken(String email) throws Exception { Token token = new
	 * Token(userName); MongoBroker.get().insert(token); EMailSenderService correo =
	 * new EMailSenderService(); correo.enviarPorGmail(email, token.getValor()); }
	 */

	public void setFoto(byte[] bytes) {
		this.img = bytes;

	}



}
