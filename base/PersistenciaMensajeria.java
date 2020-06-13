package base;

import interfaces.IBackUp;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PersistenciaMensajeria implements IBackUp{
    
    /**
     * Thread-protected Singleton
     * @return
     */
    
    
    public PersistenciaMensajeria()
    {
        super();
    }
    
    public final void persistir(HashMap<String, ArrayList<String>> mensajesNoEnviados,String fileName)
    {
        this.backUp(mensajesNoEnviados,fileName);
    }
    
    public final HashMap<String, ArrayList<String>> recuperar(String fileName)
    {
        return this.recuperarDatos(fileName);
    }
    
    
    public abstract void backUp(HashMap<String, ArrayList<String>> mensajesNoEnviados,String fileName);
    
    public abstract HashMap<String, ArrayList<String>> recuperarDatos(String fileName);
}
