package br.com.lg.MyWay.newgentemobile.compartilhado.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.AsyncTask
import android.util.Base64
import android.widget.ImageView
import br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.ConsultaImagemHeaderListener
import br.com.lg.MyWay.newgentemobile.compartilhado.network.ConexaoSeguraSSLValidador
import br.com.lg.MyWay.newgentemobile.configuracoes.demonstracao.dados.DemoUtils
import org.apache.http.conn.ssl.SSLSocketFactory
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object ImageUtils {
    fun downloadImagem(imageView: ImageView?, urlImage: String?) {
        if (urlImage != null) {
            DownloadImage(imageView, urlImage).execute()
        }
    }

    fun downloadImagemCapa(listener: ConsultaImagemHeaderListener?, urlImage: String?) {
        if (urlImage != null) {
            DownloadImage(listener, urlImage).execute()
        }
    }

    fun getBitmapFromURLWithScale(src: String?): Bitmap? {
        return try {
            val tm: X509TrustManager =
                ConexaoSeguraSSLValidador.instancia?.obterTrustManagerSemValidador() ?: throw NullPointerException("Instância de ConexaoSeguraSSLValidador é nula")
            var myBitmap: Bitmap?
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(tm), null)
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
            HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
            val url = URL(src)
            val connection = if (url.protocol.equals(
                    "https",
                    ignoreCase = true
                )
            ) url.openConnection() as HttpsURLConnection else (url.openConnection() as HttpURLConnection)


            // Incluído devido erro ao redirecionar para outras urls
            if (connection.responseCode == 302) {
                val tmnew: X509TrustManager =
                    ConexaoSeguraSSLValidador.instancia?.obterTrustManagerSemValidador() ?: throw NullPointerException("Instância de ConexaoSeguraSSLValidador é nula.")
                val scnew = SSLContext.getInstance("TLS")
                scnew.init(null, arrayOf<TrustManager>(tmnew), null)
                HttpsURLConnection.setDefaultSSLSocketFactory(scnew.socketFactory)
                HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                val urlstring = connection.getHeaderField("Location")
                // Busca nova url em caso de redirecionamento
                val newUrl = URL(urlstring)
                val newconnection = if (newUrl.protocol.equals(
                        "https",
                        ignoreCase = true
                    )
                ) newUrl.openConnection() as HttpsURLConnection else (newUrl.openConnection() as HttpURLConnection)
                val inputnew = newconnection.inputStream
                myBitmap = BitmapFactory.decodeStream(inputnew)
                inputnew.close()
                if (myBitmap == null) {
                    val urlstringhttps = connection.getHeaderField("Location")
                    // Busca nova url em caso de redirecionamento
                    val newUrlhttps = URL(urlstringhttps.replace("http", "https"))
                    val newconnectionHttps = if (newUrlhttps.protocol.equals(
                            "https",
                            ignoreCase = true
                        )
                    ) newUrlhttps.openConnection() as HttpsURLConnection else (newUrlhttps.openConnection() as HttpURLConnection)
                    val inputnewhttps = newconnectionHttps.inputStream
                    myBitmap = BitmapFactory.decodeStream(inputnewhttps)
                    inputnew.close()
                }
            } else {
                val input = connection.inputStream
                myBitmap = BitmapFactory.decodeStream(input)
                input.close()
            }
            myBitmap
        } catch (e: Exception) {
            null
        }
    }

    fun encodeToBase64(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun salvarTemaDeCores(context: Context?, cores: Array<String>) {
        Armazenamento.armazene(context, "Cores", converterArrayDeStringParaString(cores))
    }

    fun converterArrayDeStringParaString(cores: Array<String>): String {
        return cores[0] + "," + cores[1] + "," + cores[2]
    }

    fun getCoresPorChave(context: Context?, chave: String?): Array<String>? {
        val cores = Armazenamento.recupereString(context, chave)
        return cores?.split(",".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
    }

    fun getCoresDoTema(context: Context?): Array<String>? {
        return getCoresPorChave(context, "Cores")
    }

    fun salvarImagemLogoNoDefaults(activity: Activity?, imagem: Bitmap) {
        Armazenamento.armazene(activity, "ImagemLogo", encodeToBase64(imagem))
    }

    fun getImagemDaLogo(activity: Activity?): Bitmap? {
        val imagem = Armazenamento.recupereString(activity, "ImagemLogo")
        return if (imagem != null) {
            val decodedByte = Base64.decode(imagem, 0)
            BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        } else {
            null
        }
    }

    fun hasImagemLogo(activity: Activity?): Boolean {
        return Armazenamento.contem(activity, "ImagemLogo")
    }

    fun hasImagemLogoPersonalizacaoDemonstracao(context: Context?): Boolean {
        return Armazenamento.contem(context, DemoUtils.IMAGEM_LOGO_PERSONALIZADA)
    }

    fun hasImagemCapaPersonalizacaoDemonstracao(activity: Activity?): Boolean {
        return Armazenamento.contem(activity, DemoUtils.IMAGEM_CAPA_PERSONALIZADA)
    }

    fun hasTema(activity: Activity?): Boolean {
        return Armazenamento.contem(activity, "Cores")
    }

    fun hasTemaPersonalizadoDemonstracao(activity: Activity?): Boolean {
        return Armazenamento.contem(activity, DemoUtils.TEMA_PERSONALIZADO)
    }

    fun customizarBotaoBarraDeNavegacao(bitmap: Bitmap, cores: Array<String>?): Bitmap {
        return customizarCor(bitmap, cores)
    }

    fun customizarCor(bitmap: Bitmap, cores: Array<String>): Bitmap {
        val canvas = Canvas()
        val resultado = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(resultado)
        val paint = Paint()
        paint.isFilterBitmap = true

        // Color
        val cor1 = cores[0].toInt()
        val cor2 = cores[1].toInt()
        val cor3 = cores[2].toInt()
        val matrizDeCores = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, cor1.toFloat(),
                0f, 1f, 0f, 0f, cor2.toFloat(),
                0f, 0f, 1f, 0f, cor3.toFloat(),
                0f, 0f, 0f, 1f, 0f
            )
        )
        paint.setColorFilter(ColorMatrixColorFilter(matrizDeCores))
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return resultado
    }

    private open class DownloadImage : AsyncTask<Void?, Void?, Bitmap?> {
        private var imageView: ImageView? = null
        private var listener: ConsultaImagemHeaderListener? = null
        private var url: String

        constructor(imageView: ImageView?, url: String) {
            this.imageView = imageView
            this.url = url
        }

        constructor(listener: ConsultaImagemHeaderListener?, url: String) {
            this.listener = listener
            this.url = url
        }


        override fun doInBackground(vararg params: Void?): Bitmap? {
            return downloadImage(url)
        }

        override fun onPostExecute(image: Bitmap?) {
            if (imageView != null) {
                if (image != null) {
                    imageView!!.setImageBitmap(image)
                }
            } else if (listener != null) {
                if (image != null) {
                    listener?.onSucessoConsultarImagem(image)
                } else {
                    //this.listener.onFalhaConsultarImagem("Houve um erro ao tentar obter a imagem da capa");
                }
            }
        }

        private fun downloadImage(_url: String): Bitmap? {
            try {
                return getBitmapFromURLWithScale(_url)
            } catch (_: Exception) {
            }
            return null
        }
    }
}

