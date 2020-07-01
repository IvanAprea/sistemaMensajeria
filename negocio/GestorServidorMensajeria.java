package negocio;

import base.ComunicacionDirectorio;
import base.ComunicacionMensajeria;

import base.Persistencia;

import base.PersistenciaXML;

import interfaces.IBackUp;

import interfaces.IBackUpMensajeria;

import interfaces.ICargaConfig;
import interfaces.IEjecutarComando;
import interfaces.IEnviarMensajeMens;

import interfaces.IEnviarPendientes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.io.StringWriter;

import java.io.UnsupportedEncodingException;

import java.net.InetAddress;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import javax.xml.bind.JAXBException;
public class GestorServidorMensajeria implements ICargaConfig,IBackUpMensajeria,IEnviarMensajeMens,IEnviarPendientes,IEjecutarComando{

    private static final String fileNoEnviados="noEnviadosMensajeria.txt",fileNoEnviadosCAviso="noEnviadosCAvisoMensajeria.txt",fileAvisosPendientes="avisosPendientesMensajeria.txt";
    private final String regex=", *";
    private final String decoder="UTF8";
    private String IPDirectorio, puertoDirectorio,IPDirRedundante,PuertoDirRedundante;
    private static GestorServidorMensajeria _instancia = null;
    private HashMap<String, ArrayList<String>> mensajesNoEnviados;
    private HashMap<String, ArrayList<String>> mensajesNoEnviadosCAviso;
    private HashMap<String, ArrayList<String>> avisosPendientes;
    private boolean mensajesNoEnviadosOcup=false,mensajesNoEnviadosCAvisoOcup=false,avisosPendientesOcup=false;
    
    private GestorServidorMensajeria() {
        super();
        this.mensajesNoEnviados = new HashMap<String, ArrayList<String>>();
        this.mensajesNoEnviadosCAviso = new HashMap<String, ArrayList<String>>();
        this.avisosPendientes = new HashMap<String, ArrayList<String>>();
    }
    
    public synchronized static  GestorServidorMensajeria getInstancia(){
        if(_instancia == null)
            _instancia = new GestorServidorMensajeria();
        return _instancia;
    }
    
    public synchronized void enviarMsjsPendientes(String nombre){ //update
        StringWriter sw = new StringWriter();
        ArrayList<String> arr;
        String idEmisor;
        Iterator it;
        String nReceptor;
        Persona p;
        try{
            while(this.mensajesNoEnviadosCAvisoOcup == true || this.avisosPendientesOcup == true || this.mensajesNoEnviadosOcup == true)
            {
                try{
                    wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.mensajesNoEnviadosCAvisoOcup = true;
            this.avisosPendientesOcup = true; 
            this.mensajesNoEnviadosOcup = true;
            this.informarConsola(nombre+" solicita recibir sus mensajes pendientes");
            if(GestorServidorMensajeria.getInstancia().getMensajesNoEnviados().containsKey(nombre)){
                this.informarConsola(nombre+" tiene mensajes pendientes de tipo 0 y/o 1... enviando");
                it = GestorServidorMensajeria.getInstancia().getMensajesNoEnviados().get(nombre).iterator();
                while(it.hasNext()){
                    String msj = (String) it.next();
                    sw.write("TRUE");
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    sw.write(msj);
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    it.remove();
                    this.informarConsola("Mensaje pendiente enviado a "+nombre);
                }
            }
            if(GestorServidorMensajeria.getInstancia().getMensajesNoEnviadosCAviso().containsKey(nombre)){
                this.informarConsola(nombre+" tiene mensajes pendientes de tipo 2... enviando");
                it = GestorServidorMensajeria.getInstancia().getMensajesNoEnviadosCAviso().get(nombre).iterator();
                sw.getBuffer().setLength(0);
                while(it.hasNext()){
                    String msj = (String) it.next();
                    sw.write("TRUE");
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    sw.write(msj);
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    it.remove();
                    nReceptor = ComunicacionMensajeria.getInstancia().recibirMsj();
                    p = this.obtenerEmisor(msj);
                    if(p!=null){
                        try{
                            ComunicacionMensajeria.getInstancia().enviarConfirmacion(InetAddress.getByName(p.getIP()), Integer.parseInt(p.getPuerto()),nReceptor);
                        }
                        catch(IOException e)
                        {
                            idEmisor = this.obtenerIDEmisorDeMsj(msj);
                            if(this.getAvisosPendientes().containsKey(idEmisor)){ //ya existe arrayList
                                arr = this.getAvisosPendientes().get(idEmisor);
                                arr.add(nReceptor);
                            }
                            else{
                                arr = new ArrayList<String>();
                                arr.add(nReceptor);
                                this.getAvisosPendientes().put(idEmisor, arr);
                            }
                        }
                    }
                    p=null;
                    this.informarConsola("Mensaje pendiente enviado a "+nombre);
                }
            }
            sw.write("FALSE");
            ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
            sw.getBuffer().setLength(0);
            this.informarConsola("Fin de envio de mensajes pendientes a "+nombre);
            this.mensajesNoEnviadosCAvisoOcup = false;
            this.avisosPendientesOcup = false; 
            this.mensajesNoEnviadosOcup = false;
            notifyAll();
        }
        catch(Exception e){
            this.informarConsola("ERROR en envio de mensajes pendientes a "+nombre);
            e.printStackTrace();
        }
    }
    
    public void enviarAvisosPendientes(String idEmisor) {
        Iterator it;
        StringWriter sw = new StringWriter();
        while(this.avisosPendientesOcup==true)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        this.avisosPendientesOcup=true;
        try{
            this.informarConsola(idEmisor+" solicita recibir sus avisos de recepcion pendientes");
            if(GestorServidorMensajeria.getInstancia().getAvisosPendientes().containsKey(idEmisor)){
                it = GestorServidorMensajeria.getInstancia().getAvisosPendientes().get(idEmisor).iterator();
                while(it.hasNext()){
                    String msj = (String) it.next();
                    sw.write("TRUE");
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    sw.write(msj);
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    it.remove();
                    this.informarConsola("Se ha enviado un aviso de recepcion a "+idEmisor);
                }
            }    
            sw.write("FALSE");
            ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
            sw.getBuffer().setLength(0);
            this.informarConsola("Fin de envio de avisos de recepcion pendientes a "+idEmisor);
        }
        catch(Exception e){
            this.informarConsola("ERROR en envio de avisos de recepcion pendientes a "+idEmisor);
            e.printStackTrace();
        }
        this.avisosPendientesOcup=false;
        notifyAll();
    }
    
    public synchronized void intentarEnviarMensaje() {
        MensajeEmisor mensajeEm = null;
        String msj = null;
        String direcReceptor;
        try {
            msj = ComunicacionMensajeria.getInstancia().recibirMsj();
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(msj);
            mensajeEm = (MensajeEmisor) unmarshaller.unmarshal(reader);
            try{
                direcReceptor = ComunicacionMensajeria.getInstancia().pedirIDADirectorio(mensajeEm.getReceptor().getNombre(),InetAddress.getByName(IPDirectorio),Integer.parseInt(puertoDirectorio));
            }catch(IOException e){
                direcReceptor = ComunicacionMensajeria.getInstancia().pedirIDADirectorio(mensajeEm.getReceptor().getNombre(),InetAddress.getByName(this.IPDirRedundante),Integer.parseInt(this.PuertoDirRedundante));
            }
            this.informarConsola("Intentado enviar mensaje a "+ mensajeEm.getReceptor().getNombre());
            StringWriter sw = new StringWriter();
            sw.write(msj);
            ComunicacionMensajeria.getInstancia().enviarMensaje(sw, InetAddress.getByName(this.getIPbyID(direcReceptor)), Integer.parseInt(this.getPuertoByID(direcReceptor)),mensajeEm.getTipo(),InetAddress.getByName(mensajeEm.getEmisor().getIP()),Integer.parseInt(mensajeEm.getEmisor().getPuerto()));
            this.informarConsola("Mensaje enviado con exito a "+ mensajeEm.getReceptor().getNombre());
        } catch (IOException e) {
            ArrayList<String> arr;
            int tipo = mensajeEm.getTipo();
            while(this.mensajesNoEnviadosCAvisoOcup == true || this.mensajesNoEnviadosOcup == true)
            {
                try
                {
                    wait();
                } catch (InterruptedException f)
                {
                    e.printStackTrace();
                }
            }
            this.mensajesNoEnviadosCAvisoOcup = true;
            this.mensajesNoEnviadosOcup = true;
            if(this.getMensajesNoEnviados().containsKey(mensajeEm.getReceptor().getNombre()) && tipo!=2){ //ya existe arrayList
                arr = this.getMensajesNoEnviados().get(mensajeEm.getReceptor().getNombre());
                arr.add(msj);
            }
            else{
                if(tipo != 2){
                    arr = new ArrayList<String>();
                    arr.add(msj);
                    this.getMensajesNoEnviados().put(mensajeEm.getReceptor().getNombre(), arr);
                }
            }
            if(this.getMensajesNoEnviadosCAviso().containsKey(mensajeEm.getReceptor().getNombre()) && tipo==2){
                arr = this.getMensajesNoEnviadosCAviso().get(mensajeEm.getReceptor().getNombre());
                arr.add(msj);
            }
            else{
                if(tipo == 2){
                    arr = new ArrayList<String>();
                    arr.add(msj);
                    this.getMensajesNoEnviadosCAviso().put(mensajeEm.getReceptor().getNombre(), arr);
                }
            }
            this.informarConsola("No se ha podido enviar el mensaje a "+mensajeEm.getReceptor().getNombre()+", guardandolo como pendiente (tipo="+tipo+")");
            this.mensajesNoEnviadosCAvisoOcup = false;
            this.mensajesNoEnviadosOcup = false;
            notifyAll();
        } catch (Exception e) {
            this.informarConsola("ERROR al intentar enviar el mensaje");
            e.printStackTrace();
        }
    }
    
    public String obtenerIDReceptor(){
        try {
            return ComunicacionMensajeria.getInstancia().recibirMsj();
        } catch (Exception e) {
            this.informarConsola("ERROR al obtener id del receptor");
            e.printStackTrace();
            return null;
        }
    }
    
    
    public String obtenerIDEmisor(){
        try {
            return ComunicacionMensajeria.getInstancia().recibirMsj();
        } catch (Exception e) {
            this.informarConsola("ERROR al obtener id del emisor");
            return null;
        }
    }
    
    public String obtenerIDEmisorDeMsj(String msj){
        MensajeEmisor mensajeEm = null;
        try {
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(msj);
            mensajeEm = (MensajeEmisor) unmarshaller.unmarshal(reader);
            return mensajeEm.getEmisor().getIP()+":"+mensajeEm.getEmisor().getPuerto();
        } catch (Exception e) {
            this.informarConsola("ERROR al obtener id del emisor desde un mensaje");
            return null;
        }
    }
    
    public Persona obtenerEmisor(String s)
    {
        MensajeEmisor mensajeEm = null;
        try {
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(s);
            mensajeEm = (MensajeEmisor) unmarshaller.unmarshal(reader);
            return mensajeEm.getEmisor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void informarConsola(String log){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println("["+dtf.format(now)+"]    "+log);      
    }
    
    public synchronized void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("MSJ_NUEVOMSJ")){
            this.intentarEnviarMensaje();
        }
        else if(comando.equalsIgnoreCase("MSJ_PEDIDOMSJREC")){
            this.enviarMsjsPendientes(obtenerIDReceptor());
        }
        else if(comando.equalsIgnoreCase("MSJ_PEDIDOAVISOSEM")){
            this.enviarAvisosPendientes(obtenerIDEmisor());
        }
    }

    public HashMap<String, ArrayList<String>> getMensajesNoEnviados() {
        return mensajesNoEnviados;
    }
    
    public HashMap<String, ArrayList<String>> getMensajesNoEnviadosCAviso() {
        return mensajesNoEnviadosCAviso;
    }

    public HashMap<String, ArrayList<String>> getAvisosPendientes() {
        return avisosPendientes;
    }

    public boolean isMensajesNoEnviadosOcup()
    {
        return mensajesNoEnviadosOcup;
    }

    public boolean isMensajesNoEnviadosCAvisoOcup()
    {
        return mensajesNoEnviadosCAvisoOcup;
    }

    public void setMensajesNoEnviadosOcup(boolean mensajesNoEnviadosOcup)
    {
        this.mensajesNoEnviadosOcup = mensajesNoEnviadosOcup;
    }

    public void setMensajesNoEnviadosCAvisoOcup(boolean mensajesNoEnviadosCAvisoOcup)
    {
        this.mensajesNoEnviadosCAvisoOcup = mensajesNoEnviadosCAvisoOcup;
    }

    public boolean isAvisosPendientesOcup()
    {
        return avisosPendientesOcup;
    }

    public void setAvisosPendientesOcup(boolean avisosPendientesOcup)
    {
        this.avisosPendientesOcup = avisosPendientesOcup;
    }

    public synchronized void backUp()
    {
        Thread tr = new Thread()
        {
            public synchronized void run()
            {
                try{
                    while(true)
                    {
                        Thread.sleep(3000);
                        while(GestorServidorMensajeria.getInstancia().isMensajesNoEnviadosCAvisoOcup()== true ||
                               GestorServidorMensajeria.getInstancia().isMensajesNoEnviadosOcup() ||
                               GestorServidorMensajeria.getInstancia().isAvisosPendientesOcup())
                        {
                            wait();
                        }
                        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(true);
                        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosOcup(true);
                        GestorServidorMensajeria.getInstancia().setAvisosPendientesOcup(true);
                        PersistenciaXML.getInstancia().persistir(GestorServidorMensajeria.getInstancia().getMensajesNoEnviados(),
                                       GestorServidorMensajeria.fileNoEnviados);
                        PersistenciaXML.getInstancia().persistir(GestorServidorMensajeria.getInstancia().getMensajesNoEnviadosCAviso(),
                                       GestorServidorMensajeria.fileNoEnviadosCAviso);
                        PersistenciaXML.getInstancia().persistir(GestorServidorMensajeria.getInstancia().getAvisosPendientes(),
                                       GestorServidorMensajeria.fileAvisosPendientes);
                        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(false);
                        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosOcup(false);
                        GestorServidorMensajeria.getInstancia().setAvisosPendientesOcup(false);
                        notifyAll();
                    }
                }
                catch (Exception e)
                {
                    GestorServidorMensajeria.getInstancia().informarConsola("ERROR al hacer back-up de Mensajeria");
                    e.printStackTrace();
                    
                }
            }
        };
        tr.start();
    }
    
    public synchronized void recuperarDatos()
    {
        HashMap<String, ArrayList<String>> hsAux;
        
        while(GestorServidorMensajeria.getInstancia().isMensajesNoEnviadosCAvisoOcup() ||
               GestorServidorMensajeria.getInstancia().isMensajesNoEnviadosOcup() ||
               GestorServidorMensajeria.getInstancia().isAvisosPendientesOcup())
                {
            try
            {
                wait();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(true);
        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosOcup(true);
        GestorServidorMensajeria.getInstancia().setAvisosPendientesOcup(true);
        hsAux = (HashMap<String, ArrayList<String>>)PersistenciaXML.getInstancia().recuperar(GestorServidorMensajeria.fileNoEnviados);
        if(hsAux!=null)
            this.mensajesNoEnviados = hsAux;
        hsAux = (HashMap<String, ArrayList<String>>)PersistenciaXML.getInstancia().recuperar(GestorServidorMensajeria.fileNoEnviadosCAviso);
        if(hsAux!=null)
            this.mensajesNoEnviadosCAviso = hsAux;
        hsAux = (HashMap<String, ArrayList<String>>)PersistenciaXML.getInstancia().recuperar(GestorServidorMensajeria.fileAvisosPendientes);
        if(hsAux!=null)
            this.avisosPendientes = hsAux;
        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(false);
        GestorServidorMensajeria.getInstancia().setMensajesNoEnviadosOcup(false);
        GestorServidorMensajeria.getInstancia().setAvisosPendientesOcup(false);
        notifyAll();
    }
    
    private String getIPbyID(String idRecepetor) {
        return idRecepetor.split(":")[0];
    }

    private String getPuertoByID(String idRecepetor) {
        return idRecepetor.split(":")[1];
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
                this.IPDirectorio=datos[0];
                this.puertoDirectorio=datos[1];
                this.IPDirRedundante=datos[2];
                this.PuertoDirRedundante=datos[3];
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
