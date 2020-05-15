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
    private ArrayList<String> pendientesDeEnvio;
    private boolean mensajesNoEnviadosOcupado=false;
    private boolean pendientesDeEnvioOcupado=false;
    //donde ponerlos true? (ademas del thread)
    
    private Mensajeria() {
        super();
        this.mensajesNoEnviados = new HashMap<String, ArrayList<String>>();
        this.pendientesDeEnvio = new ArrayList<String>();
    }
    
    public synchronized static  Mensajeria getInstancia(){
        if(_instancia == null)
            _instancia = new Mensajeria();
        return _instancia;
    }
    
    public synchronized void mensajeriaDeamon(){
        Thread tr = new Thread(){
            public synchronized void  run(){
                try { 
                    while(true){
                        Thread.sleep(1500);
                        while(Mensajeria.getInstancia().isMensajesNoEnviadosOcupado() == true ||
                              Mensajeria.getInstancia().isPendientesDeEnvioOcupado() == true){
                            wait();
                        }
                        Mensajeria.getInstancia().setMensajesNoEnviadosOcupado(true);
                        Mensajeria.getInstancia().setPendientesDeEnvioOcupado(true);
                        Iterator it = Mensajeria.getInstancia().getPendientesDeEnvio().iterator();
                        while(it.hasNext()){
                            String id = (String) it.next();
                            String[] tokens = id.split(":");
                            StringWriter sw = new StringWriter();
                            if(Mensajeria.getInstancia().getMensajesNoEnviados().containsKey(id)){
                                Iterator it2 = Mensajeria.getInstancia().getMensajesNoEnviados().get(id).iterator();
                                while(it2.hasNext()){
                                    String msj = (String) it.next();
                                    sw.write("TRUE");
                                    ComunicacionMensajeria.getInstancia().enviarMensaje(sw, InetAddress.getByName(tokens[0]), Integer.parseInt(tokens[1]));
                                    sw.write(msj);
                                    ComunicacionMensajeria.getInstancia().enviarMensaje(sw, InetAddress.getByName(tokens[0]), Integer.parseInt(tokens[1]));
                                }
                            }
                            sw.write("FALSE");
                            ComunicacionMensajeria.getInstancia().enviarMensaje(sw, InetAddress.getByName(tokens[0]), Integer.parseInt(tokens[1]));
                        }
                        Mensajeria.getInstancia().setMensajesNoEnviadosOcupado(false);
                        Mensajeria.getInstancia().setPendientesDeEnvioOcupado(false);
                        notifyAll();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Lanzar error
                }
            }
        };
        tr.start();
    }

    public void setMensajesNoEnviadosOcupado(boolean mensajesNoEnviadosOcupado) {
        this.mensajesNoEnviadosOcupado = mensajesNoEnviadosOcupado;
    }

    public boolean isMensajesNoEnviadosOcupado() {
        return mensajesNoEnviadosOcupado;
    }

    public void setPendientesDeEnvioOcupado(boolean pendientesDeEnvioOcupado) {
        this.pendientesDeEnvioOcupado = pendientesDeEnvioOcupado;
    }

    public boolean isPendientesDeEnvioOcupado() {
        return pendientesDeEnvioOcupado;
    }

    public synchronized void ejecutarComando(String comando) {
        if(comando.equalsIgnoreCase("MSJ_NUEVOMSJ")){
            this.intentarEnviarMensaje();
        }
        else if(comando.equalsIgnoreCase("MSJ_PEDIDOMSJREC")){
            this.agregarAPendientes();
        }
    }

    public HashMap<String, ArrayList<String>> getMensajesNoEnviados() {
        return mensajesNoEnviados;
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
        } catch (IOException e) {
            String id = mensajeEm.getReceptor().getIP()+":"+mensajeEm.getReceptor().getPuerto();
            ArrayList<String> arr;
            if(this.getMensajesNoEnviados().containsKey(id)){ //ya existe arrayList
                arr = this.getMensajesNoEnviados().get(id);
                arr.add(msj);
            }
            else{
                arr = new ArrayList<String>();
                arr.add(msj);
                this.getMensajesNoEnviados().put(id, arr);
            }
        } catch (Exception e) {
            //ver como tratar error
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPendientesDeEnvio() {
        return pendientesDeEnvio;
    }

    private void agregarAPendientes() {
        try {
            String id = ComunicacionMensajeria.getInstancia().recibirMsj();
            this.getPendientesDeEnvio().add(id);
        } catch (Exception e) {
            //VER como tratar error
        }

    }
}
