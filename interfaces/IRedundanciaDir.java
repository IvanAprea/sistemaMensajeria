package interfaces;

public interface IRedundanciaDir
{
    public abstract boolean conectarDirectorio();
    public abstract void escucharDirectorio(String puerto);
    public abstract String recibirDatos();
    public abstract void enviarActualizacion();
}
