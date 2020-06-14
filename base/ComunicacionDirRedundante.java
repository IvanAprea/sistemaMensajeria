package base;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import negocio.LogicaDirectorio;

public class ComunicacionDirRedundante extends ComunicacionDirectorio
{
    private ServerSocket seda; //socketEscucharDirActivo
    private Socket sda; //socketDirActivo
    private DataInputStream dIn;
    private DataOutputStream dOut;
    private String IPDirActivo, puertoDirActivo;
    
    public ComunicacionDirRedundante() {
        super();
    }

    @Override
    public boolean conectarDirectorio()
    {
        try
        {
            sda = new Socket(InetAddress.getByName(IPDirActivo), Integer.parseInt(puertoDirActivo));
            dIn = new DataInputStream(sda.getInputStream());
        } catch (IOException e)
        {
            //el mensajeria esta caido, entonces entro aca.
            e.printStackTrace();
        }
        try
        {
            while(true)
            {
                LogicaDirectorio.getInstancia().ejecutarComando(dIn.readUTF());
            }
        } catch (IOException e)
        {
             // entra aca si se cae el activo? SI ENTRA
            return false;
        }
    }

    @Override
    public void escucharDirectorio(String puerto)
    {
        new Thread() {
            public void run() {
                while(true){
                    try
                    {
                        seda = new ServerSocket(Integer.parseInt(puerto)); //el activo va a conectarse aca
                        sda = seda.accept(); //se conectan.
                        while(true)
                        {
                            LogicaDirectorio.getInstancia().ejecutarComando(dIn.readUTF());
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void enviarActualizacion()
    {
        try
        {
            if(sda.isConnected())
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUsuariosRec());
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUserOnline());
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
        this.IPDirActivo = s;
    }

    @Override
    public void setPuertoDir(String s)
    {
        this.puertoDirActivo = s;
    }


}
