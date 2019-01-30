package edu.uclm.esi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;
import edu.uclm.esi.web.ws.WSServer;
import edu.uclm.esi.games.Token;

@RestController
public class UserControllerPost {

	@RequestMapping(value = "/solicitarCambioPSW", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Token solicitarToken(String email) throws Exception {
		Token token = new Token();
		Token tokenI = token.createToken(email);
		token.sendToken(tokenI);
		System.out.println(tokenI.toString());
		return tokenI;
	}

	@RequestMapping(value = "/cambiarCredenciales", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player buscarToken(String email, String tokenValor, String token, String psw1, String psw2)
			throws Exception {
		if (!psw1.equals(psw2))
			throw new Exception("ERROR: psw1 != p sw2");
		else if (!tokenValor.equals(token))
			throw new Exception("ERROR: token no iguales");
		Token tokenObj = new Token();
		tokenObj.borrarToken(email, tokenValor);
		Player player = new Player();
		Player playerI = player.identifyToken(email);
		System.out.println("player identif");
		player.borrarPlayer(playerI);
		//player.modificarPlayer(playerI.getUserName(), email, psw1, null);

		return player;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player register(String email, String userName, String pwd1, String pwd2) throws Exception {
		if (!pwd1.equals(pwd2))
			throw new Exception("ERROR: psw1 != psw2");
		Player player = new Player();
		player.createPlayerNormal(userName, email, pwd1, null);
		player.register(player);
		return player;
	}

	@RequestMapping(value = "/registerGoogle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player registerGoogle(String id, String email, String userName) throws Exception {
		Player player = new Player();
		player.createPlayerGoogle(id, userName, email, null);
		player.register(player);
		return player;
	}

	@RequestMapping(value = "/loginGoogle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPost(HttpSession session, String id, String userName, String email) throws Exception {
		Player player = new Player();
		Player playerI = player.identifyGoogle(id, userName, email);
		session.setAttribute("player", playerI);
		return playerI;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPostHTML(HttpSession session, String userName, String pwd) throws Exception {
		Player player = new Player();
		Player playerI = player.identify(userName, pwd);
		session.setAttribute("player", playerI);
		return playerI;
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
