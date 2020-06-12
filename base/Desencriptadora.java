package base;

import interfaces.IDesencriptar;

public class Desencriptadora implements IDesencriptar {
    public Desencriptadora() {
        super();
    }

    @Override
    public void setKeys() {
        // TODO Implement this method
    }

    @Override
    public byte[] getPublicKey() {
        // TODO Implement this method
        return new byte[0];
    }

    @Override
    public byte[] getPrivateKey() {
        // TODO Implement this method
        return new byte[0];
    }
    
    @Override
    public byte[] desencriptar(byte[] privateKey, byte[] inputData) {
        // TODO Implement this method
        return new byte[0];
    }

}
