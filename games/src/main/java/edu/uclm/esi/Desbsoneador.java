package edu.uclm.esi;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

public class Desbsoneador {

	public static Object get(Class<Persona> clase, BsonDocument bso)
			throws InstantiationException, IllegalAccessException {
		Object result = clase.newInstance();// nuevo objeto vacio
		for (java.lang.reflect.Field i : clase.getDeclaredFields()) {
			i.setAccessible(true);
			System.out.println(i.getName() + " : " + i.getType().getName());
			if (i.isAnnotationPresent(Insertable.class)) {
				Object valor = bso.get(i.getName());
				if (valor != null) {
					if (i.getType() == String.class) {
						i.set(result, ((BsonString) valor).asString().getValue());
					} else if (i.getType() == Integer.class) {
						i.set(result, ((BsonInt32) valor).asInt32().getValue());
					}
				}
			}
		}
		return result;
	}

}
