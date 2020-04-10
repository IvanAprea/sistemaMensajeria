package negocio;

import base.ComunicacionDirectorio;

import java.util.HashMap;

public class Directorio {
    
    private static Directorio _instancia = null;
    private HashMap<String, UsuarioReceptor> listaDirectorio;
    
    private Directorio() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static Directorio getInstancia()
    {
        if(_instancia == null)
            _instancia = new Directorio();
        return _instancia;
    }
    
    //Puede venir un usuario nuevo o no, por lo que se contemplan las dos situaciones
    public void agregarALista(UsuarioReceptor receptor){
        receptor.setEstado("ONLINE");
        this.listaDirectorio.put(receptor.getID(), receptor);
    }
    
}
