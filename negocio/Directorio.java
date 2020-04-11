package negocio;

import base.ComunicacionDirectorio;

import java.awt.event.ActionEvent;

import java.io.StringReader;

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
    public void agregarALista(String str){
            try {
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuarioReceptor.class);
                javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
                StringReader reader = new StringReader(str);
                UsuarioReceptor receptor = (UsuarioReceptor)unmarshaller.unmarshal(reader);
                receptor.setEstado("ONLINE");
                this.listaDirectorio.put(receptor.getID(), receptor);
            }
            catch (Exception e){
                
            }
        }
    
    public void darLista(){
        ComunicacionDirectorio.getInstancia().darLista(this.getListaDirectorio());            
    }
    
    public void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("AGREGAR")){
            
        }
        else if(comando.equalsIgnoreCase("GET")){
            this.darLista();
        }
    }
    
}
