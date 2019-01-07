package edu.uclm.esi.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
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

@RestController
public class UserControllerPost {
	
	@RequestMapping(value = "/register", method=RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player register(String email, String userName, String pwd1, String pwd2, String img) throws Exception {
		if (!pwd1.equals(pwd2))
			throw new Exception("ERROR: psw1 != psw2");
		Player player = new Player ();
		player.createPlayer(userName, email, pwd1, img, null);
		player.register(player);
		return player;
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Player loginPost(HttpSession session, String userName, String pwd) throws Exception {
		Player player = new Player();
		Player playerI = player.identify(userName, pwd);
		session.setAttribute("player", playerI);
		return playerI;
	}
	
	@RequestMapping(value= {"/joinGame", "/post/joinGame"}, method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE) 
	public Match joinGamePost(HttpSession session, @RequestBody String gameName) throws Exception {
		Player player=(Player) session.getAttribute("player");
		if (player==null)
			throw new Exception("You need to be logged");
		Match match=Manager.get().joinGame(player, gameName.substring(0, gameName.length()-1));
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
	
	@RequestMapping(value="/autenticarConTwitter", method=RequestMethod.GET)
	public void autenticarConTwitter(HttpServletResponse response) throws ClientProtocolException, IOException {
		String consumerKey="";
		String consumerSecret="";
		JSONObject keys = new JSONObject();
		keys.put("consumerKey", consumerKey);
		keys.put("consumerSecret", consumerSecret);
		String p=URLEncoder.encode(keys.toString(), "UTF-8");
		
		String url="http://localhost:9900/autenticadorMaven/getToken.jsp?p=" +p;
		HttpClient cliente=HttpClientBuilder.create().build();
		HttpGet get= new HttpGet(url);
		HttpResponse resultado=cliente.execute(get);
		String urlAutenticador=resultado.getHeaders("urlAutenticador")[0].getValue();
		response.sendRedirect(urlAutenticador);
	}
}
