package interfaces;

import java.util.ArrayList;

public interface ISincronizacion
{
    void nuevoUsuario(String usr);
    void desconectarUsuario(String nombre);
    void recibirAlive(String nombre);
    ArrayList<String> solicitarBackUp();
}
