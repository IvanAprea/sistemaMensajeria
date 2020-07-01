package negocio;

import base.ComunicacionDirectorio;

import base.Sincronizadora;

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

public class GestorUsuariosReceptores implements IGestionUsuarios,IComando{
    
    private static GestorUsuariosReceptores _instancia = null;
    private UsuariosRecMap listaDirectorio;
    private boolean listaDirOcupado=false;
    private boolean usrOnlineOcupado=false;
    private ArrayList<String> usuariosOnlineActuales;
    private final String regex=", *";
    private final String decoder="UTF8";
    
    private GestorUsuariosReceptores() {
        super();
        listaDirectorio = new UsuariosRecMap();
        usuariosOnlineActuales = new ArrayList<String>();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static GestorUsuariosReceptores getInstancia()
    {
        if(_instancia == null)
            _instancia = new GestorUsuariosReceptores();
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
                        while(GestorUsuariosReceptores.getInstancia().isListaDirOcupado()==true || //no seria or?
                               GestorUsuariosReceptores.getInstancia().isUsrOnlineOcupado()==true){
                            wait();
                        }
                        GestorUsuariosReceptores.getInstancia().setListaDirOcupado(true);
                        GestorUsuariosReceptores.getInstancia().setUsrOnlineOcupado(true);
                        listaNueva = (HashMap<String, UsuarioReceptor>) GestorUsuariosReceptores.getInstancia().getListaDirectorio().getUsuariosRecMap();
                        Iterator it = listaNueva.entrySet().iterator();
                        ArrayList<String> usrsOnline =
                            GestorUsuariosReceptores.getInstancia().getUsuariosOnlineActuales();
                        while(it.hasNext()){
                            Map.Entry me = (Map.Entry) it.next();
                            IDAux = (String) me.getKey();
                            usrACambiar = listaNueva.get(IDAux);
                            if(usrACambiar.getEstado().equalsIgnoreCase("ONLINE") && (!usrsOnline.contains(IDAux))){
                                GestorUsuariosReceptores.getInstancia().setearUsuarioDesconectado(IDAux);
                            }
                        }
                        GestorUsuariosReceptores.getInstancia().limpiarUsuariosOnline();
                        GestorUsuariosReceptores.getInstancia().setListaDirOcupado(false);
                        GestorUsuariosReceptores.getInstancia().setUsrOnlineOcupado(false);
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
    public synchronized void nuevoUsuario(boolean conSync){
        String usr = ComunicacionDirectorio.getInstancia().leerDIn();
        if(conSync){
            Sincronizadora.getInstancia().nuevoUsuario(usr);
        }
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
            StringReader reader = new StringReader(usr);
            UsuarioReceptor receptor = (UsuarioReceptor)unmarshaller.unmarshal(reader);
            receptor.setEstado("ONLINE");
            //actualizo ip y puerto en usuariosRecMap
            this.listaDirectorio.getUsuariosRecMap().put(receptor.getNombre(), receptor);
            //Lo pongo en la lista de usuarios activos
            this.getUsuariosOnlineActuales().add(receptor.getNombre()); 
            this.listaDirOcupado=false;
            this.usrOnlineOcupado=false;
            notifyAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public synchronized void desconectarUsuario(boolean conSync){
        String nombre = ComunicacionDirectorio.getInstancia().leerDIn();
        if(conSync){
            Sincronizadora.getInstancia().desconectarUsuario(nombre);
        }
        while(this.listaDirOcupado==true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.listaDirOcupado=true;
        this.setearUsuarioDesconectado(nombre);
        this.informarConsola("Usuario intentando desconectarse...");
        this.listaDirOcupado=false;
        notifyAll();

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
    
    public synchronized void recibirAlive(boolean conSync) { 
        String nombre = ComunicacionDirectorio.getInstancia().leerDIn(); 
        if(conSync){
            Sincronizadora.getInstancia().recibirAlive(nombre);
        }
        while(this.usrOnlineOcupado==true){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.usrOnlineOcupado=true;
        this.informarConsola("Alive recibido de "+nombre);
        if(!this.getUsuariosOnlineActuales().contains(nombre)){
            this.getUsuariosOnlineActuales().add(nombre);
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
            this.nuevoUsuario(true);
        }
        else if(comando.equalsIgnoreCase("DIR_GETLISTA")){
            this.darLista();
        }
        else if(comando.equalsIgnoreCase("DIR_DESCONECTAR_REC")){
            this.desconectarUsuario(true);
        }
        else if(comando.equalsIgnoreCase("DIR_DAR_ALIVE")){
            this.recibirAlive(true);
        }
        else if(comando.equalsIgnoreCase("DIR_DAR_IDREC")){
            this.mandarIDRec();
        }
        else if(comando.equalsIgnoreCase("DIR_SYNC_AGREGAR")){
            this.nuevoUsuario(false);
        }
        else if(comando.equalsIgnoreCase("DIR_SYNC_DESCONECTAR")){
            this.desconectarUsuario(false);
        }
        else if(comando.equalsIgnoreCase("DIR_SYNC_ALIVE")){
            this.recibirAlive(false);
        }
        else if(comando.equalsIgnoreCase("DIR_SYNC_BACKUP")){ // ACA PEDIRIA EL HASHMAP COMPLETO, EL TEMA ES A QUIEN
            this.enviarBackUp();
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
    
    public void cargarDatosConfig(String s)
        {
            BufferedReader br;
            String[] datos;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(s), decoder));
                String linea = br.readLine();
                while(linea!=null){
                    datos=linea.split(regex);
                    for (int i=0; i<datos.length; i++){
                        Sincronizadora.getInstancia().getDireccionesDirectorios().add(datos[i]);
                    }
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
    
    public boolean isUsrOnlineOcupado() {
        return usrOnlineOcupado;
    }

    public void mandarIDRec() {
        String nombre = ComunicacionDirectorio.getInstancia().leerDIn();
        UsuarioReceptor rec = this.getListaDirectorio().getUsuariosRecMap().get(nombre);
        ComunicacionDirectorio.getInstancia().darIDRec(rec.getIP(), rec.getPuerto());
    }

    @Override
    public void setearUsuarioDesconectado(String nombre) {
        try {
            UsuarioReceptor receptor = this.getListaDirectorio().getUsuariosRecMap().get(nombre);
            receptor.setEstado("OFFLINE");
            this.listaDirectorio.getUsuariosRecMap().put(nombre, receptor);
            this.informarConsola("Se ha desconectado al usuario "+nombre);
        }
        catch (Exception e){
            this.informarConsola("ERROR al desconectar usuario "+nombre);
            e.printStackTrace();
        }
    }


    private void enviarBackUp() {
        //enviar copia del hashmap al que lo solicita
    }
}
