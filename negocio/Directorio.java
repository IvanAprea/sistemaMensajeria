package negocio;

import base.ComunicacionDirectorio;

import java.awt.event.ActionEvent;

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


    public HashMap<String, UsuarioReceptor> getListaDirectorio() {
        return listaDirectorio;
    }

    //Puede venir un usuario nuevo o no, por lo que se contemplan las dos situaciones
    public void agregarALista(UsuarioReceptor receptor){
        receptor.setEstado("ONLINE");
        this.listaDirectorio.put(receptor.getID(), receptor);
    }
    
    public void darLista(){
        ComunicacionDirectorio.getInstancia().darLista(this.getListaDirectorio());            
    }
    
    public void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("AGREGAR")){
            this.agregarALista();
        }
        else if(comando.equalsIgnoreCase("GET")){
            this.darLista();
        }
    }
    
}
