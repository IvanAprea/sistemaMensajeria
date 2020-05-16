package negocio;

import base.ComunicacionDirectorio;
import base.ComunicacionMensajeria;

import base.PersistenciaMensajeria;

import interfaces.IBackUp;

import java.io.IOException;
import java.io.StringReader;

import java.io.StringWriter;

import java.net.InetAddress;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import javax.xml.bind.JAXBException;
public class Mensajeria implements IBackUp{

    
    private static final String fileNoEnviados="noEnviados.txt",fileNoEnviadosCAviso="noEnviadosCAviso.txt",fileAvisosPendientes="avisosPendientes.txt";
    private static Mensajeria _instancia = null;
    private HashMap<String, ArrayList<String>> mensajesNoEnviados;
    private HashMap<String, ArrayList<String>> mensajesNoEnviadosCAviso;
    private HashMap<String, ArrayList<String>> avisosPendientes;
    private boolean mensajesNoEnviadosOcup=false,mensajesNoEnviadosCAvisoOcup=false,avisosPendientesOcup=false;
    
    private Mensajeria() {
        super();
        this.mensajesNoEnviados = new HashMap<String, ArrayList<String>>();
        this.mensajesNoEnviadosCAviso = new HashMap<String, ArrayList<String>>();
        this.avisosPendientes = new HashMap<String, ArrayList<String>>();
    }
    
    public synchronized static  Mensajeria getInstancia(){
        if(_instancia == null)
            _instancia = new Mensajeria();
        return _instancia;
    }
    
    public synchronized void enviarMsjsPendientes(String id){
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
            this.informarConsola(id+" solicita recibir sus mensajes pendientes");
            if(Mensajeria.getInstancia().getMensajesNoEnviados().containsKey(id)){
                this.informarConsola(id+" tiene mensajes pendientes de tipo 0 y/o 1... enviando");
                it = Mensajeria.getInstancia().getMensajesNoEnviados().get(id).iterator();
                while(it.hasNext()){
                    String msj = (String) it.next();
                    sw.write("TRUE");
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    sw.write(msj);
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    it.remove();
                    this.informarConsola("Mensaje pendiente enviado a "+id);
                }
            }
            if(Mensajeria.getInstancia().getMensajesNoEnviadosCAviso().containsKey(id)){
                this.informarConsola(id+" tiene mensajes pendientes de tipo 2... enviando");
                it = Mensajeria.getInstancia().getMensajesNoEnviadosCAviso().get(id).iterator();
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
                    this.informarConsola("Mensaje pendiente enviado a "+id);
                }
            }
            sw.write("FALSE");
            ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
            sw.getBuffer().setLength(0);
            this.informarConsola("Fin de envio de mensajes pendientes a "+id);
            this.mensajesNoEnviadosCAvisoOcup = false;
            this.avisosPendientesOcup = false; 
            this.mensajesNoEnviadosOcup = false;
            notifyAll();
        }
        catch(Exception e){
            this.informarConsola("ERROR en envio de mensajes pendientes a "+id);
            e.printStackTrace();
        }
    }
    
    private void enviarAvisosPendientes(String idEmisor) {
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
            if(Mensajeria.getInstancia().getAvisosPendientes().containsKey(idEmisor)){
                it = Mensajeria.getInstancia().getAvisosPendientes().get(idEmisor).iterator();
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
    
    private synchronized void intentarEnviarMensaje() {
        MensajeEmisor mensajeEm = null;
        String msj = null;
        try {
            msj = ComunicacionMensajeria.getInstancia().recibirMsj();
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(msj);
            mensajeEm = (MensajeEmisor) unmarshaller.unmarshal(reader);
            String id = mensajeEm.getReceptor().getIP()+":"+mensajeEm.getReceptor().getPuerto();
            this.informarConsola("Intentado enviar mensaje a "+id);
            StringWriter sw = new StringWriter();
            sw.write(msj);
            ComunicacionMensajeria.getInstancia().enviarMensaje(sw, InetAddress.getByName(mensajeEm.getReceptor().getIP()), Integer.parseInt(mensajeEm.getReceptor().getPuerto()),mensajeEm.getTipo(),InetAddress.getByName(mensajeEm.getEmisor().getIP()),Integer.parseInt(mensajeEm.getEmisor().getPuerto()));
            //avisar al emisor que se recibio: (Y SI NO ESTUVIERA CONECTADO?)
            this.informarConsola("Mensaje enviado con exito a "+id);
        } catch (IOException e) {
            String id = mensajeEm.getReceptor().getIP()+":"+mensajeEm.getReceptor().getPuerto();
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
            if(this.getMensajesNoEnviados().containsKey(id) && tipo!=2){ //ya existe arrayList
                arr = this.getMensajesNoEnviados().get(id);
                arr.add(msj);
            }
            else{
                if(tipo != 2){
                    arr = new ArrayList<String>();
                    arr.add(msj);
                    this.getMensajesNoEnviados().put(id, arr);
                }
            }
            if(this.getMensajesNoEnviadosCAviso().containsKey(id) && tipo==2){
                arr = this.getMensajesNoEnviadosCAviso().get(id);
                arr.add(msj);
            }
            else{
                if(tipo == 2){
                    arr = new ArrayList<String>();
                    arr.add(msj);
                    this.getMensajesNoEnviadosCAviso().put(id, arr);
                }
            }
            this.informarConsola("No se ha podido enviar el mensaje a "+id+", guardandolo como pendiente (tipo="+tipo+")");
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
                        Thread.sleep(15000);
                        while(Mensajeria.getInstancia().isMensajesNoEnviadosCAvisoOcup()== true ||
                        Mensajeria.getInstancia().isMensajesNoEnviadosOcup() ||
                        Mensajeria.getInstancia().isAvisosPendientesOcup())
                        {
                            wait();
                        }
                        Mensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(true);
                        Mensajeria.getInstancia().setMensajesNoEnviadosOcup(true);
                        Mensajeria.getInstancia().setAvisosPendientesOcup(true);
                        PersistenciaMensajeria.getInstancia().backUp(Mensajeria.getInstancia().getMensajesNoEnviados(),Mensajeria.fileNoEnviados);
                        PersistenciaMensajeria.getInstancia().backUp(Mensajeria.getInstancia().getMensajesNoEnviadosCAviso(),Mensajeria.fileNoEnviadosCAviso);
                        PersistenciaMensajeria.getInstancia().backUp(Mensajeria.getInstancia().getAvisosPendientes(),Mensajeria.fileAvisosPendientes);
                        Mensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(false);
                        Mensajeria.getInstancia().setMensajesNoEnviadosOcup(false);
                        Mensajeria.getInstancia().setAvisosPendientesOcup(false);
                        notifyAll();
                    }
                }
                catch (Exception e)
                {
                    Mensajeria.getInstancia().informarConsola("ERROR al hacer back-up de Mensajeria");
                    e.printStackTrace();
                }
            }
        };
        tr.start();
    }
    
    public void recuperarDatos()
    {
        while(Mensajeria.getInstancia().isMensajesNoEnviadosCAvisoOcup() ||
                Mensajeria.getInstancia().isMensajesNoEnviadosOcup() ||
                Mensajeria.getInstancia().isAvisosPendientesOcup())
                {
            try
            {
                wait();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        Mensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(true);
        Mensajeria.getInstancia().setMensajesNoEnviadosOcup(true);
        Mensajeria.getInstancia().setAvisosPendientesOcup(true);
        this.mensajesNoEnviados =  PersistenciaMensajeria.getInstancia().recuperarDatos(Mensajeria.fileNoEnviados);
        this.mensajesNoEnviadosCAviso = PersistenciaMensajeria.getInstancia().recuperarDatos(Mensajeria.fileNoEnviadosCAviso);
        this.avisosPendientes = PersistenciaMensajeria.getInstancia().recuperarDatos(Mensajeria.fileAvisosPendientes);
        Mensajeria.getInstancia().setMensajesNoEnviadosCAvisoOcup(false);
        Mensajeria.getInstancia().setMensajesNoEnviadosOcup(false);
        Mensajeria.getInstancia().setAvisosPendientesOcup(false);
        notifyAll();
    }
}
