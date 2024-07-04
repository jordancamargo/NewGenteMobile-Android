package br.com.lg.MyWay.newgentemobile.compartilhado.network

import br.com.lg.MyWay.newgentemobile.autenticacao.dados.ServicoDeLogin
import br.com.lg.MyWay.newgentemobile.configuracoes.ajustes.ui.componentes.Console
import com.google.gson.Gson
import java.io.Serializable
import java.math.BigInteger
import java.security.MessageDigest
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import java.util.Locale
import javax.net.ssl.X509TrustManager


/**
 * Created by claudivan.celestino on 15/03/2016.
 */
class ConexaoSeguraSSLValidador : Serializable {
    private val CHAVE_VALIDADORA = "gntec727-fc96-483f-913d-ffbdc76f72f3"
    private val modulusExponentPinTokenCache: MutableMap<String, String> = HashMap()
    private var pinToken1Recebido: String? = null
    private var pinToken1ListaRecebido: Array<String>? = null
    private val ACTION_PARA_IGNORAR_VALIDACAO1 = "produtos/mobile/autenticacao/" + ServicoDeLogin.SERVICO_CONFIGURACOESLOGIN

    fun setPinToken1Recebido(pinToken1Recebido: String?) {
        this.pinToken1Recebido = pinToken1Recebido
    }

    fun setPinToken1ListaRecebido(pinToken1ListaRecebido: String?) {
        this.pinToken1ListaRecebido = Gson().fromJson(
            pinToken1ListaRecebido,
            Array<String>::class.java
        )
    }

    fun obterTrustManagerAdequadoParaARequisicao(url: String): X509TrustManager {
        return if (url.lowercase(Locale.getDefault()).contains(ACTION_PARA_IGNORAR_VALIDACAO1)) {
            obterTrustManagerSemValidador()
        } else if (pinToken1Recebido == null || pinToken1Recebido!!.isEmpty()) {
            obterTrustManagerSemValidador()
        } else {
            obterTrustManagerComValidador()
        }
    }

    fun obterTrustManagerSemValidador(): X509TrustManager {
        return object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        }
    }

    fun obterTrustManagerComValidador(): X509TrustManager {
        return object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

            @Throws(CertificateException::class)
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {
                val publicKey = certs[0].publicKey as RSAPublicKey
                val pinTokenGerado = gerarPinTokenRequisicao(publicKey)
                if (pinToken1ListaRecebido != null && pinToken1ListaRecebido!!.size > 0) {
                    if (!listOf<String?>(*pinToken1ListaRecebido!!).contains(pinTokenGerado)) {
                        Console.setLog("EVENTO:Certificado dieferente do esperado")
                        Console.setLog("Pin recebido: " + pinToken1ListaRecebido.toString())
                        throw CertificateException()
                    }
                } else {
                    if (pinTokenGerado != pinToken1Recebido) {
                        Console.setLog("EVENTO:Certificado dieferente do esperado")
                        Console.setLog("Pin recebido: $pinToken1Recebido")
                        throw CertificateException()
                    }
                }
            }
        }
    }

    private fun gerarPinTokenRequisicao(publicKey: RSAPublicKey): String? {
        val modulus = publicKey.modulus.toByteArray()
        val exponent = publicKey.publicExponent.toByteArray()
        val modulusExponent = BigInteger(1, modulus).toString() + BigInteger(1, exponent).toString()

        // cache
        if (modulusExponentPinTokenCache.containsKey(modulusExponent)) {
            return modulusExponentPinTokenCache[modulusExponent]
        }
        //
        val modulosExponentChaveValidadora = modulusExponent + CHAVE_VALIDADORA
        var hash: ByteArray? = null
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            hash = digest.digest(modulosExponentChaveValidadora.toByteArray(charset("UTF-8")))
        } catch (e: Exception) {
        }
        val pinTokenRequisicao = BigInteger(1, hash).toString()

        // cache
        modulusExponentPinTokenCache[modulusExponent] = pinTokenRequisicao
        //
        return pinTokenRequisicao
    }

    companion object {
        var instancia: ConexaoSeguraSSLValidador? = null
            get() {
                if (field == null) {
                    field = ConexaoSeguraSSLValidador()
                }
                return field
            }
    }
}