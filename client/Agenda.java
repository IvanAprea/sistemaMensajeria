package client;

import java.util.ArrayList;

public class Agenda {
    
    private ArrayList<Persona> personas = new ArrayList<Persona>();
			
    public Agenda() {
        super();
        personas.add(new Persona("ivan"));
        personas.add(new Persona("martin"));
        personas.add(new Persona("tuma"));
    }
    
    public void cargarAgenda(){
        
    }
    
    public ArrayList<Persona> getPersonas(){
        return this.personas;
    }
}
