package interfaces;

public interface IConfirmacionEmisor
{
    void recibirConfirmacion(String receptor, String fecha);
    void confirmacionPendiente(String receptor);
}
