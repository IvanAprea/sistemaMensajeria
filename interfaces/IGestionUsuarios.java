package interfaces;

public interface IGestionUsuarios {
    void comprobacionUsuariosOnline();
    void limpiarUsuariosOnline();
    void nuevoUsuario();
    void setearUsuarioDesconectado(String nombre);
    void darLista();
    void desconectarUsuario(boolean cond);
}
