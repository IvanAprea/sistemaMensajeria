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
    public void setNewKeys() throws NoSuchAlgorithmException, NoSuchProviderException {
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

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public void persistirKeys(String nombre) {
        PersistenciaXML.getInstancia().persistir(this.getPublicKey(), "/keys/"+nombre+"public.txt");
        PersistenciaXML.getInstancia().persistir(this.getPrivateKey(), "/keys/"+nombre+"private.txt");
    }

    @Override
    public void recuperarKeys(String nombre) {
        this.setPublicKey((byte[]) PersistenciaXML.getInstancia().recuperar("/keys/"+nombre+"public.txt"));
        this.setPrivateKey((byte[]) PersistenciaXML.getInstancia().recuperar("/keys/"+nombre+"private.txt"));
    }
    
    @Override
    public boolean isYaExistenKeys(String nombre){
        return (PersistenciaXML.getInstancia().isFileExist("/keys/"+nombre+"public.txt") 
                && PersistenciaXML.getInstancia().isFileExist("/keys/"+nombre+"private.txt"));
    }
}
