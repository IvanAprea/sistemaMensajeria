package interfaces;

public interface IGestionUsuarios {
    void comprobacionUsuariosOnline();
    void limpiarUsuariosOnline();
    void nuevoUsuario(String str);
    void setearUsuarioDesconectado(String ID);
    void darLista();
}
