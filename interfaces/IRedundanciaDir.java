package interfaces;

public interface IRedundanciaDir
{
    public abstract void conectarDirectorio();
    public abstract void escucharDirectorio(String puerto);
    public abstract String recibirDatos();
}
