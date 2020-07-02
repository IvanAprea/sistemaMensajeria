package base;

import interfaces.IEncriptacion;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encriptadora implements IEncriptacion {
    
    private static final String ALGORITHM = "RSA";
    
    public Encriptadora() {
        super();
    }

    @Override
    public byte[] encriptar(byte[] publicKey, byte[] inputData) throws Exception{

            PublicKey key = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(inputData);

            return encryptedBytes;

    }
}
