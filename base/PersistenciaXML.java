package base;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.HashMap;

public class PersistenciaXML extends PersistenciaMensajeria
{
    
    private static PersistenciaXML _instancia = null;
    private FileOutputStream fileoutput;
    private FileInputStream fileinput;
    private ObjectOutputStream objectoutput;
    private ObjectInputStream objectinput;
    
    /**
     * Thread-protected Singleton
     * @return
     */
    private PersistenciaXML()
    {
        super();
    }
    
    public synchronized static PersistenciaXML getInstancia() {
        if (_instancia == null)
            _instancia = new PersistenciaXML();
        return _instancia;
    }
    
    public void backUp(HashMap<String, ArrayList<String>> mensajesNoEnviados,String fileName)
    {
        try
        {
            fileoutput = new FileOutputStream(fileName);
            objectoutput = new ObjectOutputStream(fileoutput);
            if(objectoutput != null){
                objectoutput.writeObject(mensajesNoEnviados);
                objectoutput.close();
            }
            
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public HashMap<String, ArrayList<String>> recuperarDatos(String fileName)
    {
        HashMap<String, ArrayList<String>> hm = new HashMap<String, ArrayList<String>>();
        try
        {
            fileinput = new FileInputStream(fileName);
            objectinput = new ObjectInputStream(fileinput);
            if(objectinput != null)
            {
                    hm = (HashMap<String, ArrayList<String>>) objectinput.readObject();
                    objectinput.close();
            }
        } 
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (EOFException e)
        {
            System.out.println("El archivo se abrio vacio");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return hm;
    }
}
