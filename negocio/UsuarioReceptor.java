package negocio;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UsuarioReceptor extends Persona{
    
    private String estado;
    private byte[] publicKey;
    
    public UsuarioReceptor(String nombre, String ip, String puerto) {
        super(nombre,ip,puerto);
    }
    
    public UsuarioReceptor(){
        
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
