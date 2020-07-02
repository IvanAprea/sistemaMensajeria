package interfaces;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface IDesencriptacion {
    void setNewKeys() throws NoSuchAlgorithmException, NoSuchProviderException;
    void setPublicKey(byte[] publicKey);
    void setPrivateKey(byte[] privateKey);
    byte[] getPublicKey();
    byte[] getPrivateKey();
    byte[] desencriptar(byte[] inputData) throws Exception;
    void persistirKeys(String nombre);
    void recuperarKeys(String nombre);
    boolean isYaExistenKeys(String nombre);
}
