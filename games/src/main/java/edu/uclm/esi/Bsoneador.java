package edu.uclm.esi;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.BsonValue;

public class Bsoneador {

	public static BsonDocument get(Object object) throws IllegalArgumentException, IllegalAccessException {
		BsonDocument result= new BsonDocument();
		//recorrer campos y si alguno tiene la eqtiqueta insertable se mete
		Class clase=object.getClass();
		for(java.lang.reflect.Field i: clase.getDeclaredFields()) {
			i.setAccessible(true);
			System.out.println(i.getName()+" : " +i.getType().getName());
			if(i.isAnnotationPresent(Insertable.class)) {
				//result.put("valorEntero", new BsonInt32(25));
				//result.put("valorCadena", new BsonString("Hola"));
				Object valor=i.get(object);
				if(valor!=null)
					result.put(i.getName(),getBsonValue(valor) );
			}
		}
		return result;
	}

	private static BsonValue getBsonValue(Object valor) throws IllegalArgumentException, IllegalAccessException {
		Class tipo= valor.getClass();
		if(tipo==String.class)
			return new BsonString(valor.toString());
		else if (tipo==Integer.class)
			return new BsonInt32(Integer.parseInt(valor.toString()));
		else//si tienen algunos objetos como prametro
			return get(valor);
	}

	
	
}
