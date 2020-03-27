package client;

import interfaz.IVistaEmisor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;
import java.util.List;

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
        
        personas = vista.getPersonas();
        Iterator<Persona> it = personas.iterator();
        asunto=vista.getAsunto();
        texto=vista.getMensaje();
        tipo=vista.getTipo();
        
        while(it.hasNext()){
            mensaje = new Mensaje(asunto,texto,it.next(),tipo);
            Comunicacion.getInstancia().enviarMensaje(mensaje);
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
            //this.enviarMensaje();
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
