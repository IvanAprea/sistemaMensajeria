package interfaces;

public interface IEncriptar {
    byte[] encriptar(byte[] publicKey, byte[] inputData) throws Exception;
}
