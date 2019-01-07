package edu.uclm.esi.web;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;

@RestController
public class UserControllerGet {

	
	@RequestMapping(value="/login", method=RequestMethod.GET,consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player login(HttpSession session, String userName, String pwd) throws Exception {
		Player player = new Player();
		Player playerI = player.identify(userName, pwd);
		session.setAttribute("player", playerI);
		return playerI;
	}
	
	@RequestMapping(value="/joinGame", method=RequestMethod.GET)
	public Match joinGame(HttpSession session, @RequestParam(value="gameName") String gameName) throws Exception {
		Player player=(Player) session.getAttribute("player");
		if (player==null)
			throw new Exception("You need to be logged");
		Match match=Manager.get().joinGame(player, gameName);
		return match;
	}
		
	@RequestMapping("/games")
	public JSONArray games() throws Exception {
		return Manager.get().getGames();
	}
}
