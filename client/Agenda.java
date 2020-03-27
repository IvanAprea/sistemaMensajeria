package client;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.ArrayList;

public class Agenda {
    
    private FileInputStream fis;
    private ObjectInputStream entrada;
    private static Agenda instancia=null;
    private ArrayList<Persona> personas = new ArrayList<Persona>();
			
    private Agenda() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    
    public synchronized static Agenda getInstance(){
        if(instancia==null){
            instancia = new Agenda();
        }
        return instancia;
    }
    
    public void cargarAgenda(){
        try {
            
            Persona persona;
            
            fis = new FileInputStream("agenda.txt");
            entrada = new ObjectInputStream (fis);
            while(entrada!=null){
                
                personas.add((Persona)entrada.readObject());
                
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    
    public ArrayList<Persona> getPersonas(){
        return this.personas;
    }
}
