package negocio;

import base.ComunicacionDirectorio;

import java.awt.event.ActionEvent;

import java.io.StringReader;

import java.io.StringWriter;

import java.util.HashMap;

public class Directorio {
    
    private static Directorio _instancia = null;
    private UsuariosRecMap listaDirectorio;
    
    private Directorio() {
        super();
        listaDirectorio = new UsuariosRecMap();
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


    public UsuariosRecMap getListaDirectorio() {
        return listaDirectorio;
    }

    //Puede venir un usuario nuevo o no, por lo que se contemplan las dos situaciones
    public void agregarALista(String str){
            try {
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuarioReceptor.class);
                javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
                StringReader reader = new StringReader(str);
                UsuarioReceptor receptor = (UsuarioReceptor)unmarshaller.unmarshal(reader);
                System.out.println(receptor.getNombre());
                receptor.setEstado("ONLINE");
                this.listaDirectorio.getUsuariosRecMap().put(receptor.getID(), receptor);
            }
            catch (Exception e){
                
            }
        }
    
    public void darLista(){
        try
        {
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuariosRecMap.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(this.getListaDirectorio(), sw);
            ComunicacionDirectorio.getInstancia().darLista(sw);      
        }
        catch(Exception e)
        {
            
        } 
    }
    
    public void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("AGREGAR")){
            ComunicacionDirectorio.getInstancia().nuevoUsuario();
        }
        else if(comando.equalsIgnoreCase("GET")){
            this.darLista();
        }
    }
    
}
