package negocio;

import base.ComunicacionDirectorio;

import interfaces.IGestionUsuarios;

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

public class NegocioDirectorio implements IGestionUsuarios{
    
    private static NegocioDirectorio _instancia = null;
    private UsuariosRecMap listaDirectorio;
    private boolean listaDirOcupado=false;
    private boolean usrOnlineOcupado=false;
    private ArrayList<String> usuariosOnlineActuales;
    
    private NegocioDirectorio() {
        super();
        listaDirectorio = new UsuariosRecMap();
        usuariosOnlineActuales = new ArrayList<String>();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static NegocioDirectorio getInstancia()
    {
        if(_instancia == null)
            _instancia = new NegocioDirectorio();
        return _instancia;
    }

    public ArrayList<String> getUsuariosOnlineActuales() {
        return usuariosOnlineActuales;
    }

    public UsuariosRecMap getListaDirectorio() {
        return listaDirectorio;
    }
    
    public synchronized void comprobacionUsuariosOnline(){
        Thread tr = new Thread(){
            public synchronized void  run(){
                try {
                    HashMap<String, UsuarioReceptor> listaNueva;
                    String IDAux;
                    UsuarioReceptor usrACambiar;
                    while(true){
                        Thread.sleep(7500);
                        while(NegocioDirectorio.getInstancia().isListaDirOcupado()==true &&
                               NegocioDirectorio.getInstancia().isUsrOnlineOcupado()==true){
                            wait();
                        }
                        NegocioDirectorio.getInstancia().setListaDirOcupado(true);
                        NegocioDirectorio.getInstancia().setUsrOnlineOcupado(true);
                        listaNueva = (HashMap<String, UsuarioReceptor>) NegocioDirectorio.getInstancia().getListaDirectorio().getUsuariosRecMap();
                        Iterator it = listaNueva.entrySet().iterator();
                        ArrayList<String> usrsOnline = NegocioDirectorio.getInstancia().getUsuariosOnlineActuales();
                        while(it.hasNext()){
                            Map.Entry me = (Map.Entry) it.next();
                            IDAux = (String) me.getKey();
                            usrACambiar = listaNueva.get(IDAux);
                            if(usrACambiar.getEstado().equalsIgnoreCase("ONLINE") && (!usrsOnline.contains(IDAux))){
                                NegocioDirectorio.getInstancia().setearUsuarioDesconectado(IDAux);
                            }
                        }
                        NegocioDirectorio.getInstancia().limpiarUsuariosOnline();
                        NegocioDirectorio.getInstancia().setListaDirOcupado(false);
                        NegocioDirectorio.getInstancia().setUsrOnlineOcupado(false);
                        notifyAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Lanzar error
                }
            }
        };
        tr.start();
    }
    
    public void limpiarUsuariosOnline(){
        this.usuariosOnlineActuales.clear();
    }
    
    //Puede venir un usuario nuevo o no, por lo que se contemplan las dos situaciones
    public synchronized void nuevoUsuario(String str){
        while(this.listaDirOcupado==true && this.usrOnlineOcupado==true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.listaDirOcupado=true;
        this.usrOnlineOcupado=true;
        try {
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuarioReceptor.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(str);
            UsuarioReceptor receptor = (UsuarioReceptor)unmarshaller.unmarshal(reader);
            receptor.setEstado("ONLINE");
            this.listaDirectorio.getUsuariosRecMap().put(receptor.getID(), receptor);
            //Lo pongo en la lista de usuarios activos
            this.getUsuariosOnlineActuales().add(receptor.getID());
            this.listaDirOcupado=false;
            this.usrOnlineOcupado=false;
            notifyAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public synchronized void setearUsuarioDesconectado(String ID){
        try {
            UsuarioReceptor receptor = this.getListaDirectorio().getUsuariosRecMap().get(ID);
            receptor.setEstado("OFFLINE");
            this.listaDirectorio.getUsuariosRecMap().put(ID, receptor);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public synchronized void darLista(){
        try
        {
            while(this.listaDirOcupado==true){
                wait();
            }
            this.listaDirOcupado=true;
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuariosRecMap.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(this.getListaDirectorio(), sw);
            ComunicacionDirectorio.getInstancia().darLista(sw);     
            this.listaDirOcupado=true;
            notifyAll();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        } 
    }
    
    public synchronized void recibirAlive(String id) {
        while(this.usrOnlineOcupado==true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.usrOnlineOcupado=true;
        if(!this.getUsuariosOnlineActuales().contains(id)){
            this.getUsuariosOnlineActuales().add(id);
        }
        this.usrOnlineOcupado=false;
        notifyAll();
    }
    
    public synchronized void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("AGREGAR")){
            ComunicacionDirectorio.getInstancia().nuevoUsuario();
        }
        else if(comando.equalsIgnoreCase("GET")){
            this.darLista();
        }
        else if(comando.equalsIgnoreCase("DESCONECTAR")){
            while(this.listaDirOcupado==true){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.listaDirOcupado=true;
            ComunicacionDirectorio.getInstancia().setearUsuarioDesconectado();
            this.listaDirOcupado=false;
            notifyAll();
        }
        else if(comando.equalsIgnoreCase("ALIVE")){
            ComunicacionDirectorio.getInstancia().recibirAlive();
        }
    }


    public void setListaDirOcupado(boolean listaDirOcupado) {
        this.listaDirOcupado = listaDirOcupado;
    }

    public boolean isListaDirOcupado() {
        return listaDirOcupado;
    }

    public void setUsrOnlineOcupado(boolean usrOnlineOcupado) {
        this.usrOnlineOcupado = usrOnlineOcupado;
    }

    public boolean isUsrOnlineOcupado() {
        return usrOnlineOcupado;
    }
}
