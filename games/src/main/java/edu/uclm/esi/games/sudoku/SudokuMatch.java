package edu.uclm.esi.games.sudoku;

import java.util.Random;

import edu.uclm.esi.games.Player;
import edu.uclm.esi.games.Match;

public class SudokuMatch extends Match{

	public SudokuMatch() {
		super();
		this.board= new SudokuBoard(this);
	}
	
	public void calculateFirstPlayer() {
		boolean dado=new Random().nextBoolean();
		this.currentPlayer=dado ? 0 : 1;
		this.currentPlayer=0;   // puesto a propósito con fines de desarrollo y test para que empiece Pepe
	}
	
	protected boolean tieneElTurno(Player player) {
		return true;
	}

	@Override
	protected void save() throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
