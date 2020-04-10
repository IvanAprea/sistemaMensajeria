package negocio;

public class UsuarioReceptor {
    
    private String ID, nombre, ip, puerto, estado;
    
    public UsuarioReceptor(String ID, String nombre, String ip, String puerto) {
        super();
        this.setID(ID);
        this.setNombre(nombre);
        this.setIp(ip);
        this.setPuerto(puerto);
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIp() {
        return ip;
    }

    public String getPuerto() {
        return puerto;
    }

    public String getEstado() {
        return estado;
    }
}
