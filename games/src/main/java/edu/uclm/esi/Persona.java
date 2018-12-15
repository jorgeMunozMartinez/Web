package edu.uclm.esi;
import org.bson.BsonDocument;

import edu.uclm.esi.mongolabels.dao.MongoBroker;
import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Persona {
	@Insertable
	private String nombre;
	@Insertable
	private int edad;
	@Insertable
	private Persona madre;
	
	public Persona() {

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public static void main(String []args) throws Exception {
		Persona madre=new Persona();
		madre.setNombre("Juana");
		
		Persona ana = new Persona();
		ana.setNombre("Ana");
		ana.setEdad(25);
		ana.setMadre(madre);

		System.out.println(ana.getClass().getName());
		BsonDocument bsoAna=Bsoneador.get(ana);
		System.out.println(bsoAna);
		
		BsonDocument bsoJuana=Bsoneador.get(madre);
		System.out.println(bsoJuana);
		
		//insert sql
//		BsonBroker.insert(ana.getClass().getName(),bsoAna);
//		BsonBroker.insert(ana.getClass().getName(),bsoJuana);
		
		Persona ana2=(Persona) Desbsoneador.get(Persona.class,bsoJuana);
	}

	private void setMadre(Persona madre) {
		this.madre=madre;
		
	}
}
