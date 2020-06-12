package interfaces;

public interface IDesencriptar {
    void setKeys();
    byte[] getPublicKey();
    byte[] getPrivateKey();
    byte[] desencriptar(byte[] privateKey, byte[] inputData);
}
