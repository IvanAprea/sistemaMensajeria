package base;

public class ComunicacionDirectorio {

    private static ComunicacionDirectorio _instancia = null;
    
    private ComunicacionDirectorio() {
        super();
    }
    
    /**
     * Thread-protected Singleton
     * @return
     */
    public synchronized static ComunicacionDirectorio getInstancia()
    {
        if(_instancia == null)
            _instancia = new ComunicacionDirectorio();
        return _instancia;
    }
}
