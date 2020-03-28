package client;

import interfaz.IVistaEmisor;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.StringWriter;

import java.util.Iterator;
import java.util.List;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Emisor extends Persona implements ActionListener{
    
    private IVistaEmisor vista;
    private static Emisor instancia = null;
    
    private Emisor() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    
    public synchronized static Emisor getInstance(){
        if(instancia==null){
            instancia = new Emisor();
        }
        return instancia;
    }
    
    public void enviarMensaje(){
        String asunto,texto;
        int tipo;
        List<Persona> personas;
        Mensaje mensaje;
        Persona receptorAux;
        
        personas = vista.getPersonas();
        Iterator<Persona> it = personas.iterator();
        asunto=vista.getAsunto();
        texto=vista.getMensaje();
        tipo=vista.getTipo();
        mensaje = new Mensaje(asunto,texto,it.next(),tipo);
        
        try{
            JAXBContext context = JAXBContext.newInstance(Mensaje.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(mensaje, sw);
            while(it.hasNext()){
                receptorAux = it.next();
                Comunicacion.getInstancia().enviarMensaje(sw,InetAddress.getByName(receptorAux.getIP()),Integer.parseInt(receptorAux.getPuerto()));
            }
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    public void recibirConfirmacion(){
        
    }
    
    public void editarDatosPersona(){
        this.setApellido(vista.getApellidoConfig());
        this.setNombre(vista.getNombreConfig());
        this.vista.cerrarConfig();
    }


    public void setVista(IVistaEmisor vista) {
        this.vista = vista;
        this.vista.addActionListener(this);
        vista.actualizarListaAgenda(Agenda.getInstance().getPersonas());
    }

    public IVistaEmisor getVista() {
        return vista;
    }


    @Override
    public void actionPerformed(ActionEvent arg) {
        String comando = arg.getActionCommand();
        if(comando.equalsIgnoreCase("ENVIAR MENSAJE")){
            this.enviarMensaje();
            this.vista.mostrarPanelMsjRecibido();
        }else
        if(comando.equalsIgnoreCase("CONFIGURACION")){
            this.vista.abrirConfig();
        }else
        if(comando.equalsIgnoreCase("ACEPTAR CAMBIO")){
            this.editarDatosPersona();
        }else
        if(comando.equalsIgnoreCase("CANCELAR CAMBIO")){
            this.vista.cerrarConfig();
        }
    }
}
