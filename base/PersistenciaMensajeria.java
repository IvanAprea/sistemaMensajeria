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
    
    public final void persistir(Object objeto,String fileName)
    {
        this.backUp(objeto,fileName);
    }
    
    public final Object recuperar(String fileName)
    {
        return this.recuperarDatos(fileName);
    }
    
    
    public abstract void backUp(Object mensajesNoEnviados,String fileName);
    
    public abstract Object recuperarDatos(String fileName);
}
