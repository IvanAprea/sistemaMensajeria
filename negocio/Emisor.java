package negocio;

import base.ComunicacionEmisor;

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

import java.util.HashMap;

import javax.swing.JOptionPane;

import presentacion.VentanaEmisor;

public class Emisor extends Persona implements ActionListener{
    
    private final int cantCarAsunto=128,cantCarMensaje=2048;
    
    private IVentanaEmisor vista;
    private String IPDirectorio, puertoDirectorio;
    private static Emisor instancia = null;
    private final String regex=", *";
    private final String nombreConfigDirectorio="config.txt";
    private final String decoder="UTF8";
    private final int cantDatos=2;
    private Socket socketDirectorio;
    private UsuariosRecMap listaActualReceptores;
    
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
        Mensaje mensaje;
        UsuarioReceptor personaAux;
        
        personas = listaPersonas;
        Iterator<UsuarioReceptor> it = personas.iterator();
        asunto=vista.getAsunto();
        texto=vista.getMensaje();
        tipo=vista.getTipo();
        mensaje = new Mensaje(asunto,texto,this,tipo);
        try{
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(Mensaje.class);
            javax.xml.bind.Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(mensaje, sw);
            while(it.hasNext()){
                personaAux=it.next();
                try{
                    ComunicacionEmisor.getInstancia().enviarMensaje(sw, InetAddress.getByName(personaAux.getIP()), Integer.parseInt(personaAux.getPuerto()),mensaje.getTipo());
                } catch (UnknownHostException e) {
                    this.lanzarCartelError("No se pudo conectar con "+personaAux.getNombre());
                } catch (Exception e){
                    this.lanzarCartelError("El destinatario "+personaAux.getNombre()+" no puede recibir el mensaje");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void recibirConfirmacion(String receptor){
        this.vista.lanzarCartelError(receptor + " ha recibido correctamente el mensaje.");
    }
    
    public void configAtributos(String nombre) {
        this.setNombre(nombre);
    }
    
    public void lanzarCartelError(String err) {
        this.vista.lanzarCartelError(err);
    }
    
    public void setVista(VentanaEmisor vista) {
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
    
    public void abrirConexionDirectorio() throws UnknownHostException {
            this.setSocketDirectorio(ComunicacionEmisor.getInstancia().abrirConexionDirectorio(InetAddress.getByName(IPDirectorio),Integer.valueOf(puertoDirectorio)));

    }
    
    public void obtenerListaReceptores(){
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
        catch (UnknownHostException e) {
            this.lanzarCartelError("ERROR al conectar con el directorio");
        } 
        catch (IOException e) {
            this.lanzarCartelError("ERROR al cerrar la conexion con el directorio");
        }
        catch(Exception e){
            e.printStackTrace();
        }
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
            this.obtenerListaReceptores();
            this.vista.actualizarListaDirectorio(this.getListaActualReceptores().getUsuariosRecMap());
        }
        else if(comando.equalsIgnoreCase("CONFIRMAR DESTINATARIOS")){
            this.vista.confirmarDestinatarios();
        }
        else if(comando.equalsIgnoreCase("CANCELAR DESTINATARIOS")){
            this.vista.cancelarDestinatarios();
        }        
    }

    public static void setInstancia(Emisor instancia) {
        Emisor.instancia = instancia;
    }
}
