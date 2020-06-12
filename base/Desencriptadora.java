package base;

import interfaces.IDesencriptar;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

public class Desencriptadora implements IDesencriptar {
    
    private static final String ALGORITHM = "RSA";
    private byte[] publicKey;
    private byte[] privateKey;
                      
    public Desencriptadora() {
        super();
    }

    @Override
    public void setKeys() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        // 512 is keysize
        keyGen.initialize(512, random);
        KeyPair generateKeyPair = keyGen.generateKeyPair();
        this.publicKey = generateKeyPair.getPublic().getEncoded();
        this.privateKey = generateKeyPair.getPrivate().getEncoded();
    }

    @Override
    public byte[] getPublicKey() {
        return this.publicKey;
    }

    public byte[] getPrivateKey() {
        return this.privateKey;
    }
    
    @Override
    public byte[] desencriptar(byte[] inputData) throws Exception {
        PrivateKey key = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(inputData);
        return decryptedBytes;
    }

}
