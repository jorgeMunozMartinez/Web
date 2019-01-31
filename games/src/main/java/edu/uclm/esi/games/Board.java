package edu.uclm.esi.games;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Board {
	@JsonIgnore
	protected Match match;
	
	public Board(Match match) {
		this.match=match;
	}

	public abstract void move(Player player, int[] coordinates) throws Exception;
	public abstract boolean win(Player player);
}
