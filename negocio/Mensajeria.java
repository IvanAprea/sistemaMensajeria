package negocio;

import base.ComunicacionDirectorio;
import base.ComunicacionMensajeria;

import java.io.IOException;
import java.io.StringReader;

import java.io.StringWriter;

import java.net.InetAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Mensajeria {
    
    private static Mensajeria _instancia = null;
    private HashMap<String, ArrayList<String>> mensajesNoEnviados;
    private HashMap<String, ArrayList<String>> mensajesNoEnviadosCAviso;
    
    private Mensajeria() {
        super();
        this.mensajesNoEnviados = new HashMap<String, ArrayList<String>>();
        this.mensajesNoEnviadosCAviso = new HashMap<String, ArrayList<String>>();
    }
    
    public synchronized static  Mensajeria getInstancia(){
        if(_instancia == null)
            _instancia = new Mensajeria();
        return _instancia;
    }
    
    public synchronized void enviarPendientes(String id){
        StringWriter sw = new StringWriter();
        try{
            if(Mensajeria.getInstancia().getMensajesNoEnviados().containsKey(id)){
                Iterator it = Mensajeria.getInstancia().getMensajesNoEnviados().get(id).iterator();
                while(it.hasNext()){
                    String msj = (String) it.next();
                    sw.write("TRUE");
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    sw.write(msj);
                    ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
                    sw.getBuffer().setLength(0);
                    it.remove();
                }
            }
            sw.write("FALSE");
            ComunicacionMensajeria.getInstancia().enviarPendientes(sw);
            sw.getBuffer().setLength(0);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void intentarEnviarMensaje() {
        MensajeEmisor mensajeEm = null;
        String msj = null;
        try {
            msj = ComunicacionMensajeria.getInstancia().recibirMsj();
            javax.xml.bind.JAXBContext context = javax.xml.bind.JAXBContext.newInstance(MensajeEmisor.class);
            javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader reader = new StringReader(msj);
            mensajeEm = (MensajeEmisor) unmarshaller.unmarshal(reader);
            StringWriter sw = new StringWriter();
            sw.write(msj);
            ComunicacionMensajeria.getInstancia().enviarMensaje(sw, InetAddress.getByName(mensajeEm.getReceptor().getIP()), Integer.parseInt(mensajeEm.getReceptor().getPuerto()));
            //avisar al emisor que se recibio:
            ComunicacionMensajeria.getInstancia().notificarEmisorLlegadaMsj(ComunicacionMensajeria.getInstancia().recibirMsj());
        } catch (IOException e) {
            String id = mensajeEm.getReceptor().getIP()+":"+mensajeEm.getReceptor().getPuerto();
            ArrayList<String> arr;
            int tipo = mensajeEm.getTipo();
            if(this.getMensajesNoEnviados().containsKey(id) && tipo!=2){ //ya existe arrayList
                arr = this.getMensajesNoEnviados().get(id);
                arr.add(msj);
            }
            else{
                arr = new ArrayList<String>();
                arr.add(msj);
                this.getMensajesNoEnviados().put(id, arr);
            }
            if(this.getMensajesNoEnviadosCAviso().containsKey(id) && tipo==2){
                arr = this.getMensajesNoEnviadosCAviso().get(id);
                arr.add(msj);
            }
            else{
                arr = new ArrayList<String>();
                arr.add(msj);
                this.getMensajesNoEnviadosCAviso().put(id, arr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("MSJ_NUEVOMSJ")){
            this.intentarEnviarMensaje();
        }
        else if(comando.equalsIgnoreCase("MSJ_PEDIDOMSJREC")){
            this.enviarPendientes(obtenerIDReceptor());
        }
    }

    public HashMap<String, ArrayList<String>> getMensajesNoEnviados() {
        return mensajesNoEnviados;
    }
    
    public HashMap<String, ArrayList<String>> getMensajesNoEnviadosCAviso() {
        return mensajesNoEnviadosCAviso;
    }
    
    public String obtenerIDReceptor(){
        try {
            return ComunicacionMensajeria.getInstancia().recibirMsj();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
                            
}
