package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


class CriptoAesGcmCipher {
    /**
     * Keystore from the application-specific Android provider.
     */
    private var myKeyStore: KeyStore? = null

    /**
     * SecretKey from the application-specific Android provider
     */
    private var key: SecretKey? = null

    init {
        createAesKey()
        setupKeystore()
        generateKeyIntoKeystore()
    }

    /**
     * Load Android keystore.
     */
    private fun setupKeystore() {
        try {
            myKeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            myKeyStore?.load(null)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: KeyStoreException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun generateKeyIntoKeystore() {
        try {
            if (!myKeyStore!!.containsAlias(ALIAS_KEY)) {
                val generator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
                generator.init(
                    KeyGenParameterSpec.Builder(
                        ALIAS_KEY,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build()
                )
                generator.generateKey()
            }
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun createAesKey() {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES)
        keyGenerator.init(AES_KEY_SIZE)

        // Generate Key
        key = keyGenerator.generateKey()
    }

    @Throws(Exception::class)
    fun encrypt(arrayBytes: ByteArray?): ByteArray {
        // Get Cipher Instance for selected algorithm
        val cipher = Cipher.getInstance(TRANSFORMATION_AES_GCM_NOPADDING)

        // Get the key previously stored in the key store
        key = (myKeyStore!!.getEntry(ALIAS_KEY, null) as KeyStore.SecretKeyEntry).secretKey
        var IV = ByteArray(GCM_IV_LENGTH)
        val random = SecureRandom()
        random.nextBytes(IV)

        // Initialize Cipher for ENCRYPT_MODE for encrypt plaintext
        cipher.init(Cipher.ENCRYPT_MODE, key)

        // Perform Encryption
        val cipherText = cipher.doFinal(arrayBytes)
        IV = cipher.iv
        val cipherArray = ByteArray(IV.size + cipherText.size)
        System.arraycopy(IV, 0, cipherArray, 0, IV.size)
        System.arraycopy(cipherText, 0, cipherArray, IV.size, cipherText.size)
        return cipherArray
    }

    @Throws(Exception::class)
    fun decrypt(cipherArray: ByteArray): ByteArray {
        // Get Cipher Instance based on selective AES algorithm
        val cipher =
            Cipher.getInstance(TRANSFORMATION_AES_GCM_NOPADDING)

        // Get the key previously stored in the key store
        key = (myKeyStore!!.getEntry(
            ALIAS_KEY,
            null
        ) as KeyStore.SecretKeyEntry).secretKey

        // Create GCMParameterSpec for key
        val IV =
            ByteArray(GCM_IV_LENGTH) //here is issue
        System.arraycopy(
            cipherArray,
            0,
            IV,
            0,
            GCM_IV_LENGTH
        )
        val somenteDados = ByteArray(cipherArray.size - IV.size)
        System.arraycopy(cipherArray, IV.size, somenteDados, 0, somenteDados.size)
        val gcmParameterSpec =
            GCMParameterSpec(GCM_TAG_LENGTH * 8, IV)

        // Initialize Cipher for DECRYPT_MODE to in plain text
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)

        // Perform Decryption on encrypted text

        //return new String(decryptedText);
        return cipher.doFinal(somenteDados)
    }

    companion object {
        /**
         * Android KeyStore type.
         */
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

        /**
         * Alias for the application AES key.
         */
        private const val ALIAS_KEY = "my_key"
        const val AES_KEY_SIZE = 256
        const val GCM_IV_LENGTH = 12
        const val GCM_TAG_LENGTH = 16
        private const val TRANSFORMATION_AES_GCM_NOPADDING = "AES/GCM/NoPadding"
        private const val ALGORITHM_AES = "AES"
    }
}