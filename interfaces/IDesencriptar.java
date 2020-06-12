package interfaces;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface IDesencriptar {
    void setKeys() throws NoSuchAlgorithmException, NoSuchProviderException;
    byte[] getPublicKey();
    byte[] desencriptar(byte[] inputData) throws Exception;
}
