package client;

import interfaz.IVistaEmisor;

public class Emisor {
    
    private IVistaEmisor vista;
    private Agenda agenda;
    
    public Emisor(IVistaEmisor vista,Agenda agenda) {
        
        super();
        this.agenda=agenda;
        this.vista = vista;
        vista.actualizarListaAgenda(agenda.getPersonas());
        
        
    }
    
    public void enviarMensaje(){
        
    }
    
    public void recibirConfirmacion(){
        
    }
    
    public void actualizarListaAgenda(){
        
    }
}
