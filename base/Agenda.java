package base;

import java.io.BufferedReader;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;

import negocio.Persona;

public class Agenda {
    
    
    private static Agenda instancia=null;
    private HashMap<String, Persona> personas;
			
    private Agenda() {
        super();
        personas = new HashMap<String, Persona>();
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
        final String regex=", *";
        final String nombreAgenda="agenda.txt";
        final String decoder="UTF8";
        final int cantDatos=3;
        
        BufferedReader br;
        String[] datos;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(nombreAgenda), decoder));
            String linea = br.readLine();
            while(linea!=null){
                datos=linea.split(regex);
                if(datos.length == 4){
                    this.personas.put(datos[2]+":"+datos[3], new Persona(datos[0],datos[1],datos[2],datos[3]));
                }
                linea=br.readLine();
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public HashMap<String, Persona> getPersonas(){
        return this.personas;
    }
    
    public Persona getPersona(String ipPuerto){
        return getPersonas().get(ipPuerto);
    }
}