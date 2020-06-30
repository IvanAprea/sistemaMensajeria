package base;

import java.util.ArrayList;

public class Sincronizadora {
    
    private static Sincronizadora _instancia = null;
    private ArrayList<String> direccionesDirectorios = new ArrayList<String>();
    
    private Sincronizadora() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static Sincronizadora getInstancia()
    {
        if(_instancia == null)
            _instancia = new Sincronizadora();
        return _instancia;
    }


    public ArrayList<String> getDireccionesDirectorios()
    {
        return direccionesDirectorios;
    }
}
