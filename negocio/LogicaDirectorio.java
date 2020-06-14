package negocio;

import base.ComunicacionDirectorio;

import interfaces.ICargaConfig;
import interfaces.IComando;
import interfaces.IGestionUsuarios;

import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.io.StringWriter;

import java.io.UnsupportedEncodingException;

import java.net.InetAddress;
import java.net.Socket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.Observable;

import javax.xml.bind.JAXBException;

public class LogicaDirectorio implements IGestionUsuarios,IComando,ICargaConfig{
    
    private static LogicaDirectorio _instancia = null;
    private UsuariosRecMap listaDirectorio;
    private boolean listaDirOcupado=false;
    private boolean usrOnlineOcupado=false;
    private ListaUsuariosOnline usuariosOnlineActuales;
    private ComunicacionDirectorio comDir;
    private final String regex=", *";
    private final String decoder="UTF8";
    
    private LogicaDirectorio() {
        super();
        listaDirectorio = new UsuariosRecMap();
        usuariosOnlineActuales = new ListaUsuariosOnline();
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
        return this.usuariosOnlineActuales.getAl();
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
                        while(LogicaDirectorio.getInstancia().isListaDirOcupado()==true ||
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
                        comDir.enviarActualizacion();
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
        this.usuariosOnlineActuales.getAl().clear();
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
            this.comDir.enviarActualizacion();
            this.listaDirOcupado=false;
            this.usrOnlineOcupado=false;
            notifyAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            
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
            comDir.darLista(sw);
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
            while(this.listaDirOcupado==true){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.listaDirOcupado = true;
            this.comDir.enviarActualizacion();
            this.listaDirOcupado = false;
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
            comDir.nuevoUsuario();
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
            comDir.setearUsuarioDesconectado();
            this.informarConsola("Usuario intentando desconectarse...");
            this.listaDirOcupado=false;
            notifyAll();
        }
        else if(comando.equalsIgnoreCase("DIR_DAR_ALIVE")){
            comDir.recibirAlive();
        }
        else if(comando.equalsIgnoreCase("BACKUP"))
        {
            this.actualizarUsuariosRec(comDir.recibirDatos());
            this.actualizarUsuariosOnline(comDir.recibirDatos());
        }
        else if(comando.equalsIgnoreCase("ACTUALIZAR_DATOS"))
        {
            while(this.listaDirOcupado==true || this.usrOnlineOcupado==true){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.listaDirOcupado=true;
            this.usrOnlineOcupado=true;
            this.comDir.enviarActualizacion();
            this.listaDirOcupado=false;
            this.usrOnlineOcupado=false;
            notifyAll();
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

    public void setComDir(ComunicacionDirectorio comDir)
    {
        this.comDir = comDir;
    }

    public ComunicacionDirectorio getComDir()
    {
        return comDir;
    }

    public void setListaDirectorio(UsuariosRecMap listaDirectorio)
    {
        this.listaDirectorio = listaDirectorio;
    }

    public void setUsuariosOnlineActuales(ListaUsuariosOnline usuariosOnlineActuales)
    {
        this.usuariosOnlineActuales = usuariosOnlineActuales;
    }
    
    public synchronized void actualizarUsuariosRec(String s)
    {
        while(this.isListaDirOcupado())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.listaDirOcupado = true;
        this.setListaDirectorio(reconvertirUsuariosRec(s));
        this.listaDirOcupado = false;
        notifyAll();
    }
    
    public UsuariosRecMap reconvertirUsuariosRec(String s)
    {
        javax.xml.bind.JAXBContext context;
        try
        {
            context = javax.xml.bind.JAXBContext.newInstance(UsuariosRecMap.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(s);
            return (UsuariosRecMap) unmarshaller.unmarshal(reader);
        } catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public synchronized String convertirUsuariosRec()
    {
        javax.xml.bind.JAXBContext context;
        try
        {
            context = javax.xml.bind.JAXBContext.newInstance(UsuariosRecMap.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(listaDirectorio, sw);
            return sw.toString();
        } catch (JAXBException e)
        {
            e.printStackTrace();
            return "";
        }
    }
    
    public synchronized void actualizarUsuariosOnline(String s)
    {
        while(this.isUsrOnlineOcupado())
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.usrOnlineOcupado=true;
        this.setUsuariosOnlineActuales(reconvertirUserOnline(s));
        this.usrOnlineOcupado=false;
        notifyAll();
    }
    
    public ListaUsuariosOnline reconvertirUserOnline(String s)
    {
        javax.xml.bind.JAXBContext context;
        try
        {
            context = javax.xml.bind.JAXBContext.newInstance(ListaUsuariosOnline.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(s);
            return (ListaUsuariosOnline) unmarshaller.unmarshal(reader);
        } catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public synchronized String convertirUserOnline()
    {
        javax.xml.bind.JAXBContext context;
        try
        {
            context = javax.xml.bind.JAXBContext.newInstance(ListaUsuariosOnline.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(usuariosOnlineActuales, sw);
            return sw.toString();
        } catch (JAXBException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void cargarDatosConfig(String s)
    {
        BufferedReader br;
        String[] datos;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(s), decoder));
            String linea = br.readLine();
            while(linea!=null){
                datos=linea.split(regex);
                comDir.setIpDir(datos[0]);
                comDir.setPuertoDir(datos[1]);
                linea = br.readLine();
            }
            br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
