package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.InvalidKeyException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.PrivateKey
import java.security.UnrecoverableEntryException
import java.security.cert.CertificateException
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.NoSuchPaddingException
import javax.security.auth.x500.X500Principal


/**
 * Created by xcelder1 on 11/7/16.
 */
class KeystoreManager(context: Context) {
    /**
     * @return Your KeyStore instance with the Alias you defined
     */
    var keyStore: KeyStore? = null
    var keyPair: KeyPair? = null
    private val sharedPreferences: SharedPreferences
    fun getPreference(key: String): Any {
        var key = key
        return try {
            key = encryptText(key)
            val value = sharedPreferences.getString(key, "")
            if ("" == value) value else decryptText(value)
        } catch (e: KeystoreManagerException) {
            ""
        }
    }

    fun setPreference(key: String, value: String) {
        var key = key
        var value = value
        try {
            value = encryptText(value)
            key = encryptText(key)
            sharedPreferences.edit().putString(key, value).commit()
        } catch (e: KeystoreManagerException) {
            e.printStackTrace()
        }
    }

    fun removePreferences() {
        sharedPreferences.edit().clear().apply()
    }

    init {
        sharedPreferences = context.getSharedPreferences("KeyStore", 0)
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_NAME)
            keyStore?.load(null)
        } catch (e: KeyStoreException) {
            throw KeystoreManagerException(e.message)
        } catch (e: CertificateException) {
            throw KeystoreManagerException(e.message)
        } catch (e: NoSuchAlgorithmException) {
            throw KeystoreManagerException(e.message)
        } catch (e: IOException) {
            throw KeystoreManagerException(e.message)
        }
        try {
            // Create new key if needed
            if (keyStore?.containsAlias(ALIAS) == false) {
                val generator = KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA, KEYSTORE_NAME)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val spec = KeyGenParameterSpec.Builder(
                        ALIAS,
                        KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT
                    )
                        .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()
                    generator.initialize(spec)
                } else {
                    val start = Calendar.getInstance()
                    val end = Calendar.getInstance()
                    end.add(Calendar.YEAR, 1)
                    var spec: KeyPairGeneratorSpec? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        spec = KeyPairGeneratorSpec.Builder(context)
                            .setAlias(ALIAS)
                            .setSubject(X500Principal("CN=Sample Name, O=Android Authority"))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(start.time)
                            .setEndDate(end.time)
                            .build()
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        generator.initialize(spec)
                    }
                }
                keyPair = generator.generateKeyPair()
            }
        } catch (e: Exception) {
            throw KeystoreManagerException(e.message)
        }
    }

    /**
     * Method for encrypt a text with your own Android KeyStore
     *
     * @param txt The text you want to encrypt
     * @return String with your text finally encrypted
     * @throws KeystoreManagerException
     */
    @Throws(KeystoreManagerException::class)
    fun encryptText(txt: String): String {
        var encryptedText = ""
        return try {
            val publicKey = keyStore!!.getCertificate(ALIAS).publicKey

            // Encrypt the text
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (txt.isEmpty()) {
                    throw KeystoreManagerException(KeystoreManagerException.EXCEPTION_EMPTY_TEXT)
                }
            }
            val input: Cipher
            input = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Cipher.getInstance(
                ALGORITHM_M, PROVIDER_M
            ) else Cipher.getInstance(ALGORITHM, PROVIDER)
            input.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(outputStream, input)
            cipherOutputStream.write(txt.toByteArray(charset("UTF-8")))
            cipherOutputStream.close()
            val vals = outputStream.toByteArray()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                encryptedText = Base64.encodeToString(vals, Base64.DEFAULT)
            }
            encryptedText
        } catch (e: Exception) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        }
    }

    /**
     * Method for decrypt a text with your own Android KeyStore
     *
     * @param txt The text you want to decrypt
     * @return String with your text finally decrypted
     * @throws KeystoreManagerException
     */
    @Throws(KeystoreManagerException::class)
    fun decryptText(txt: String?): String {
        var decryptedText = ""
        return try {
            val privateKey = keyStore!!.getKey(ALIAS, null) as PrivateKey
            val output: Cipher
            try {
                output =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Cipher.getInstance(
                        ALGORITHM_M, PROVIDER_M
                    ) else Cipher.getInstance(ALGORITHM, PROVIDER)
                output.init(Cipher.DECRYPT_MODE, privateKey)
                var cipherInputStream: CipherInputStream? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    cipherInputStream = CipherInputStream(
                        ByteArrayInputStream(Base64.decode(txt, Base64.DEFAULT)), output
                    )
                }
                val values = ArrayList<Byte>()
                var nextByte: Int
                while (cipherInputStream!!.read().also { nextByte = it } != -1) {
                    values.add(nextByte.toByte())
                }
                val bytes = ByteArray(values.size)
                for (i in bytes.indices) {
                    bytes[i] = values[i]
                }
                decryptedText = String(bytes, 0, bytes.size, charset("UTF-8"))
                decryptedText
            } catch (e: NoSuchAlgorithmException) {
                throw KeystoreManagerException(e.message)
            } catch (e: NoSuchPaddingException) {
                throw KeystoreManagerException(e.message)
            } catch (e: IOException) {
                throw KeystoreManagerException(e.message)
            } catch (e: NoSuchProviderException) {
                throw KeystoreManagerException(e.message)
            } catch (e: InvalidKeyException) {
                throw KeystoreManagerException(e.message)
            }
        } catch (e: NoSuchAlgorithmException) {
            throw KeystoreManagerException(e.message)
        } catch (e: UnrecoverableEntryException) {
            throw KeystoreManagerException(e.message)
        } catch (e: KeyStoreException) {
            throw KeystoreManagerException(e.message)
        }
    }

    /**
     * Method for encrypt a byte array with your own Android KeyStore
     *
     * @param bytes The byte array you want to encrypt
     * @return Byte array with your bytes finally encrypted
     * @throws KeystoreManagerException
     */
    @Throws(KeystoreManagerException::class)
    fun encryptBytes(bytes: ByteArray): ByteArray {
        return try {
            val publicKey = keyStore!!.getCertificate(ALIAS).publicKey

            // Encrypt the text
            if (bytes.size <= 0) {
                throw KeystoreManagerException(KeystoreManagerException.EXCEPTION_EMPTY_TEXT)
            }
            val input: Cipher
            input = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Cipher.getInstance(
                ALGORITHM_M, PROVIDER_M
            ) else Cipher.getInstance(ALGORITHM, PROVIDER)
            input.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(outputStream, input)
            cipherOutputStream.write(bytes)
            cipherOutputStream.close()
            outputStream.toByteArray()
        } catch (e: IOException) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        } catch (e: NoSuchAlgorithmException) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        } catch (e: InvalidKeyException) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        } catch (e: NoSuchPaddingException) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        } catch (e: NoSuchProviderException) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        } catch (e: KeystoreManagerException) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        } catch (e: KeyStoreException) {
            throw KeystoreManagerException("Exception " + e.message + " occured")
        }
    }

    /**
     * Method for decrypt a byte array with your own Android KeyStore
     *
     * @param bytes The byte array you want to decrypt
     * @return Byte array with your bytes finally decrypted
     * @throws KeystoreManagerException
     */
    @Throws(KeystoreManagerException::class)
    fun decryptBytes(bytes: ByteArray?): ByteArray {
        return try {
            val privateKey = keyStore!!.getKey(ALIAS, null) as PrivateKey
            val output: Cipher
            try {
                output =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Cipher.getInstance(
                        ALGORITHM_M, PROVIDER_M
                    ) else Cipher.getInstance(ALGORITHM, PROVIDER)
                //}
                output.init(Cipher.DECRYPT_MODE, privateKey)
                val cipherInputStream = CipherInputStream(
                    ByteArrayInputStream(bytes), output
                )
                val values = ArrayList<Byte>()
                var nextByte: Int
                while (cipherInputStream.read().also { nextByte = it } != -1) {
                    values.add(nextByte.toByte())
                }
                val result = ByteArray(values.size)
                for (i in result.indices) {
                    result[i] = values[i]
                }
                result
            } catch (e: NoSuchAlgorithmException) {
                throw KeystoreManagerException(e.message)
            } catch (e: NoSuchPaddingException) {
                throw KeystoreManagerException(e.message)
            } catch (e: IOException) {
                throw KeystoreManagerException(e.message)
            } catch (e: NoSuchProviderException) {
                throw KeystoreManagerException(e.message)
            } catch (e: InvalidKeyException) {
                throw KeystoreManagerException(e.message)
            }
        } catch (e: NoSuchAlgorithmException) {
            throw KeystoreManagerException(e.message)
        } catch (e: UnrecoverableEntryException) {
            throw KeystoreManagerException(e.message)
        } catch (e: KeyStoreException) {
            throw KeystoreManagerException(e.message)
        }
    }

    /**
     * @return A KeyPair with an asymmetric pair of public and private keys generated by the KeyStore instance that was decided with the Alias you have chosen
     * @throws KeystoreManagerException
     */
    @Throws(KeystoreManagerException::class)
    fun getKeyPair(): KeyPair {
        return try {
            val privateKeyEntry = keyStore!!.getEntry(ALIAS, null) as KeyStore.PrivateKeyEntry
            KeyPair(privateKeyEntry.certificate.publicKey, privateKeyEntry.privateKey)
        } catch (e: NoSuchAlgorithmException) {
            throw KeystoreManagerException(e.message)
        } catch (e: UnrecoverableEntryException) {
            throw KeystoreManagerException(e.message)
        } catch (e: KeyStoreException) {
            throw KeystoreManagerException(e.message)
        }
    }

    companion object {
        const val ALIAS =
            "YOUR_ALIAS" //Enter your alias here (only a name for the key pair instance)
        const val KEY_ALGORITHM_RSA = "RSA"
        const val KEYSTORE_NAME = "AndroidKeyStore"
        private const val ALGORITHM = "RSA/ECB/PKCS1Padding"
        private const val ALGORITHM_M = "RSA/None/PKCS1Padding"
        private const val PROVIDER = "AndroidOpenSSL"
        private const val PROVIDER_M = "AndroidKeyStoreBCWorkaround"
        private val key = "MyDifficultPassw".toByteArray()
        var instance: KeystoreManager? = null
            private set

        fun init(context: Context) {
            try {
                instance = KeystoreManager(context)
            } catch (e: KeystoreManagerException) {
                e.printStackTrace()
            }
        }
    }
}
