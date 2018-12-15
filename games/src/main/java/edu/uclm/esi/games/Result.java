package edu.uclm.esi.games;

import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Result {
	
	@Bsonable//este campo tiene esta caracteristica 
	private String winner;
	@Bsonable
	private String userName2;
	@Bsonable
	private String userName1;

	public Result(String userName1, String userName2, String winner) {
		this.userName1=userName1;
		this.userName2=userName2;
		this.winner=winner;
	}

}
