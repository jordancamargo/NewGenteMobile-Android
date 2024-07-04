package br.com.lg.MyWay.newgentemobile.compartilhado.utils


import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android.LGApplication
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.CriptoAesGcmCipher
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.CriptografiaUtil
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.KeystoreManager
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.KeystoreManagerException
import br.com.lg.MyWay.newgentemobile.autenticacao.dominio.LoginHelper
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream


object Armazenamento {
    fun armazene(contexto: Context?, chave: String?, valor: String?) {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        val editor = mySharedPreferences.edit()
        editor.putString(chave, valor)
        editor.apply()
    }

    fun armazene(contexto: Context?, chave: String?, valor: Int) {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        val editor = mySharedPreferences.edit()
        editor.putInt(chave, valor)
        editor.apply()
    }

    fun armazene(contexto: Context?, chave: String?, valor: Boolean) {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        val editor = mySharedPreferences.edit()
        editor.putBoolean(chave, valor)
        editor.apply()
    }

    fun contem(contexto: Context?, chave: String?): Boolean {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        return mySharedPreferences.contains(chave)
    }

    @JvmOverloads
    fun recupereString(contexto: Context?, chave: String?, valorDefault: String? = null): String? {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        return mySharedPreferences.getString(chave, valorDefault)
    }

    fun recuperarInt(contexto: Context?, chave: String?, valorDefault: Int): Int {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        return mySharedPreferences.getInt(chave, valorDefault)
    }

    fun salvarInt(contexto: Context?, chave: String?, valor: Int) {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        mySharedPreferences.edit().putInt(chave, valor).commit()
    }

    @JvmOverloads
    fun recupereBoolean(
        contexto: Context?,
        chave: String?,
        valorDefault: Boolean = false
    ): Boolean {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        return mySharedPreferences.getBoolean(chave, valorDefault)
    }

    fun remova(contexto: Context?, chave: String?) {
        val mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            contexto
                ?: LGApplication.instance!!.baseContext
        )
        val editor = mySharedPreferences.edit()
        editor.remove(chave)
        editor.apply()
    }

    fun salvarDadosPortal(context: Context, dadosPortal: LoginHelper.DadosPortal) {
        escreverObjeto(context, LoginHelper.DadosPortal::class.java.name, dadosPortal)
    }

    fun obterDadosPortalSalvos(context: Context): LoginHelper.DadosPortal? {
        return lerObjeto(context, LoginHelper.DadosPortal::class.java.name) as LoginHelper.DadosPortal?
    }

    fun armazeneTenant(context: Context?, tenant: String?) {
        armazene(context, "tenant", tenant)
    }

    fun recupereTenant(context: Context?): String? {
        return recupereString(context, "tenant", null)
    }

    fun salvarValoresCamposAutenticacao(
        context: Context,
        valoresAutenticacao: HashMap<String, String?>
    ) {
        KeystoreManager.init(context)
        val criptografiaUtil = CriptografiaUtil
        val valoresAutenticacaoCriptografados: MutableMap<String, String?> = HashMap()
        val keySet: Set<String> = valoresAutenticacao.keys
        for (key in keySet) {
            val valor = valoresAutenticacao[key]
            var valorCriptografado: String? = null
            try {
                valorCriptografado = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    KeystoreManager.instance!!.encryptText(valor.toString())
                } else {
                    criptografiaUtil.criptografarExtensaoAES(valor.toString())
                }
            } catch (e: KeystoreManagerException) {
                e.printStackTrace()
            }
            valoresAutenticacaoCriptografados[key] = valorCriptografado
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                armazene(
                    context,
                    "br.com.lg.myway_$key", valoresAutenticacaoCriptografados[key]
                )
            }
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            escreverObjeto(context, "br.com.lg.myway", valoresAutenticacaoCriptografados)
        }
    }

    fun excluirValoresCamposAutenticacao(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            armazene(context, "br.com.lg.myway_usuario", "")
            armazene(context, "br.com.lg.myway_senha", "")
        }
        escreverObjeto(context, "br.com.lg.myway", HashMap<String, ByteArray>())
    }

    fun obterValoresCamposAutenticacaoSalvos(context: Context): Map<String?, CharArray>? {
        return try {
            KeystoreManager.init(context)
            val criptografiaUtil = CriptografiaUtil
            var valoresAutenticacaoCriptografados: MutableMap<String?, String?>? = HashMap()
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                valoresAutenticacaoCriptografados =
                    lerObjeto(context, "br.com.lg.myway") as MutableMap<String?, String?>?
            } else {
                valoresAutenticacaoCriptografados!!["usuario"] =
                    recupereString(context, "br.com.lg.myway_usuario")
                valoresAutenticacaoCriptografados["senha"] =
                    recupereString(context, "br.com.lg.myway_senha")
            }
            val valoresAutenticacao: MutableMap<String?, CharArray> = HashMap()
            val keySet: Set<String?> = valoresAutenticacaoCriptografados!!.keys
            for (key in keySet) {
                var valorDescriptografado = ""
                if (valoresAutenticacaoCriptografados[key] is String) {
                    val valor = valoresAutenticacaoCriptografados[key]
                    valorDescriptografado = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ({
                        try {
                            KeystoreManager.instance!!.decryptText(valor.toString())
                        } catch (e: Exception) {
                            criptografiaUtil.descriptografarExtensaoAES(valor.toString())
                        }
                    })!! else ({
                        criptografiaUtil.descriptografarExtensaoAES(valor.toString())
                    })!!
                }
                valoresAutenticacao[key] = valorDescriptografado.toCharArray()
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                val mapValoresCampos: Map<String?, CharArray> = valoresAutenticacao
                var valuer = ""
                val it: Iterator<*> = mapValoresCampos.entries.iterator()
                while (it.hasNext()) {
                    val (key, value1) = it.next() as Map.Entry<*, *>
                    var value: String
                    val id = key as String
                    if (id == "serial") {
                        try {
                            value = value1 as String
                            valuer = value
                        } catch (e: Exception) {
                            value = String((value1 as CharArray?)!!)
                            valuer = value
                        }
                    }
                }
                if (valuer == Build.SERIAL) {
                    valoresAutenticacao
                } else {
                    excluirValoresCamposAutenticacao(context)
                    obterValoresCamposAutenticacaoSalvosSemCriptografia(context)
                }
            } else {
                valoresAutenticacao
            }
        } catch (e: Exception) {
            try {
                excluirValoresCamposAutenticacao(context)
                obterValoresCamposAutenticacaoSalvosSemCriptografia(context)
            } catch (ex: Exception) {
                FirebaseCrashlytics.getInstance().log(ex.message!!)
                excluirValoresCamposAutenticacao(context)
                HashMap<String?, CharArray>()
            }
        }
    }

    fun obterValoresCamposAutenticacaoSalvosSemCriptografia(context: Context): Map<String?, CharArray>? {
        return try {
            lerObjeto(context, "br.com.lg.myway") as HashMap<String?, CharArray>?
        } catch (e: Exception) {
            HashMap()
        }
    }

    private fun lerObjeto(context: Context, key: String): Any? {
        var `object`: Any? = null
        try {
            val fis = context.openFileInput(key)
            val cipher = CriptoAesGcmCipher()
            val arrayBytesCriptografado = readAllBytes(fis)
            val arrayBytesDescriptografado: ByteArray = cipher.decrypt(arrayBytesCriptografado)
            `object` = arrayToObjeto(arrayBytesDescriptografado)
            fis.close()
        } catch (e: Exception) {
            e.message
        }
        return `object`
    }

    private fun escreverObjeto(context: Context, key: String, `object`: Any) {
        try {
            val cipher = CriptoAesGcmCipher()
            val arrayBytesParaCriptorafia = objetoToArray(`object`)
            val arrayBytesCriptografado: ByteArray = cipher.encrypt(arrayBytesParaCriptorafia)
            val fos = context.openFileOutput(key, Context.MODE_PRIVATE)
            fos.write(arrayBytesCriptografado)
            fos.close()
        } catch (e: Exception) {
            e.message
        }
    }

    @Throws(Exception::class)
    private fun objetoToArray(obj: Any): ByteArray {
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(obj)
        oos.flush()
        return bos.toByteArray()
    }

    private fun arrayToObjeto(bytes: ByteArray): Any? {
        try {
            ByteArrayInputStream(bytes).use { bis ->
                ObjectInputStream(bis).use { `in` -> return `in`.readObject() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    private fun readAllBytes(`in`: InputStream): ByteArray {
        val out = ByteArrayOutputStream()
        copyAllBytes(`in`, out)
        return out.toByteArray()
    }

    @Throws(IOException::class)
    private fun copyAllBytes(`in`: InputStream, out: OutputStream): Int {
        var byteCount = 0
        val buffer = ByteArray(4096)
        while (true) {
            val read = `in`.read(buffer)
            if (read == -1) {
                break
            }
            out.write(buffer, 0, read)
            byteCount += read
        }
        return byteCount
    }
}

