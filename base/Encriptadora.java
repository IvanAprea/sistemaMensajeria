package base;

import interfaces.IEncriptar;

public class Encriptadora implements IEncriptar {
    public Encriptadora() {
        super();
    }

    @Override
    public byte[] encriptar(byte[] publicKey, byte[] inputData) {
        return new byte[0];
    }
}
