package edu.uclm.esi;

import org.bson.BsonDocument;

import edu.uclm.esi.mongolabels.dao.MongoBroker;

public class BorrarPlayers {
	
	public static void main(String []args) {
		MongoBroker.get().delete("Player", new BsonDocument());
	}

}
