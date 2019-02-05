package edu.uclm.esi.games.ppt;

import java.io.IOException;

import edu.uclm.esi.games.Board;
import edu.uclm.esi.games.Player;
import edu.uclm.esi.web.ws.WSServer;

public class PPTBoard extends Board {

	// Piedra = 0, papel = 1, tijera = 2

	private final static int PIEDRA = 0;
	private final static int PAPEL = 1;
	private final static int TIJERA = 2;

	private int[] tiradas0, tiradas1;
	private int v0, v1 = 0;
	private int tirada = 0;

	public PPTBoard(PPTMatch pptMatch) {
		super(pptMatch);
		this.tiradas0 = new int[] { -1, -1, -1 };
		this.tiradas1 = new int[] { -1, -1, -1 };
	}

	@Override
	public void move(Player player, int coordinates) throws Exception {

		int pos;
		if (tirada < 3) {
			if (this.match.getPlayers().get(0) == player) {
				pos = rellenar(tiradas0, coordinates);
				if (tiradas1[pos] == -1) {
					WSServer.result(this.match.getPlayers().get(0), null, "espera", null, null, 0, 0, 0);
				} else {
					partida(pos);
				}
			} else {
				pos = rellenar(tiradas1, coordinates);
				if (tiradas0[pos] == -1) {
					WSServer.result(this.match.getPlayers().get(1), null, "espera", null, null, 0, 0, 0);
				} else {
					partida(pos);
				}
			}
		} else {
			WSServer.endGame(this.match.getPlayers().get(0), this.match.getPlayers().get(1), v0, v1);
		}

	}

	public void partida(int pos) throws IOException {
		tirada = tirada + 1;
		int selec0 = tiradas0[pos];
		int selec1 = tiradas1[pos];
		Player player0 = this.match.getPlayers().get(0);
		Player player1 = this.match.getPlayers().get(1);
		if (selec0 == 0 && selec1 == 0) {// piedra piedra
			WSServer.result(player0, player1, "empate", "stone", "stone", v0, v1, tirada);
		} else if (selec0 == 0 && selec1 == 1) {// piedra papel
			v1 = v1 + 1;
			WSServer.result(player1, player0, "gana", "paper", "stone", v1, v0, tirada);
		} else if (selec0 == 0 && selec1 == 2) {// piedra tijera
			v0 = v0 + 1;
			WSServer.result(player0, player1, "gana", "stone", "scissors", v0, v1, tirada);
		} else if (selec0 == 1 && selec1 == 0) {// papel piedra
			v0 = v0 + 1;
			WSServer.result(player0, player1, "gana", "paper", "stone", v0, v1, tirada);
		} else if (selec0 == 1 && selec1 == 1) {// papel papel
			WSServer.result(player0, player1, "empate", "paper", "papel", v0, v1, tirada);
		} else if (selec0 == 1 && selec1 == 2) {// papel tijera
			v1 = v1 + 1;
			WSServer.result(player1, player0, "gana", "scissors", "paper", v1, v0, tirada);
		} else if (selec0 == 2 && selec1 == 0) {// tijera piedra
			v1 = v1 + 1;
			WSServer.result(player1, player0, "gana", "scissors", "stone", v1, v0, tirada);
		} else if (selec0 == 2 && selec1 == 1) {// tijera papel
			v0 = v0 + 1;
			WSServer.result(player0, player1, "gana", "scissors", "paper", v0, v1, tirada);
		} else if (selec0 == 2 && selec1 == 2) {// tijera tijera
			WSServer.result(player0, player1, "empate", "scissors", "scissors", v0, v1, tirada);
		}
	}

	private int rellenar(int[] tiradas, int valor) {
		System.out.println(valor);
		for (int i = 0; i < tiradas.length; i++) {
			if (tiradas[i] == -1) {
				tiradas[i] = valor;
				return i;
			}
		}

		return -1;
	}

	@Override
	public Player getWinner() {
		// TODO Auto-generated method stub
		for (int i = 0; i < tiradas0.length; i++) {
			if (tiradas0[i] == -1 || tiradas1[i] == -1)
				return null;
		}
		return (gana(tiradas1, tiradas0));
	}

	private Player gana(int[] a, int[] b) {
		int victoriasA = 0, victoriasB = 0;
		for (int i = 0; i < a.length; i++) {
			if (gana(a[i], b[i]))
				victoriasA++;
			else
				victoriasB++;
		}
		return victoriasA > victoriasB ? this.match.getPlayers().get(0) : this.match.getPlayers().get(1);
	}

	private boolean gana(int a, int b) {
		if (a == PIEDRA && b == TIJERA)
			return true;
		if (a == TIJERA && b == PAPEL)
			return true;
		if (a == PAPEL && b == PIEDRA)
			return true;
		return false;
	}

	public int[] getTiradas1() {
		return tiradas0;
	}

	public int[] getTiradas2() {
		return tiradas1;
	}

	@Override
	public boolean end() {
		if (this.getWinner() != null) {
			return true;
		}
		for (int i = 0; i < tiradas0.length; i++) {
			if (tiradas0[i] != -1 || tiradas1[1] == -1) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean win(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

}
