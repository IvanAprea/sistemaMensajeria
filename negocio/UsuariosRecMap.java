package negocio;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.HashMap;
import java.util.Map;

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
