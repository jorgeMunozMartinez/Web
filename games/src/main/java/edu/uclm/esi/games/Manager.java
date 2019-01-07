package edu.uclm.esi.games;

public class Manager {

	public Player singUp(String email, String userName, String pwd, String img) throws Exception {
		Player player = new Player();
		player.createPlayer(userName, email, pwd, img, null);
		player.register(player);
		return player;
	}

	public Player logIn(String userName, String pwd) throws Exception {
		Player player = new Player();
		return player.identify(userName, pwd);
	}

}
