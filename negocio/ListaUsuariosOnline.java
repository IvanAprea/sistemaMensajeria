package negocio;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaUsuariosOnline
{
    private ArrayList<String> usuariosOnline = new ArrayList<String>();
    

    public void setAl(ArrayList<String> al)
    {
        this.usuariosOnline = al;
    }

    public ArrayList<String> getAl()
    {
        return usuariosOnline;
    }
}
