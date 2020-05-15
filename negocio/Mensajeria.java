package negocio;

import base.ComunicacionDirectorio;
import base.ComunicacionMensajeria;

import java.io.IOException;
import java.io.StringReader;

import java.io.StringWriter;

import java.net.InetAddress;

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
            String msj = ComunicacionMensajeria.getInstancia().recibirMsj();
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(msj);
            MensajeEmisor mensajeEm = (MensajeEmisor) unmarshaller.unmarshal(reader);
            StringWriter sw = new StringWriter();
            sw.write(msj);
            ComunicacionMensajeria.getInstancia().enviarMensaje(sw, InetAddress.getByName(mensajeEm.getReceptor().getIP()), Integer.parseInt(mensajeEm.getReceptor().getPuerto()));
        } catch (IOException e) {
            //guardar en estructura
        } catch (Exception e) {
            //ver como tratar error
            e.printStackTrace();
        }
    }
}
