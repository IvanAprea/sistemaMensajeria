package negocio;

import base.ComunicacionEmisor;

import exceptions.excepcionEnviarMensaje;

import interfaces.ICargaConfig;
import interfaces.IEnviarMensaje;

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

public class NegocioEmisor extends Persona implements ActionListener,IEnviarMensaje,ICargaConfig{
    
    private final int cantCarAsunto=128,cantCarMensaje=2048;
    private final String puerto="79";
    private IVentanaEmisor vista;
    private String IPDirectorio, puertoDirectorio,IPMensajeria,puertoMensajeria;
    private static NegocioEmisor instancia = null;
    private final String regex=", *";
    private final String nombreConfigDirectorio="config.txt";
    private final String decoder="UTF8";
    private final int cantDatos=2;
    private Socket socketDirectorio;
    private UsuariosRecMap listaActualReceptores;
    
    private NegocioEmisor() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static NegocioEmisor getInstancia(){
        if(instancia==null){
            instancia = new NegocioEmisor();
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
        mensaje = new MensajeEmisor(asunto,texto,this,tipo,null);
        try{
            while(it.hasNext()){
                personaAux=it.next();
                mensaje.setReceptor(personaAux);
                javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
                javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
                StringWriter sw = new StringWriter();
                marshaller.marshal(mensaje, sw);
                try{
                    ComunicacionEmisor.getInstancia().enviarMensaje(sw, InetAddress.getByName(this.IPMensajeria), Integer.parseInt(this.puertoMensajeria),mensaje.getTipo());
                } catch(excepcionEnviarMensaje e){
                    this.confirmacionPendiente(personaAux.getNombre());
                }catch (UnknownHostException e) {
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
    
    public synchronized void confirmacionPendiente(String receptor){
        this.vista.lanzarCartelError(receptor + " no esta conectado, se avisara cuando reciba el mensaje.");
    }
    
    public void configAtributos(String nombre) {
        this.setNombre(nombre);
    }
    
    public void lanzarCartelError(String err) {
        this.vista.lanzarCartelError(err);
    }
    
    public void escucharPuerto()
    {
        ComunicacionEmisor.getInstancia().escucharPuerto(this.puerto);
    }
    
    public void setVista(VentanaEmisor vista) {
        this.vista = vista;
        KeyListener kl1 = new KeyListener(){
        
            public void keyTyped(KeyEvent e){
                if (vista.getAsunto().length()== cantCarAsunto){
                    NegocioEmisor.getInstancia().getVista().lanzarCartelError("No puede ingresar mas de "+cantCarAsunto+" caracteres en el asunto.");
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
                    NegocioEmisor.getInstancia().getVista().lanzarCartelError("No puede ingresar mas de "+cantCarMensaje+" caracteres en el mensaje.");
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
    
    public void abrirConexionDirectorio() throws UnknownHostException {
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
            catch (UnknownHostException e) {
                throw new Exception("ERROR al conectar con el directorio");
            } 
            catch (IOException e) {
                throw new Exception("ERROR al cerrar la conexion con el directorio");
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

    public static void setInstancia(NegocioEmisor instancia) {
        NegocioEmisor.instancia = instancia;
    }
}
