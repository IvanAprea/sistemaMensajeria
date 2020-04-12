package presentación;

import negocio.Persona;

import java.awt.event.ActionListener;

import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import negocio.UsuarioReceptor;

public interface IVentanaEmisor {
    String getAsunto();

    String getMensaje();

    List<UsuarioReceptor> getPersonas();

    int getTipo();

    String getNombreConfig();

    String getApellidoConfig();

    void enviarMensaje();

    void actualizarListaDirectorio(HashMap<String, UsuarioReceptor> personas);

    void addActionListener(ActionListener actionListener);

    void abrirConfig();

    void confirmarConfiguracion();

    void cerrarConfig();

    void lanzarCartelError(String err);

    String getIPConfig();

    String getPuertoConfig();

    void addKeyListener(KeyListener kl1, KeyListener kl2);
}
