package negocio;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.net.InetAddress;
import java.net.URL;

public class Persona {
    
    private String IP,puerto="80",nombre = "default";
    
    public Persona(String nombre,String ip,String puerto) {
        super();
        this.nombre=nombre;
        this.IP=ip;
        this.puerto=puerto;
    }
    
    public void setearIp(){
        try 
        {
            this.IP = InetAddress.getLocalHost().getHostAddress();//in.readLine();
        } 
        catch(Exception e) 
        {
            e.printStackTrace(); 
        }
    }
    
    public Persona() {
    	super();
    }

    public String getIP() {
        return IP;
    }

    public String getPuerto() {
        return puerto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPuerto(String Puerto) {
        this.puerto = Puerto;
    }

    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }

    @Override
    public String toString() {
        return this.getNombre();
    }

}
