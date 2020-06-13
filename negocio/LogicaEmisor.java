package negocio;

import base.ComunicacionEmisor;

import base.ComunicacionReceptor;


import base.PersistenciaMensajeria;
import base.PersistenciaXML;

import interfaces.ICargaConfig;
import interfaces.IConfirmacionEmisor;
import interfaces.IEncriptar;
import interfaces.IEnviarMensajeEm;

import java.util.Map;

import presentacion.IVentanaEmisor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import java.io.UnsupportedEncodingException;

import java.util.Iterator;
import java.util.List;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import presentacion.VentanaEmisor2;

public class LogicaEmisor extends Persona implements ActionListener,IEnviarMensajeEm,ICargaConfig,IConfirmacionEmisor{
    
    private final int cantCarAsunto=128,cantCarMensaje=2048;
    private IVentanaEmisor vista;
    private String IPDirectorio, puertoDirectorio,IPMensajeria,puertoMensajeria;
    private static LogicaEmisor instancia = null;
    private final String regex=", *";
    private final String nombreConfigDirectorio="config.txt";
    private final String decoder="UTF8";
    private final int cantDatos=2;
    private Socket socketDirectorio;
    private UsuariosRecMap listaActualReceptores;
    private IEncriptar encriptador;
    private HashMap<UsuarioReceptor,ArrayList<String>> noEnviados = new HashMap<UsuarioReceptor,ArrayList<String>>();
    private boolean noEnviadosOcupado=false,enviandoNoEnviados = false;
    
    private LogicaEmisor() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static LogicaEmisor getInstancia(){
        if(instancia==null){
            instancia = new LogicaEmisor();
        }
        return instancia;
    }

    public void setSocketDirectorio(Socket socketDirectorio) {
        this.socketDirectorio = socketDirectorio;
    }

    public Socket getSocketDirectorio() {
        return socketDirectorio;
    }

    public void setListaActualReceptores(UsuariosRecMap listaActualReceptores) {
        this.listaActualReceptores = listaActualReceptores;
    }

    public UsuariosRecMap getListaActualReceptores() {
        return listaActualReceptores;
    }
  
    public synchronized void enviarMensaje(List<UsuarioReceptor> listaPersonas){
        String asunto,texto;
        int tipo;
        List<UsuarioReceptor> personas;
        MensajeEmisor mensaje;
        UsuarioReceptor personaAux;
        
        personas = listaPersonas;
        Iterator<UsuarioReceptor> it = personas.iterator();
        asunto=vista.getAsunto();
        texto=vista.getMensaje();
        tipo=vista.getTipo();
        mensaje = new MensajeEmisor(null,null,this,tipo,null);
        mensaje.setearFecha();
        while(it.hasNext()){
            personaAux=it.next();
            mensaje.setReceptor(personaAux);
            try{
                mensaje.setAsunto(this.encriptador.encriptar(personaAux.getPublicKey(), asunto.getBytes()));
                mensaje.setTexto(this.encriptador.encriptar(personaAux.getPublicKey(), texto.getBytes()));
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
                StringWriter sw = new StringWriter();
                marshaller.marshal(mensaje, sw);
                try{
                    ComunicacionEmisor.getInstancia().enviarMensaje(sw.toString(), InetAddress.getByName(this.IPMensajeria), Integer.parseInt(this.puertoMensajeria));
                }catch (UnknownHostException e) {
                    this.lanzarCartelError("No se pudo conectar con "+personaAux.getNombre());
                } catch (Exception e){
                    ArrayList<String> arr;
                    while(this.isNoEnviadosOcupado())
                    {
                        wait();
                    }
                    this.setNoEnviadosOcupado(true);
                    if(this.getNoEnviados().containsKey(personaAux))
                    {
                        arr = this.getNoEnviados().get(personaAux);
                        arr.add(sw.toString());
                    } else
                    {
                        arr = new ArrayList<String>();
                        arr.add(sw.toString());
                        this.getNoEnviados().put(personaAux, arr);
                    }
                    this.setNoEnviadosOcupado(false);
                    notifyAll();
                    if(isEnviandoNoEnviados() == false)
                    {
                        enviarMensajesPendientes();
                        persistirNoEnviados();
                        setEnviandoNoEnviados(true);
                    }
                    this.lanzarCartelError(personaAux.getNombre()+" no puede recibir el mensaje en este momento, se enviara luego.");
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public synchronized void enviarMensajesPendientes()
    {
        Thread tr = new Thread() {
            public synchronized void run() {
                while(true)
                {
                    try
                    {
                        Thread.sleep(3000);
                        while(isNoEnviadosOcupado())
                        {
                            wait();
                        }
                        setNoEnviadosOcupado(true);
                        if(!noEnviados.isEmpty())
                        {
                            ArrayList<String> arr = null;
                            Map.Entry me;
                            UsuarioReceptor user;
                            Iterator it = noEnviados.entrySet().iterator();
                            while(it.hasNext())
                            {
                                me = (Map.Entry) it.next();
                                user = (UsuarioReceptor) me.getKey();
                                arr = (ArrayList<String>) me.getValue();
                                try
                                {
                                    ComunicacionEmisor.getInstancia().enviarMensajes(arr, InetAddress.getByName(IPMensajeria),Integer.parseInt(puertoMensajeria));
                                } catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                if(arr.isEmpty())
                                {
                                    it.remove();
                                }
                            }
                        }
                        setNoEnviadosOcupado(false);
                        notifyAll();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    
                }
            }
        };
        tr.start();
    }
    
    public synchronized void persistirNoEnviados()
    {
        Thread tr = new Thread() {
            public void run() {
                while(true)
                {
                    try
                    {
                        Thread.sleep(3000);
                        while(isNoEnviadosOcupado())
                        {
                            wait();
                        }
                        setNoEnviadosOcupado(true);
                        if(!noEnviados.isEmpty())
                        {
                            PersistenciaXML.getInstancia().backUp(LogicaEmisor.getInstancia().getNoEnviados(), "noEnviadosEmisor.txt");
                        }
                        setNoEnviadosOcupado(false);
                        notifyAll();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    
                }
            }
        };
        tr.start();
    }
    
    public synchronized void recuperarNoEnviados()
    {
        try
        {
            while(isNoEnviadosOcupado())
            {
                wait();
            }
            setNoEnviadosOcupado(true);
            this.noEnviados = (HashMap<UsuarioReceptor,ArrayList<String>>)PersistenciaXML.getInstancia().recuperarDatos("noEnviadosEmisor.txt");
            setNoEnviadosOcupado(false);
            if(!this.noEnviados.isEmpty())
            {
                enviarMensajesPendientes();
                persistirNoEnviados();
                setEnviandoNoEnviados(true);
            }
            notifyAll();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
    public synchronized void recibirConfirmacion(String receptor, String fecha){
        this.vista.lanzarCartelError(receptor + " ha recibido correctamente el mensaje enviado el "+fecha+".");
    }
    
    public void configAtributos(String nombre) {
        this.setNombre(nombre);
    }
    
    public void lanzarCartelError(String err) {
        this.vista.lanzarCartelError(err);
    }
    
    public void escucharPuerto()
    {
        ComunicacionEmisor.getInstancia().escucharPuerto(super.getPuerto());
    }
    
    public void setVista(VentanaEmisor2 vista) {
        this.vista = vista;
        KeyListener kl1 = new KeyListener(){
        
            public void keyTyped(KeyEvent e){
                if (vista.getAsunto().length()== cantCarAsunto){
                    LogicaEmisor.getInstancia().getVista().lanzarCartelError("No puede ingresar mas de "+cantCarAsunto+" caracteres en el asunto.");
                    e.consume();
                }
            }
            public void keyPressed(KeyEvent arg0) {
            }
            public void keyReleased(KeyEvent arg0) {
            }
        };
        KeyListener kl2 = new KeyListener(){
        
            public void keyTyped(KeyEvent e){
                if (vista.getAsunto().length()== cantCarMensaje){
                    LogicaEmisor.getInstancia().getVista().lanzarCartelError("No puede ingresar mas de "+cantCarMensaje+" caracteres en el mensaje.");
                    e.consume();
                }
            }
            public void keyPressed(KeyEvent arg0) {
            }
            public void keyReleased(KeyEvent arg0) {
            }
        };
        this.vista.addKeyListener(kl1,kl2);
        this.vista.addActionListener(this);
    }

    public IVentanaEmisor getVista() {
        return vista;
    }
    
    public void cargarDatosConfig(){
        BufferedReader br;
        String[] datos;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(nombreConfigDirectorio), decoder));
            String linea = br.readLine();
            while(linea!=null){
                datos=linea.split(regex);
                this.IPDirectorio=datos[0];
                this.puertoDirectorio=datos[1];
                this.IPMensajeria=datos[2];
                this.puertoMensajeria=datos[3];
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
    
    public void abrirConexionDirectorio() throws UnknownHostException,IOException {
            this.setSocketDirectorio(ComunicacionEmisor.getInstancia().abrirConexionDirectorio(InetAddress.getByName(IPDirectorio),Integer.valueOf(puertoDirectorio)));

    }
    
    public void obtenerListaReceptores() throws Exception {
        try 
        {
            this.abrirConexionDirectorio();
            String hm = ComunicacionEmisor.getInstancia().pedirListaADirectorio(this.getSocketDirectorio());
            this.getSocketDirectorio().close();
            if(hm!=null){
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(UsuariosRecMap.class);
                javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
                StringReader reader = new StringReader(hm);
                this.listaActualReceptores = (UsuariosRecMap)unmarshaller.unmarshal(reader);
            }
        } 
        catch (Exception e) {
            throw new Exception("ERROR en la conexion con el directorio");
        }
    }
    
    public void pedirAvisosPendientes() {
        ComunicacionEmisor.getInstancia().pedirAvisosPendientes(this.IPMensajeria,this.puertoMensajeria, this.getIP()+":"+this.getPuerto());
    }
    
    @Override
        public void actionPerformed(ActionEvent arg) {
            String comando = arg.getActionCommand();
            if(comando.equalsIgnoreCase("ENVIAR MENSAJE")){
                this.vista.enviarMensaje();
            }else
            if(comando.equalsIgnoreCase("ACEPTAR SESION")){
                this.vista.confirmarSesion();
            }
            else if(comando.equalsIgnoreCase("SELECCIONAR DESTINATARIOS")){
                try {
                    this.obtenerListaReceptores();
                    this.vista.actualizarListaDirectorio(this.getListaActualReceptores().getUsuariosRecMap());
                } catch (Exception e) {
                    this.lanzarCartelError(e.getMessage());
                }
            }
            else if(comando.equalsIgnoreCase("CONFIRMAR DESTINATARIOS")){
                this.vista.confirmarDestinatarios();
            }
            else if(comando.equalsIgnoreCase("CANCELAR DESTINATARIOS")){
                this.vista.cancelarDestinatarios();
            }
        }

    public static void setInstancia(LogicaEmisor instancia) {
        LogicaEmisor.instancia = instancia;
    }

    public void setEncriptador(IEncriptar encriptador)
    {
        this.encriptador = encriptador;
    }

    public void setNoEnviadosOcupado(boolean listaNoDispOcupado)
    {
        this.noEnviadosOcupado = listaNoDispOcupado;
    }

    public boolean isNoEnviadosOcupado()
    {
        return noEnviadosOcupado;
    }

    public HashMap<UsuarioReceptor, ArrayList<String>> getNoEnviados()
    {
        return noEnviados;
    }

    public void setEnviandoNoEnviados(boolean enviandoNoEnviados)
    {
        this.enviandoNoEnviados = enviandoNoEnviados;
    }

    public boolean isEnviandoNoEnviados()
    {
        return enviandoNoEnviados;
    }
}
