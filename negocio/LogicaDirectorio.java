package negocio;

import base.ComunicacionDirectorio;

import interfaces.IComando;
import interfaces.IGestionUsuarios;

import java.awt.event.ActionEvent;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;

import java.io.StringWriter;

import java.net.InetAddress;
import java.net.Socket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LogicaDirectorio implements IGestionUsuarios,IComando{
    
    private static LogicaDirectorio _instancia = null;
    private UsuariosRecMap listaDirectorio;
    private boolean listaDirOcupado=false;
    private boolean usrOnlineOcupado=false;
    private ArrayList<String> usuariosOnlineActuales;
    
    private LogicaDirectorio() {
        super();
        listaDirectorio = new UsuariosRecMap();
        usuariosOnlineActuales = new ArrayList<String>();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static LogicaDirectorio getInstancia()
    {
        if(_instancia == null)
            _instancia = new LogicaDirectorio();
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
                        while(LogicaDirectorio.getInstancia().isListaDirOcupado()==true || //no seria or?
                               LogicaDirectorio.getInstancia().isUsrOnlineOcupado()==true){
                            wait();
                        }
                        LogicaDirectorio.getInstancia().setListaDirOcupado(true);
                        LogicaDirectorio.getInstancia().setUsrOnlineOcupado(true);
                        listaNueva = (HashMap<String, UsuarioReceptor>) LogicaDirectorio.getInstancia().getListaDirectorio().getUsuariosRecMap();
                        Iterator it = listaNueva.entrySet().iterator();
                        ArrayList<String> usrsOnline = LogicaDirectorio.getInstancia().getUsuariosOnlineActuales();
                        while(it.hasNext()){
                            Map.Entry me = (Map.Entry) it.next();
                            IDAux = (String) me.getKey();
                            usrACambiar = listaNueva.get(IDAux);
                            if(usrACambiar.getEstado().equalsIgnoreCase("ONLINE") && (!usrsOnline.contains(IDAux))){
                                LogicaDirectorio.getInstancia().setearUsuarioDesconectado(IDAux);
                            }
                        }
                        LogicaDirectorio.getInstancia().limpiarUsuariosOnline();
                        LogicaDirectorio.getInstancia().setListaDirOcupado(false);
                        LogicaDirectorio.getInstancia().setUsrOnlineOcupado(false);
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
            this.informarConsola("Se ha desconectado al usuario "+ID);
        }
        catch (Exception e){
            this.informarConsola("ERROR al desconectar usuario "+ID);
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
            this.informarConsola("Intentando enviar lista de receptores");
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuariosRecMap.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(this.getListaDirectorio(), sw);
            ComunicacionDirectorio.getInstancia().darLista(sw);
            this.informarConsola("Lista de receptores enviada con exito");
            this.listaDirOcupado=false;
            notifyAll();
        }
        catch(Exception e)
        {
            this.informarConsola("ERROR al enviar lista de receptores");
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
        this.informarConsola("Alive recibido de "+id);
        if(!this.getUsuariosOnlineActuales().contains(id)){
            this.getUsuariosOnlineActuales().add(id);
        }
        this.usrOnlineOcupado=false;
        notifyAll();
    }
    
    public void informarConsola(String log){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println("["+dtf.format(now)+"]    "+log);      
    }
    
    public synchronized void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("DIR_AGREGAR_REC")){
            ComunicacionDirectorio.getInstancia().nuevoUsuario();
        }
        else if(comando.equalsIgnoreCase("DIR_GETLISTA")){
            this.darLista();
        }
        else if(comando.equalsIgnoreCase("DIR_DESCONECTAR_REC")){
            while(this.listaDirOcupado==true){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.listaDirOcupado=true;
            ComunicacionDirectorio.getInstancia().setearUsuarioDesconectado();
            this.informarConsola("Usuario intentando desconectarse...");
            this.listaDirOcupado=false;
            notifyAll();
        }
        else if(comando.equalsIgnoreCase("DIR_DAR_ALIVE")){
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
