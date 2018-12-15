package edu.uclm.esi;

import org.bson.BsonDocument;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class BsonBroker {

	public static void insert(String collectionName, BsonDocument bso) {
		String url="mongodb://tysweb:tysweb20182019@ds159782.mlab.com:59782/juegos";
		MongoClientURI clientUri=new MongoClientURI(url);
		MongoClient client = new MongoClient(clientUri);
		MongoDatabase db=client.getDatabase("juegos");
		db.getCollection(collectionName, BsonDocument.class).insertOne(bso);
	}

}
