package base;

import interfaces.IBackUp;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Persistencia implements IBackUp{
    
    /**
     * Thread-protected Singleton
     * @return
     */
    
    
    public Persistencia()
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
