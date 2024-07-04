package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ArquivoUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.InvalidKeyException
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException


class EncriptaDecriptaRSA {
    var imagemLogoFile: File = ArquivoUtils.fileImagemLogoCustomizada

    companion object {
        const val ALGORITHM = "RSA"

        /**
         * Local da chave privada no sistema de arquivos.
         */
        val PATH_CHAVE_PRIVADA: String =
            ArquivoUtils.diretorioDeCache.absolutePath + "/private.key"

        /**
         * Local da chave pública no sistema de arquivos.
         */
        val PATH_CHAVE_PUBLICA: String =
            ArquivoUtils.diretorioDeCache.absolutePath + "/public.key"

        /**
         * Gera a chave que contém um par de chave Privada e Pública usando 1025 bytes.
         * Armazena o conjunto de chaves nos arquivos private.key e public.key
         */
        fun geraChave() {
            try {
                val keyGen = KeyPairGenerator.getInstance(ALGORITHM)
                keyGen.initialize(1024)
                val key = keyGen.generateKeyPair()
                val chavePrivadaFile = File(PATH_CHAVE_PRIVADA)
                val chavePublicaFile = File(PATH_CHAVE_PUBLICA)

                // Cria os arquivos para armazenar a chave Privada e a chave Publica
                if (chavePrivadaFile.parentFile != null) {
                    chavePrivadaFile.parentFile.mkdirs()
                }
                chavePrivadaFile.createNewFile()
                if (chavePublicaFile.parentFile != null) {
                    chavePublicaFile.parentFile.mkdirs()
                }
                chavePublicaFile.createNewFile()

                // Salva a Chave Pública no arquivo
                val chavePublicaOS = ObjectOutputStream(
                    FileOutputStream(chavePublicaFile)
                )
                chavePublicaOS.writeObject(key.public)
                chavePublicaOS.close()

                // Salva a Chave Privada no arquivo
                val chavePrivadaOS = ObjectOutputStream(
                    FileOutputStream(chavePrivadaFile)
                )
                chavePrivadaOS.writeObject(key.private)
                chavePrivadaOS.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * Verifica se o par de chaves Pública e Privada já foram geradas.
         */
        fun verificaSeExisteChavesNoSO(): Boolean {
            val chavePrivada = File(PATH_CHAVE_PRIVADA)
            val chavePublica = File(PATH_CHAVE_PUBLICA)
            return if (chavePrivada.exists() && chavePublica.exists()) {
                true
            } else false
        }

        /**
         * Criptografa o texto puro usando chave pública.
         */
        fun criptografa(texto: String, chave: PublicKey?): ByteArray? {
            var cipherText: ByteArray? = null
            try {
                val cipher = Cipher.getInstance(ALGORITHM)
                // Criptografa o texto puro usando a chave Púlica
                cipher.init(Cipher.ENCRYPT_MODE, chave)
                cipherText = cipher.doFinal(texto.toByteArray())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return cipherText
        }

        @Throws(
            IllegalBlockSizeException::class,
            BadPaddingException::class,
            InvalidKeyException::class,
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class
        )
        fun encryptWithPubKey(input: ByteArray?, key: PublicKey?): ByteArray {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            return cipher.doFinal(input)
        }

        /**
         * Decriptografa o texto puro usando chave privada.
         */
        fun decriptografa(texto: ByteArray?, chave: PrivateKey?): String {
            var dectyptedText: ByteArray? = null
            try {
                val cipher = Cipher.getInstance(ALGORITHM)
                // Decriptografa o texto puro usando a chave Privada
                cipher.init(Cipher.DECRYPT_MODE, chave)
                dectyptedText = cipher.doFinal(texto)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return String(dectyptedText!!)
        }

        /**
         * Testa o Algoritmo
         */
        @JvmStatic
        fun main(args: Array<String>) {
            try {

                // Verifica se já existe um par de chaves, caso contrário gera-se as chaves..
                if (!verificaSeExisteChavesNoSO()) {
                    // Método responsável por gerar um par de chaves usando o algoritmo RSA e
                    // armazena as chaves nos seus respectivos arquivos.
                    geraChave()
                }
                val msgOriginal = "Exemplo de mensagem"
                var inputStream: ObjectInputStream? = null

                // Criptografa a Mensagem usando a Chave Pública
                inputStream = ObjectInputStream(FileInputStream(PATH_CHAVE_PUBLICA))
                val chavePublica = inputStream.readObject() as PublicKey
                val textoCriptografado = criptografa(msgOriginal, chavePublica)

                // Decriptografa a Mensagem usando a Chave Pirvada
                inputStream = ObjectInputStream(FileInputStream(PATH_CHAVE_PRIVADA))
                val chavePrivada = inputStream.readObject() as PrivateKey
                val textoPuro = decriptografa(textoCriptografado, chavePrivada)

                // Imprime o texto original, o texto criptografado e
                // o texto descriptografado.
                println("Mensagem Original: $msgOriginal")
                println("Mensagem Criptografada: " + textoCriptografado.toString())
                println("Mensagem Decriptografada: $textoPuro")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

