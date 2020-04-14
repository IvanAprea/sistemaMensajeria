package negocio;

import base.ComunicacionDirectorio;

import java.awt.event.ActionEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;

import java.io.StringWriter;

import java.net.InetAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Directorio {
    
    private static Directorio _instancia = null;
    private UsuariosRecMap listaDirectorio;
    private ArrayList<String> usuariosOnlineActuales;
    
    private Directorio() {
        super();
        listaDirectorio = new UsuariosRecMap();
        usuariosOnlineActuales = new ArrayList<String>();
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

    public ArrayList<String> getUsuariosOnlineActuales() {
        return usuariosOnlineActuales;
    }

    public UsuariosRecMap getListaDirectorio() {
        return listaDirectorio;
    }
    
    //INICIAR
    public synchronized void comprobacionUsuariosOnline(){
        Thread tr = new Thread(){
            public void run(){
                try {
                    HashMap<String, UsuarioReceptor> listaNueva;
                    String IDAux;
                    UsuarioReceptor usrACambiar;
                    while(true){
                        Thread.sleep(7500);
                        listaNueva = (HashMap<String, UsuarioReceptor>) Directorio.getInstancia().getListaDirectorio().getUsuariosRecMap();
                        Iterator it = listaNueva.entrySet().iterator();
                        ArrayList<String> usrsOnline = Directorio.getInstancia().getUsuariosOnlineActuales();
                        while(it.hasNext()){
                            Map.Entry me = (Map.Entry) it.next();
                            IDAux = (String) me.getKey();
                            usrACambiar = listaNueva.get(IDAux);
                            if(usrACambiar.getEstado().equalsIgnoreCase("ONLINE") && (!usrsOnline.contains(IDAux))){
                                Directorio.getInstancia().setearUsuarioDesconectado(IDAux);
                            }
                        }
                    }
                } catch (Exception e) {
                    //Lanzar error
                }
            }
        };
        tr.start();
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
            //Lo pongo en la lista de usuarios activos
            this.getUsuariosOnlineActuales().add(receptor.getID());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void setearUsuarioDesconectado(String ID){
        try {
            UsuarioReceptor receptor = this.getListaDirectorio().getUsuariosRecMap().get(ID);
            receptor.setEstado("OFFLINE");
            this.listaDirectorio.getUsuariosRecMap().put(ID, receptor);
        }
        catch (Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
        } 
    }
    
    public void recibirAlive(String id) {
        if(!this.getUsuariosOnlineActuales().contains(id)){
            this.getUsuariosOnlineActuales().add(id);
        }
    }
    
    public void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("AGREGAR")){
            ComunicacionDirectorio.getInstancia().nuevoUsuario();
        }
        else if(comando.equalsIgnoreCase("GET")){
            this.darLista();
        }
        else if(comando.equalsIgnoreCase("DESCONECTAR")){
            ComunicacionDirectorio.getInstancia().setearUsuarioDesconectado();
        }
        else if(comando.equalsIgnoreCase("ALIVE")){
            ComunicacionDirectorio.getInstancia().recibirAlive();
        }
    }


}
