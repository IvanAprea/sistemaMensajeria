package negocio;


import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement 
@XmlAccessorType(XmlAccessType.FIELD)
public class UsuariosRecMap {
    private Map<String, UsuarioReceptor> usuariosRecMap = new HashMap<String, UsuarioReceptor>();
    
    public Map<String, UsuarioReceptor> getUsuariosRecMap() {
        return usuariosRecMap;
    }
    
    public void setUsuariosRecMap(Map<String, UsuarioReceptor> usuariosRecMap) {
        this.usuariosRecMap = usuariosRecMap;
    }
    
}
