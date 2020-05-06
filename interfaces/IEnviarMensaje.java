package interfaces;

import java.util.List;

import negocio.UsuarioReceptor;

public interface IEnviarMensaje {
    void enviarMensaje(List<UsuarioReceptor> listaPersonas);
    void recibirConfirmacion(String receptor);
    void obtenerListaReceptores();
}
