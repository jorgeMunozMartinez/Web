package edu.uclm.esi;

import edu.uclm.esi.mongolabels.dao.MongoBroker;

public class LimpiarBBDD {
	public static void main(String[] args) {
		MongoBroker.get().drop("Player");
		MongoBroker.get().drop("Token");
	}

}
