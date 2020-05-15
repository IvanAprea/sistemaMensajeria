package base;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;

public class PersistenciaMensajeria {
    
    private static PersistenciaMensajeria _instancia = null;
    private XMLEncoder xe;
    private XMLDecoder xd;
    private FileOutputStream fileoutput;
    private FileInputStream fileinput;
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static PersistenciaMensajeria getInstancia()
    {
        if(_instancia == null)
            _instancia = new PersistenciaMensajeria();
        return _instancia;
    }
    
    private PersistenciaMensajeria()
    {
        super();
    }
    
    public void backUp(HashMap<String, ArrayList<String>> mensajesNoEnviados,String fileName)
    {
        try
        {
            fileoutput = new FileOutputStream(fileName);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        xe = new XMLEncoder(fileoutput);
        xe.writeObject(mensajesNoEnviados);
        xe.close();
    }
    
    public Serializable recuperarDatos(String fileName)
    {
        Serializable s = null;

        try
        {
            fileinput = new FileInputStream(fileName);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        xd = new XMLDecoder(fileinput);
        if(xd != null)
        {
            s = (Serializable) xd.readObject();
        }
        xd.close();
        return s;
    }
}
