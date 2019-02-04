package edu.uclm.esi.web;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;
import edu.uclm.esi.games.Token;
import edu.uclm.esi.web.ws.WSServer;

@RestController
public class UserControllerPost {

	@RequestMapping(value = "/sendToken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Token sendToken(HttpSession session, String email) {
		Token token = null;
		try {
			try {
				Player player = Player.identifyMail(email, "Normal");
			} catch (Exception e) {
				throw new Exception("Error: email");
			}
			token = new Token();
			token.createToken(email);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return token;
	}

	@RequestMapping(value = "/refactor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void refactor(HttpSession session, String token, String psw1, String psw2, String email) {
		try {
			Token tokenO = new Token();
			tokenO = tokenO.tokenEmail(email);
			Player player = Player.identifyMail(email, "Normal");
			if (tokenO.getValor().equals(token)) {
				if (psw1.equals(psw2)) {
					tokenO.borrarToken(email, token);
					player.deletePlayer(email, "Normal");
					player.register(email, player.getUserName(), psw1, "Normal");
				} else {
					throw new Exception("Error: password not equals");
				}
			} else {
				throw new Exception("Error: token not equals");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPost(HttpSession session, String userName, String pwd, String tipo) throws Exception {
		Player player = Player.identify(userName, pwd, tipo);
		session.setAttribute("player", player);
		return player;
	}

	@RequestMapping(value = "/loginGoogle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPostGoogle(HttpSession session, String id, String userName, String email, String tipo) {
		Player player = null;
		try {
			player = Player.identifyGoogle(id, userName, email, tipo);
		} catch (Exception e) {
			System.out.println(e.toString());
			try {
				player = Player.registerGoogle(email, userName, id, tipo);
			} catch (Exception e1) {
				System.out.println(e.toString());
			}
		}
		session.setAttribute("player", player);
		return player;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player register(String email, String userName, String pwd1, String pwd2, String tipo) throws Exception {
		Player player = null;
		if (!pwd1.equals(pwd2))
			throw new Exception("Error: contraseñas distintas");
		try {
			player = Player.identifyMail(email, "Normal");
		} catch (Exception e) {
			player = Player.register(email, userName, pwd1, tipo);
		}
		return player;
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Match joinppt(HttpSession session, String gameName) throws Exception {
		Player player = (Player) session.getAttribute("player");
		if (player == null)
			throw new Exception("You need to be logged");
		Match match = Manager.get().joinGame(player, gameName.substring(0, gameName.length() - 1));
		WSServer.send(match.getPlayers(), match);
		return match;
	}

	@RequestMapping(value = { "/joinGame",
			"/post/joinGame" }, method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Match joinGamePost(HttpSession session, @RequestBody String gameName) throws Exception {
		Player player = (Player) session.getAttribute("player");
		if (player == null)
			throw new Exception("You need to be logged");
		Match match = Manager.get().joinGame(player, gameName.substring(0, gameName.length() - 1));
		WSServer.send(match.getPlayers(), match);
		return match;
	}
}
