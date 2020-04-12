package presentacion;

import java.util.Map;

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
    void enviarMensaje();
    void actualizarListaDirectorio(Map<String, UsuarioReceptor> personas);
    void confirmarDestinatarios();  
    void cancelarDestinatarios();
    void addActionListener(ActionListener actionListener);
    void abrirSesion();
    void confirmarSesion();
    void cerrarSesion();
    void lanzarCartelError(String err);
    void addKeyListener(KeyListener kl1, KeyListener kl2);
}
