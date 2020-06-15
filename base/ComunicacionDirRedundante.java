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
    public synchronized boolean conectarDirectorio()
    {
        try
        {
            sda = new Socket(InetAddress.getByName(IPDirActivo), Integer.parseInt(puertoDirActivo));
            dIn = new DataInputStream(sda.getInputStream());
            try
            {
                while(true)
                {
                    LogicaDirectorio.getInstancia().ejecutarComando(dIn.readUTF());
                }
            } catch (IOException e)
            {
                 // entra aca si se cae el activo? SI ENTRA
                sda = null;
                return false;
            }
        } catch (IOException e)
        {
            //el activo esta caido, entonces entro aca.
            return false;
        }

    }

    @Override
    public synchronized void escucharDirectorio(String puerto)
    {
        new Thread() {
            public void run() {
                while(true){
                    try
                    {
                        LogicaDirectorio.getInstancia().setDesconectado(true);
                        LogicaDirectorio.getInstancia().comprobacionUsuariosOnline();
                        seda = new ServerSocket(Integer.parseInt(puerto)); //el activo va a conectarse aca
                        sda = seda.accept(); //se conectan.
                        LogicaDirectorio.getInstancia().setDesconectado(false);
                        dIn = new DataInputStream(sda.getInputStream());
                        dOut = new DataOutputStream(sda.getOutputStream());
                        while(true)
                        {
                            LogicaDirectorio.getInstancia().ejecutarComando(dIn.readUTF());
                        }
                    } catch (IOException e)
                    {
                        try
                        {
                            if(sda!=null)
                                sda.close();
                            if(seda!=null)
                                seda.close();
                        } catch (IOException f)
                        {
                            f.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    public synchronized void enviarActualizacion()
    {
        try
        {
            if(!LogicaDirectorio.getInstancia().isDesconectado() && sda!= null && sda.isConnected() ){
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUsuariosRec());
                dOut.writeUTF(LogicaDirectorio.getInstancia().convertirUserOnline());
            }
        } catch (IOException e)
        {
            e.printStackTrace();
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
        this.IPDirActivo = s;
    }

    @Override
    public void setPuertoDir(String s)
    {
        this.puertoDirActivo = s;
    }


}
