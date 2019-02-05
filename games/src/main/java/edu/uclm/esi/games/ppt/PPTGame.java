package edu.uclm.esi.games.ppt;

import edu.uclm.esi.games.Game;
import edu.uclm.esi.games.Match;

public class PPTGame extends Game {
	
	public PPTGame() {
		super(2);
	}

	@Override
	public String getName() {
		return "ppt";
	}

	@Override
	protected Match createMatch() {
		// TODO Auto-generated method stub
		return new PPTMatch();
	}

}
