package lk.oxo.eshop.util;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

import lk.oxo.eshop.R;

public class Encryption {
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Context context;
    private Cipher cipher;

    public Encryption(Context context) {
        this.context = context;
        try {
            keyStore = KeyStore.getInstance(context.getString(R.string.keystore_type));
            keyStore.load(null);
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, context.getString(R.string.keystore_type));
        } catch (KeyStoreException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (CertificateException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        }
    }
private byte[] i = null;
    public byte[] encrypt(String password) {
        byte[] bytes = null;
        try {
            cipher = Cipher.getInstance(context.getString(R.string.encryption_cipher));
//            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES);

           byte[] iv = new byte[cipher.getBlockSize()];
//            SecureRandom random = new SecureRandom();
//            random.nextBytes(iv);
//            i = iv;
            cipher.init(Cipher.ENCRYPT_MODE, generateKey());
            i = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            byte[] enctyptedData = cipher.doFinal(password.getBytes("UTF-8"));
//            System.out.println(enctyptedData);
//            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + enctyptedData.length);
//            byteBuffer.put(iv);
//            byteBuffer.put(enctyptedData);
//            bytes= byteBuffer.array();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(iv);
            outputStream.write(enctyptedData);
            bytes = outputStream.toByteArray();
        }  catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidParameterSpecException e) {
            throw new RuntimeException(e);
        }
        decrypt(bytes.toString());
        return bytes;
    }

    public String decrypt(String encodedPassword) {
        System.out.println(encodedPassword);
        String password = null;

        byte[] decode = Base64.decode(encodedPassword, Base64.DEFAULT);
//        System.out.println(decode);
        try {
            SecretKey key = (SecretKey) keyStore.getKey(context.getString(R.string.encryption_alias), null);
            cipher = Cipher.getInstance(context.getString(R.string.encryption_cipher));
//            byte[] iv = Arrays.copyOfRange(decode, 0, cipher.getBlockSize());
//            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES);
            byte[] iv = Arrays.copyOfRange(decode, 0, cipher.getBlockSize());
//            IvParameterSpec parameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] bytes = cipher.doFinal(Arrays.copyOfRange(decode,cipher.getBlockSize(),decode.length));
            System.out.println(bytes);
            password = new String(bytes,"UTF-8");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (BadPaddingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        } catch (KeyStoreException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
        System.out.println(password);
        return password;
    }

    private SecretKey generateKey() {
        SecretKey key = null;
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec
                .Builder(context.getString(R.string.encryption_alias),
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
        builder.setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
        try {
            keyGenerator.init(builder.build());
            key = keyGenerator.generateKey();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.encryption_exception), Toast.LENGTH_SHORT).show();
        }
        return key;
    }
}
