package edu.uclm.esi.games.sudoku;

import java.util.Random;

import edu.uclm.esi.games.Board;
import edu.uclm.esi.games.Player;

public class SudokuBoard extends Board{
	
	private int board[][];
	private int sizeBoard=9;

	public  SudokuBoard(SudokuMatch match) {
		super(match);
		int random = new Random().nextInt(8);
		int x = random;
		int v = 1;
		board = new int[sizeBoard][sizeBoard];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if ((v + x + j) <= 9) {
					board[i][j] = x + j + v;
				} else {
					board[i][j] = x + j + v - 9;
				}
				if (board[i][j] == 10) {
					board[i][j] = 1;
				}
			}
			x += 3;
			if (x >= 9) {
				x = x - 9;
			} else if (i == 2) {
				v = 2;
				x = random;
			} else if (i == 5) {
				v = 3;
				x = random;
			}

		}

	}

	@Override
	public void move(Player player, int[] coordinates) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Player getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean end() {
		// TODO Auto-generated method stub
		return false;
	}

}
