package base;

import interfaces.IRedundanciaDir;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.StringReader;

import java.net.BindException;
import java.net.InetAddress;
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
            public void run() {
                try
                {
                    sedr = new ServerSocket(Integer.parseInt(puerto)); //el redundante va a establecer conexion aca.
                    sdr = sedr.accept(); //se conectan.
                    dOut = new DataOutputStream(sdr.getOutputStream());
                    dIn = new DataInputStream(sdr.getInputStream());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean conectarDirectorio()
    {
                try
                {
                    sdr = new Socket(InetAddress.getByName(IPDirRedundante), Integer.parseInt(puertoDirRedundante));
                    //pido los datos actuales al redundante
                    dOut = new DataOutputStream(sdr.getOutputStream());
                    dIn = new DataInputStream(sdr.getInputStream());
                    dOut.writeUTF("ACTUALIZAR_DATOS");
                    LogicaDirectorio.getInstancia().actualizarUsuariosRec(dIn.readUTF()); //recibo los datos que debo actualizar 1° UsuariosRecMap listaDirectorio
                    LogicaDirectorio.getInstancia().actualizarUsuariosOnline(dIn.readUTF()); // 2° ArrayList<String> usuariosOnlineActuales
                    return true;
                } catch (IOException e)
                {
                    System.out.println("Nadie esta escuchando para DirActivo, primer ejecución o redundante tambien caido.");
                    return false;
                    //si entra aca es porque nadie esta escuchando, osea que el activo se esta ejecutando por primera vez
                }
    }
    

    public void enviarActualizacion()
    {
        try
        {
            if(sdr!=null && sdr.isConnected()){
                dOut.writeUTF("BACKUP");
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUsuariosRec());
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUserOnline());
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String recibirDatos()
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
