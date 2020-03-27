package client;

public class Persona {
    
    private String IP,Puerto,Nombre = "default",Apellido = "default";
    
    public Persona(String nombre) {
        super();
        this.Nombre=nombre;
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
