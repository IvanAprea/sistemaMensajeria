package negocio;

import base.ComunicacionDirectorio;
import base.ComunicacionMensajeria;

public class Mensajeria {
    
    private static Mensajeria _instancia = null;
    
    private Mensajeria() {
        super();
    }
    
    public synchronized static  Mensajeria getInstancia(){
        if(_instancia == null)
            _instancia = new Mensajeria();
        return _instancia;
    }
    
    public synchronized void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("MSJ_RECCONECTADO")){
            
        }
        else if(comando.equalsIgnoreCase("MSJ_NUEVOMSJ")){
            this.intentarEnviarMensaje();
        }
    }

    private void intentarEnviarMensaje() {       
        try {
            //recuperar msj
            Mensaje msj = this.prepararMsj();
            
        } catch (Exception e) {
        }
        //si hay excepcion socket.close();
        //verificar si receptor esta conectado mediante directorio
        //si esta conectado enviar mensaje directo
        //si no esta conectado  almacenar mensaje en estructura
    }

    public Mensaje prepararMsj() {
        try {
            String msj = ComunicacionMensajeria.getInstancia().recibirMsj();
            //deserializar y pasar a MensajeEmisor
            //Crear mensaje y asignar todo
            //retornarlo
        } catch (Exception e) {
        }
    }
}
