package edu.uclm.esi.games.sudoku;

import java.util.Random;

public class pruebas {

	public static void main(String[] args) {		
		int sizeBoard = 9;
		int[][] board = new int[sizeBoard][sizeBoard];
		int p = 1;
		int random = new Random().nextInt(8);
		
			int x = random;
			int v = 1;
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
		

        for (int y = 0; y < 9; y++) {
            for (int z = 0; z < 9; z++) {
                System.out.print(board[y][z] + " ");
            }
            System.out.println("");
        }
	}
}
