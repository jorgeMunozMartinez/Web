package edu.uclm.esi.games;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.Vector;

import edu.uclm.esi.mongolabels.labels.Bsonable;

public abstract class Match {
	@Bsonable
	protected UUID id;
	@Bsonable
	protected String identif;
	@Bsonable
	protected Vector<Player> players;
	@Bsonable
	protected int currentPlayer;
	@Bsonable
	protected Player winner;
	@Bsonable
	protected Board board;
	
	public Match() {
		SecureRandom random = new SecureRandom();
		this.identif = new BigInteger(130, random).toString(32);
		this.id=UUID.randomUUID();
		this.players=new Vector<>();
		this.currentPlayer=-1;
	}
	
	public UUID getId() {
		return id;
	}

	public void addPlayer(Player player) {
		this.players.add(player);
		player.setCurrentMatch(this);
	}
	
	public Vector<Player> getPlayers() {
		return players;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public Match move(Player player, int[] coordinates) throws Exception {
		if (this.players.get(currentPlayer)!=player)
			throw new Exception("You are not the current player");
		if (this.winner!=null)
			throw new Exception("The match is finished");
		this.board.move(player, coordinates);
		this.currentPlayer=(this.currentPlayer+1)%this.players.size();
		if (this.board.win(player))
			this.winner=player;
		return this;
	}

	public abstract void calculateFirstPlayer();

	protected abstract void save() throws Exception ;

}
