package negocio;

import base.ComunicacionEmisor;

import presentación.IVentanaEmisor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import java.io.UnsupportedEncodingException;

import java.util.Iterator;
import java.util.List;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.HashMap;

import javax.swing.JOptionPane;

public class Emisor extends Persona implements ActionListener{
    
    private final int cantCarAsunto=128,cantCarMensaje=2048;
    
    private IVentanaEmisor vista;
    private String IPDirectorio, puertoDirectorio;
    private boolean RCocupado=false;
    private static Emisor instancia = null;
    private final String regex=", *";
    private final String nombreConfigDirectorio="config.txt";
    private final String decoder="UTF8";
    private final int cantDatos=2;
    private Socket socketDirectorio;
    private HashMap<String, Persona> listaActualReceptores;
    
    private Emisor() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static Emisor getInstancia(){
        if(instancia==null){
            instancia = new Emisor();
        }
        return instancia;
    }

    public void setSocketDirectorio(Socket socketDirectorio) {
        this.socketDirectorio = socketDirectorio;
    }

    public Socket getSocketDirectorio() {
        return socketDirectorio;
    }

    public void setListaActualReceptores(HashMap<String, Persona> listaActualReceptores) {
        this.listaActualReceptores = listaActualReceptores;
    }

    public HashMap<String, Persona> getListaActualReceptores() {
        return listaActualReceptores;
    }

    public synchronized void enviarMensaje(List<Persona> listaPersonas){
        String asunto,texto;
        int tipo;
        List<Persona> personas;
        Mensaje mensaje;
        Persona personaAux;
        
        personas = listaPersonas;
        Iterator<Persona> it = personas.iterator();
        asunto=vista.getAsunto();
        texto=vista.getMensaje();
        tipo=vista.getTipo();
        mensaje = new Mensaje(asunto,texto,this,tipo);
        if(tipo == 2){
            ComunicacionEmisor.getInstancia().escucharPuerto(this.getPuerto());
        }
        try{
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(Mensaje.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(mensaje, sw);
            while(it.hasNext()){
                personaAux=it.next();
                try{
                    ComunicacionEmisor.getInstancia().enviarMensaje(sw, InetAddress.getByName(personaAux.getIP()), Integer.parseInt(personaAux.getPuerto()));
                } catch (UnknownHostException e) {
                    this.lanzarCartelError("No se pudo conectar con "+personaAux.getNombre()+" "+personaAux.getApellido());
                } catch (Exception e){
                    this.lanzarCartelError("El destinatario "+personaAux.getNombre()+" "+personaAux.getApellido()+" no puede recibir el mensaje");
                    if(tipo == 2){
                        ComunicacionEmisor.getInstancia().escucharPuerto(this.getPuerto());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void recibirConfirmacion(String confirmacion){
        while(RCocupado==true){
            try{
                wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RCocupado=true;
        Persona per = Agenda.getInstance().getPersona(confirmacion);
        String nombre;
        if(per==null){
            nombre = confirmacion;
        }
        else{
            nombre = per.toString();
        }
        this.vista.lanzarCartelError(nombre + " ha recibido correctamente el mensaje.");
        RCocupado=false;
        notifyAll();
    }
    
    public void configAtributos(String ip, String puerto, String nombre, String apellido) {
        this.setIP(ip);
        this.setPuerto(puerto);
        this.setNombre(nombre);
        this.setApellido(apellido);
    }
    
    public void lanzarCartelError(String err) {
        this.vista.lanzarCartelError(err);
    }
    
    public void setVista(IVentanaEmisor vista) {
        this.vista = vista;
        KeyListener kl1 = new KeyListener(){
        
            public void keyTyped(KeyEvent e){
                if (vista.getAsunto().length()== cantCarAsunto){
                    Emisor.getInstancia().getVista().lanzarCartelError("No puede ingresar mas de "+cantCarAsunto+" caracteres en el asunto.");
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
                    Emisor.getInstancia().getVista().lanzarCartelError("No puede ingresar mas de "+cantCarMensaje+" caracteres en el mensaje.");
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
        vista.actualizarListaAgenda(Agenda.getInstance().getPersonas());
    }

    public IVentanaEmisor getVista() {
        return vista;
    }
    
    public void cargarDatosDirectorio(){
        BufferedReader br;
        String[] datos;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(nombreConfigDirectorio), decoder));
            String linea = br.readLine();
            while(linea!=null){
                datos=linea.split(regex);
                this.IPDirectorio=datos[0];
                this.puertoDirectorio=datos[1];
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
    
    public void abrirConexionDirectorio(){
        try {
            this.setSocketDirectorio(ComunicacionEmisor.getInstancia()
                                     .abrirConexionDirectorio(InetAddress.getByName(IPDirectorio),
                                                              Integer.valueOf(puertoDirectorio)));
        } catch (UnknownHostException e) {
            this.lanzarCartelError("ERROR al conectar con el directorio");
        }
    }
    
    public void obtenerListaReceptores(){
        this.abrirConexionDirectorio();
        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent arg) {
        String comando = arg.getActionCommand();
        if(comando.equalsIgnoreCase("ENVIAR MENSAJE")){
            this.vista.enviarMensaje();
        }else
        if(comando.equalsIgnoreCase("CONFIGURACION")){
            this.vista.abrirConfig();
        }else
        if(comando.equalsIgnoreCase("ACEPTAR CAMBIO")){
            this.vista.confirmarConfiguracion();
        }else
        if(comando.equalsIgnoreCase("CANCELAR CAMBIO")){
            this.vista.cerrarConfig();
        }
    }

    public static void setInstancia(Emisor instancia) {
        Emisor.instancia = instancia;
    }
}
