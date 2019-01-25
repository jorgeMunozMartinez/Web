package edu.uclm.esi.games;

public class Manager {

	private static Manager me;

	public static Manager get() {
		if (me == null)
			me = new Manager();
		return me;
	}

	public Player singUp(String id,String email, String userName, String pwd, byte[] img, String tipo) throws Exception {
		Player player = new Player();
		player.createPlayer(id,userName, email, pwd, img,tipo, null);
		player.register(player);
		return player;
	}

	public Player logIn(String userName, String pwd) throws Exception {
		Player player = new Player();
		return player.identify(userName, pwd);
	}
}
