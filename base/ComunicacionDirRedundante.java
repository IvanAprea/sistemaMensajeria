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
    public void conectarDirectorio()
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
            escucharDirectorio("73"); // entra aca si se cae el activo? SI ENTRA
        }
    }

    @Override
    public void escucharDirectorio(String puerto)
    {
        new Thread() {
            public void run() {
                try
                {
                    while(true){
                        seda = new ServerSocket(Integer.parseInt(puerto)); //el activo va a conectarse aca
                        sda = seda.accept(); //se conectan.
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void cargarDatosDir()
    {
        // TODO Implement this method
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
