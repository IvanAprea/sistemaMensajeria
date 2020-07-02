package interfaces;

public interface IEncriptacion {
    byte[] encriptar(byte[] publicKey, byte[] inputData) throws Exception;
}
