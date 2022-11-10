package co.sentinel.cosmos.crypto;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyStore;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class WannabitKeyStore {

    private static final String KEYSTORE = "AndroidKeyStore";

    public static SecretKey createSecretKey(final Context context, final String alias) {
        SecretKey result = null;

        try {
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE);
            AlgorithmParameterSpec spec;

            spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();

            keyGenerator.init(spec);
            result =  keyGenerator.generateKey();

        } catch (Exception e) {

        } finally {
            return result;
        }
    }

    public static  SecretKey getSecretKey(final String alias) {
        SecretKey result = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE);
            keyStore.load(null);

            result = ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();

        } catch (Exception e) {

        } finally {
            return result;

        }

    }
}
