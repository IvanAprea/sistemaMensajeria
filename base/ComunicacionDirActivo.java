package base;

import interfaces.IRedundanciaDir;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.StringReader;

import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.ArrayList;

import java.util.Observable;
import java.util.Observer;

import javax.xml.bind.JAXBException;

import negocio.LogicaDirectorio;
import negocio.LogicaEmisor;
import negocio.Mensaje;
import negocio.MensajeEmisor;
import negocio.UsuariosRecMap;

public class ComunicacionDirActivo extends ComunicacionDirectorio
{
    private ServerSocket sedr; //socketEscucharDirRedundante
    private Socket sdr; //socketDirRedundante
    private String IPDirRedundante, puertoDirRedundante;
    private DataInputStream dIn;
    private DataOutputStream dOut;
    
    public ComunicacionDirActivo() {
        super();
    }

    @Override
    public void escucharDirectorio(String puerto)
    {
        new Thread() {
            public synchronized void run() {
                try
                {
                    while(true){
                        try{
                            sedr = new ServerSocket(Integer.parseInt(puerto)); //el redundante va a establecer conexion aca.
                            sdr = sedr.accept(); //se conectan.
                            dOut = new DataOutputStream(sdr.getOutputStream());
                            dIn = new DataInputStream(sdr.getInputStream());
                            dIn.readUTF();
                        } catch (IOException e)
                        {try{
                            if(sedr!=null)
                                sedr.close();
                            if(sdr!=null)
                                sdr.close();
                             sdr = null;
                             sedr = null;
                         }catch(IOException f)
                         {
                             f.printStackTrace();
                         }
                        } 
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public synchronized boolean conectarDirectorio()
    {
                try
                {
                    sdr = new Socket();
                    sdr.connect(new InetSocketAddress(InetAddress.getByName(IPDirRedundante), Integer.parseInt(puertoDirRedundante)),500);
                    //pido los datos actuales al redundante
                    dOut = new DataOutputStream(sdr.getOutputStream());
                    dIn = new DataInputStream(sdr.getInputStream());
                    dOut.writeUTF("ACTUALIZAR_DATOS");
                    LogicaDirectorio.getInstancia().actualizarUsuariosRec(dIn.readUTF()); //recibo los datos que debo actualizar 1° UsuariosRecMap listaDirectorio
                    LogicaDirectorio.getInstancia().actualizarUsuariosOnline(dIn.readUTF()); // 2° ArrayList<String> usuariosOnlineActuales
                    return true;
                } catch (IOException e)
                {
                    sdr = null;
                    System.out.println("Nadie esta escuchando para DirActivo, primer ejecución o redundante tambien caido.");
                    return false;
                    //si entra aca es porque nadie esta escuchando, osea que el activo se esta ejecutando por primera vez
                }
    }
    

    public synchronized void enviarActualizacion()
    {
        try
        {
            if(sdr!=null && sdr.isConnected()){
                System.out.println("Realizando BACKUP");
                dOut.writeUTF("BACKUP");
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUsuariosRec());
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUserOnline());
                }else
                {
                    notifyAll();
                }
        } catch (IOException e)
        {
            sdr=null;
            System.out.println("Se perdio la conexion");
            escucharDirectorio("72");
        }
    }

    @Override
    public synchronized String recibirDatos()
    {
        try
        {
            return dIn.readUTF();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void setIpDir(String s)
    {
        this.IPDirRedundante = s;
    }

    @Override
    public void setPuertoDir(String s)
    {
        this.puertoDirRedundante = s;
    }


}
