package edu.uclm.esi.games;

import java.util.UUID;

import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Token {
	@Bsonable
	private long caducidad;
	@Bsonable
	private String userName;
	@Bsonable
	private String valor;

	public Token(String userName) {
		this.valor = UUID.randomUUID().toString();
		this.userName=userName;
		this.caducidad = System.currentTimeMillis()+6*60*100;
	}

	public String getValor() {
		// TODO Auto-generated method stub
		return valor;
	}

}
