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

	public Player createPlayer(String id,String userName, String email, String pwd, byte[] img,String tipo, Match currentMatch)
			throws Exception {
		Player player = new Player();
		player.setUserName(userName);
		player.setEmail(email);
		player.setPwd(pwd);
		player.setImg(img);
		player.setCurrentMatch(currentMatch);
		player.setId(id);
		player.setTipo(tipo);
		player.register(player);
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
		criterion.append("pwd", new  BsonString(pwd));
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

	public Player solictarToken(String userName,String email) {
		Player player= null;
		try {
			BsonDocument criterion = new BsonDocument();
			player=(Player) MongoBroker.get().loadOne(Player.class, criterion );
			player.createToken(userName,email);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private void createToken(String userName,String userEm) throws Exception {
		Token token = new Token(userName);
		MongoBroker.get().insert(token);
		EMailSenderService email = new EMailSenderService();
		email.enviarPorGmail(userEm, token.getValor());
	}

	public void setFoto(byte[] bytes) {
		this.img=bytes;
		
	}
}
