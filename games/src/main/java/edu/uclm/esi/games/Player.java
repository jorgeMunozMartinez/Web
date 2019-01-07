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
	private String img;
	@JsonIgnore
	private Match currentMatch;
	
	public Player() {
		
	}
	
	public void createPlayer(String userName, String email, String pwd, String img, Match currentMatch) {
		this.userName = userName;
		this.email = email;
		this.pwd = pwd;
		this.img = img;
		this.currentMatch = currentMatch;		
	}

	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img=img;
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
		this.pwd=pwd;
	}

	public Player identify(String userName, String pwd) throws Exception {
		BsonDocument criterion=new BsonDocument();
		criterion.append("userName", new BsonString(userName)).put("pwd", new BsonString(pwd));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}

	public void register(Player player) throws Exception {
		MongoBroker.get().insert(player);
	}

	public void setCurrentMatch(Match match) {
		this.currentMatch=match;
	}
	
	public Match getCurrentMatch() {
		return currentMatch;
	}

	public Match move(int[] coordinates) throws Exception {
		return this.currentMatch.move(this, coordinates);
	}
}
