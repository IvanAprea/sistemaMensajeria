package base;

import interfaces.ICargaConfig;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Iterator;

public class Sincronizadora{
    private static Sincronizadora _instancia = null;
    private Socket s;
    private ArrayList<String> direccionesDirectorios = new ArrayList<String>(); // de otros directorios, la propia no está
    private final int TIMEOUT=1000;
    
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
                s= new Socket();
                s.connect(new InetSocketAddress(InetAddress.getByName(ipAct),Integer.parseInt(puertoAct)), TIMEOUT);
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
                s= new Socket();
                s.connect(new InetSocketAddress(InetAddress.getByName(ipAct),Integer.parseInt(puertoAct)), TIMEOUT);
                DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                dOut.writeUTF("DIR_SYNC_DESCONECTAR");
                dOut.writeUTF(nombre);
                dOut.flush();
                s.close();
                
            } catch (IOException e) { //VER QUE HACER ACA (SI HAY UN REDUNDANDTE CAIDO) -> EN TEORIA NADA Y DESPUES PIDE TODO CUANDO SE LEVANTE
                try {
                    System.out.println("El directiorio que quiero sincronizar esta caido.");
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
                s= new Socket();
                s.connect(new InetSocketAddress(InetAddress.getByName(ipAct),Integer.parseInt(puertoAct)), TIMEOUT);
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
    
    public ArrayList<String> solicitarBackUp(){
        ArrayList<String> al = new ArrayList<String>();
        al.clear();
        Iterator it = this.getDireccionesDirectorios().iterator();
        String idAct,ipAct,puertoAct;
        
        while(it.hasNext() && al.isEmpty()){
            idAct = (String) it.next();
            ipAct = getIPbyID(idAct);
            puertoAct = getPuertoByID(idAct);
            try {
                s= new Socket();
                s.connect(new InetSocketAddress(InetAddress.getByName(ipAct),Integer.parseInt(puertoAct)), TIMEOUT);
                DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
                DataInputStream dIn = new DataInputStream(s.getInputStream());
                dOut.writeUTF("DIR_SYNC_BACKUP");
                dOut.flush();
                al.add(dIn.readUTF());
                al.add(dIn.readUTF());
            } catch (IOException e) {
                System.out.println("No se pudo conectar con "+idAct+ "para solicitar backUp");
            }
            finally{
                try {
                    if(s!=null)
                        s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return al;
    }
    
    private String getIPbyID(String idRecepetor) {
        return idRecepetor.split(":")[0];
    }

    private String getPuertoByID(String idRecepetor) {
        return idRecepetor.split(":")[1];
    }

}
