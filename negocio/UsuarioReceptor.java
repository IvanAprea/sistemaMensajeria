package negocio;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsuarioReceptor extends Persona{
    
    private String ID, estado;
    private byte[] publicKey;
    
    public UsuarioReceptor(String ID, String nombre, String ip, String puerto) {
        super(nombre,ip,puerto);
        this.setID(ID);
    }
    
    public UsuarioReceptor(){
        
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getID() {
        return ID;
    }

    public String getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return super.toString()+"   "+this.getEstado();
    }

    public byte[] getPublicKey()
    {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
