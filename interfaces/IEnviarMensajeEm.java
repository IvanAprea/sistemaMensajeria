package interfaces;

import java.util.List;

import negocio.UsuarioReceptor;

public interface IEnviarMensajeEm{
    void enviarMensaje(List<UsuarioReceptor> listaPersonas);
    void recibirConfirmacion(String receptor,String fecha);
    void obtenerListaReceptores() throws Exception;
}