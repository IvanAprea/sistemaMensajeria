package client;

import interfaz.IVistaEmisor;

public class Emisor {
    
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
        
    }
    
    public void recibirConfirmacion(){
        
    }
    
    public void actualizarListaAgenda(){
        
    }

    public void setVista(IVistaEmisor vista) {
        this.vista = vista;
        vista.actualizarListaAgenda(Agenda.getInstance().getPersonas());
    }

    public IVistaEmisor getVista() {
        return vista;
    }

    public static void setInstancia(Emisor instancia) {
        Emisor.instancia = instancia;
    }

}
