package negocio;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MensajeEmisor extends Mensaje
{
    private UsuarioReceptor receptor;
    
    public MensajeEmisor(byte[] asunto, byte[] texto, Persona persona, int tipo, UsuarioReceptor receptor)
    {
        super(asunto, texto, persona, tipo);
        this.receptor=receptor;
    }

    public MensajeEmisor()
    {
        super();
    }

    public UsuarioReceptor getReceptor()
    {
        return receptor;
    }

    public void setReceptor(UsuarioReceptor receptor)
    {
        this.receptor = receptor;
    }
    
}
