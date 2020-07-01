package base;

import java.io.DataOutputStream;
import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Iterator;

public class Sincronizadora {
    private static Sincronizadora _instancia = null;
    private Socket s;
    private ArrayList<String> direccionesDirectorios = new ArrayList<String>(); // de otros directorios, la propia no está
    
    private Sincronizadora() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static Sincronizadora getInstancia()
    {
        if(_instancia == null)
            _instancia = new Sincronizadora();
        return _instancia;
    }


    public ArrayList<String> getDireccionesDirectorios()
    {
        return direccionesDirectorios;
    }

    public void nuevoUsuario(String usr) {
        Iterator it = this.getDireccionesDirectorios().iterator();
        String idAct,ipAct,puertoAct;
        while(it.hasNext()){
            idAct = (String) it.next();
            ipAct = getIPbyID(idAct);
            puertoAct = getPuertoByID(idAct);
            try {
                s = new Socket(InetAddress.getByName(ipAct),Integer.parseInt(puertoAct));
                DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                dOut.writeUTF("DIR_SYNC_AGREGAR");
                dOut.writeUTF(usr);
                dOut.flush();
                s.close();
                
            } catch (IOException e) { //VER QUE HACER ACA (SI HAY UN REDUNDANDTE CAIDO) -> EN TEORIA NADA Y DESPUES PIDE TODO CUANDO SE LEVANTE
                try {
                    if(s!=null){
                        s.close();
                    }
                } catch (IOException f) {
                    f.printStackTrace();
                }
                
            }
        }
    }

    public void desconectarUsuario(String nombre) {
        Iterator it = this.getDireccionesDirectorios().iterator();
        String idAct,ipAct,puertoAct;
        while(it.hasNext()){
            idAct = (String) it.next();
            ipAct = getIPbyID(idAct);
            puertoAct = getPuertoByID(idAct);
            try {
                s = new Socket(InetAddress.getByName(ipAct),Integer.parseInt(puertoAct));
                DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                dOut.writeUTF("DIR_SYNC_DESCONECTAR");
                dOut.writeUTF(nombre);
                dOut.flush();
                s.close();
                
            } catch (IOException e) { //VER QUE HACER ACA (SI HAY UN REDUNDANDTE CAIDO) -> EN TEORIA NADA Y DESPUES PIDE TODO CUANDO SE LEVANTE
                try {
                    if(s!=null){
                        s.close();
                    }
                } catch (IOException f) {
                    f.printStackTrace();
                }
                
            }
        }
    }

    public void recibirAlive(String nombre) {
        Iterator it = this.getDireccionesDirectorios().iterator();
        String idAct,ipAct,puertoAct;
        while(it.hasNext()){
            idAct = (String) it.next();
            ipAct = getIPbyID(idAct);
            puertoAct = getPuertoByID(idAct);
            try {
                s = new Socket(InetAddress.getByName(ipAct),Integer.parseInt(puertoAct));
                DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                dOut.writeUTF("DIR_SYNC_ALIVE");
                dOut.writeUTF(nombre);
                dOut.flush();
                s.close();
                
            } catch (IOException e) { //VER QUE HACER ACA (SI HAY UN REDUNDANDTE CAIDO) -> EN TEORIA NADA Y DESPUES PIDE TODO CUANDO SE LEVANTE
                try {
                    if(s!=null){
                        s.close();
                    }
                } catch (IOException f) {
                    f.printStackTrace();
                }
                
            }
        }
    }
    
    private String getIPbyID(String idRecepetor) {
        return idRecepetor.split(":")[0];
    }

    private String getPuertoByID(String idRecepetor) {
        return idRecepetor.split(":")[1];
    }
}
