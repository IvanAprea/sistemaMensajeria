package client;

public class Persona {
    
    private String IP,Puerto="80",Nombre = "default",Apellido = "default";
    
    public Persona(String nombre,String apellido,String ip,String puerto) {
        super();
        this.Nombre=nombre;
        this.Apellido=apellido;
        this.IP=ip;
        this.Puerto=puerto;
    }
    
    public Persona() {
    	super();
    }

    public String getIP() {
        return IP;
    }

    public String getPuerto() {
        return Puerto;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPuerto(String Puerto) {
        this.Puerto = Puerto;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    @Override
    public String toString() {
        
        return this.getNombre();
    }

}
