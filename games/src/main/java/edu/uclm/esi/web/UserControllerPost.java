package edu.uclm.esi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;
import edu.uclm.esi.web.ws.WSServer;

@RestController
public class UserControllerPost {

	@RequestMapping(value = "/solicitarCambioPSW", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player solicitarToken(HttpSession session, String userName, String email) throws Exception {
		Player player = new Player();
		return player.solictarToken(userName, email);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player register(String email, String userName, String pwd1, String pwd2, String img) throws Exception {
		if (!pwd1.equals(pwd2))
			throw new Exception("ERROR: psw1 != psw2");
		Player player = new Player();
		player.createPlayer(null, userName, email, pwd1, "Normal", null, null);
		player.register(player);
		return player;
	}

	@RequestMapping(value = "/loginGoogle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPost(HttpSession session, String userName, String email) throws Exception {
		Player player = new Player();
		Player playerI = player.identifyGoogle(userName, email);
		session.setAttribute("player", playerI);
		return playerI;
	}

	@RequestMapping(value = "/logIn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPostHTML(HttpSession session, String userName, String pwd) throws Exception {
		Player player = new Player();
		Player playerI = player.identify(userName, pwd);
		session.setAttribute("player", playerI);
		return playerI;
	}

	@RequestMapping(value = "/loginII", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPostHTMLGOOGLE(HttpSession session, String id, String nombre, String email) throws Exception {
		Player player = new Player();
		player.identifyGoogle(id, nombre, email);
		if (player == null) {
			player.createPlayer(id, nombre, email, null, "Google", null, null);
			player.register(player);
		}
		session.setAttribute("player", player);
		return player;
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

	@ExceptionHandler(Exception.class)
	public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		ModelAndView result = new ModelAndView();
		result.setViewName("respuesta");
		result.addObject("exception", ex);
		result.setStatus(HttpStatus.UNAUTHORIZED);

		return result;
	}
}
