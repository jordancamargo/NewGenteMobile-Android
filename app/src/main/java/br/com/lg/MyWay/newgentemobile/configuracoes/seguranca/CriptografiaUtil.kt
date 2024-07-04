package br.com.lg.MyWay.newgentemobile.configuracoes.seguranca

import android.util.Base64
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android.LGApplication
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ConfiguracoesVindasDoServidorUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ExceptionUtils
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.EncriptaDecriptaRSA.Companion.encryptWithPubKey
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.keytostring.keyToString
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object CriptografiaUtil {
    private val AES: String = LGApplication.instance!!.applicationContext.getString(R.string.type)

    //============== CRIPTOGRAFIA COM AES ==============//
    private fun obterKeyAES(): SecretKey? {
        try {
            val key = obterChaveAES()
            return SecretKeySpec(key, AES)
        } catch (e: Exception) {
        }
        return null
    }

    private fun obterChaveAES(): ByteArray {
        return byteArrayOf(71, 110, 116, 67, 53, 52, 51, 50, 49, 66, 68, 99, 57, 54, 53, 49)
    }

    //TODO:Chave criptofraia extensao
    private fun obterKeyExtensaoAES(): SecretKey? {
        try {
            return SecretKeySpec("ExtW52990BFc8856".toByteArray(charset("UTF-8")), AES)
        } catch (e: Exception) {
        }
        return null
    }

    fun criptografarExtensaoAES(valor: String): String? {
        return criptografarAES(valor, obterKeyExtensaoAES())
    }

    fun criptografarExtensaoAESExtensao(valor: String): String {
        return valor
    }

    fun criptografarAES(valor: String): String? {
        return if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) < GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) criptografarAES(valor, obterKeyAES()) else criptografarRSA(valor, obterKeyAES())
    }

    private fun criptografarAES(valor: String, key: SecretKey?): String? {
        var retorno: String? = null
        try {
            val valorBytes = valor.toByteArray(charset("UTF-8"))
            val cipher = Cipher.getInstance(AES)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val bytTextoCriptografado = cipher.doFinal(valorBytes)
            retorno = Base64.encodeToString(bytTextoCriptografado, Base64.DEFAULT)
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        return retorno
    }

    private fun criptografarRSA(valor: String, key: SecretKey?): String? {
        var retorno: String? = null
        try {
            val keystring: String = keyToString(key!!)
            val publicKey =
                toStringkey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArYSXBLQlysGReMUDdLxA TmJvOMwxYqnLp302qEU/JI7QbEKZOBnKZN8ANS0O7MTqOT3QWsjwDZnOABgL25pF SsuCVqCV/UcqXSDZ4pmbAKxHMlalkqHBcSmCuvJ4yYyzzQvQ96GPD64F81l2oHZr xvmTkiZSWu3pM647VcCWePXBWTIx8fIKeLZyEXh7S5L2Aj7m7zWmaTGj4mYqflYW zRwHa8OGw+yIdiWf/h59LHVNWCNxR82E4C4INBFQjspgv0ehga+jrQ7HAwETNDFq MpQeV3R/eQnPiL0O3lS0LL54JW3ivHDlg0b0ACltupZXEZ4i3HvSKfQyktxbm6qP XQIDAQAB")
            val valorBytes = valor.toByteArray(charset("UTF-8"))
            val cipher = Cipher.getInstance(AES)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val bytTextoCriptografado = cipher.doFinal(valorBytes)
            val textoCriptografado: ByteArray = encryptWithPubKey(key!!.encoded, publicKey)
            val chave = Base64.encodeToString(textoCriptografado, Base64.DEFAULT)
            val valorjson = Base64.encodeToString(bytTextoCriptografado, Base64.DEFAULT)
            retorno = "$chave.$valorjson"
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        return retorno
    }

    //TODO:TIPO de descriptografia extensao
    fun descriptografarExtensaoAES(valor: String): String? {
        return descriptografarAES(valor, obterKeyExtensaoAES(), null, AES)
    }

    fun descriptografarExtensaoAESExtensao(valor: String): String {
        return valor
    }

    fun descriptografarAES(valor: String): String? {
        return descriptografarAES(valor, obterKeyAES(), null, AES)
    }

    private fun descriptografarAES(
        valor: String,
        key: SecretKey?,
        ivSpec: IvParameterSpec?,
        tipoCriptografia: String
    ): String? {
        var retorno: String? = null
        try {
            retorno = if (tipoCriptografia == AES) {
                val valorBytes = Base64.decode(valor, Base64.DEFAULT)
                val dcipher = Cipher.getInstance(tipoCriptografia)
                dcipher.init(Cipher.DECRYPT_MODE, key)
                val bytTextoDescriptografado = dcipher.doFinal(valorBytes)
                String(bytTextoDescriptografado, charset("UTF-8"))
            } else {
                val valorBytes = decode(valor)
                val dcipher = Cipher.getInstance(tipoCriptografia)
                dcipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
                val bytTextoDescriptografado = dcipher.doFinal(valorBytes)
                String(bytTextoDescriptografado, charset("UTF-8"))
            }
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        return retorno
    }

    fun decode(arg: String): ByteArray {
        var s = arg
        s = s.replace("-", "+")
        // 62nd char of encoding
        s = s.replace("_", "/")
        when (s.length % 4) {
            0 -> {}
            2 -> s += "=="
            3 -> s += "="
        }
        return Base64.decode(s, Base64.DEFAULT)
    }

    fun toStringkey(key: String?): PublicKey? {
        val publicBytes = Base64.decode(key, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(publicBytes)
        var keyFactory: KeyFactory? = null
        try {
            keyFactory = KeyFactory.getInstance("RSA")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return try {
            keyFactory!!.generatePublic(keySpec)
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
            null
        }
    }
}