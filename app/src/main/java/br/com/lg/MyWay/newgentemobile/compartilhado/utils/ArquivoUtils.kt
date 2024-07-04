package br.com.lg.MyWay.newgentemobile.compartilhado.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android.LGApplication
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao
//import br.com.lg.MyWay.newgentemobile.compartilhado.ui.componentes.InfoToast
//import br.com.lg.MyWay.newgentemobile.funcionalidades.relatorios.dados.models.Relatorio
import com.google.android.gms.common.util.IOUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.URLConnection
import java.util.Locale
import java.util.Scanner


object ArquivoUtils {
    private val IMAGEM_LOGO_URL_KEY = "IMAGEM_LOGO_URL_KEY"
    private val IMAGEM_FUNDO_URL_KEY = "IMAGEM_FUNDO_URL_KEY"
    fun obtemArquivo(nomeDoArquivo: String, extensao: String, contexto: Context): File {
        return obtemArquivo("", nomeDoArquivo, extensao, contexto)
    }

    fun obtemArquivo(
        path: String,
        nomeDoArquivo: String,
        extensao: String,
        contexto: Context
    ): File {
        var `is`: InputStream? = null
        try {
            `is` = contexto.resources.assets.open(path + nomeDoArquivo + extensao)
        } catch (e: IOException) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        return converteStreamParaArquivo(`is`, nomeDoArquivo, extensao)
    }

    fun converteStreamParaString(`is`: InputStream): String {
        val sb = StringBuilder()
        try {
            var line: String? = null
            val reader = BufferedReader(InputStreamReader(`is`))
            while (reader.readLine().also { line = it } != null) {
                sb.append(line + "\n")
            }
        } catch (e: IOException) {
            ExceptionUtils.printStackTraceIfDebug(e)
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                ExceptionUtils.printStackTraceIfDebug(e)
            }
        }
        return sb.toString()
    }

    fun converteStreamParaArquivo(
        `is`: InputStream?,
        nomeDoArquivo: String,
        extensao: String
    ): File {
        var extensao = extensao
        if (!extensao.startsWith(".")) extensao = ".$extensao"
        val path: File = File(Sessao.applicationContext!!.filesDir, "MyWay")
        if (!path.exists()) {
            path.mkdir()
        }
        val f = File(path, nomeDoArquivo + extensao)
        f.deleteOnExit()
        return converteStreamParaArquivo(`is`, f)
    }

    fun converteStreamParaArquivo(`is`: InputStream?, f: File): File {
        val os: OutputStream
        try {
            os = FileOutputStream(f)
            val buf = ByteArray(1024)
            var len: Int
            while (`is`!!.read(buf).also { len = it } > 0) os.write(buf, 0, len)
            os.flush()
            os.close()
        } catch (e: FileNotFoundException) {
            ExceptionUtils.printStackTraceIfDebug(e)
        } catch (e1: IOException) {
            e1.printStackTrace()
        } finally {
            try {
                `is`!!.close()
            } catch (e: IOException) {
            }
        }
        return f
    }

    fun obterJsonAsset(context: Context, nomePasta: String, nomeArquivo: String): String? {
        var json: String? = null
        val nomeCompleto = nomePasta + File.separator + nomeArquivo
        val assetManager = context.assets
        try {
            val inputStream = assetManager.open(nomeCompleto)
            json = Scanner(inputStream).useDelimiter("\\A").next()
        } catch (e: IOException) {
            ExceptionUtils.printStackTraceIfDebug(e)
        } catch (e: Exception) {
        }
        return json
    }

    //OBTEM A IMAGEM DA PASTA ASSETS LEVANDO EM CONSIDERACAO A PASTA.
    fun obterBitmapAsset(context: Context, nomePasta: String, nomeArquivo: String): Bitmap? {
        val nomeCompleto = nomePasta + File.separator + nomeArquivo
        var bitmap: Bitmap? = null
        val assetManager = context.assets
        try {
            val inputStream = assetManager.open(nomeCompleto)
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        return bitmap
    }

    // =========== MÉTODOS IMAGEM CAPA  ------------------------------------------------------------
    fun salvarImagemCapaPersonalizada(imagem: Bitmap) {
        val stream = ByteArrayOutputStream()
        imagem.compress(Bitmap.CompressFormat.PNG, 50, stream)
        val byteArray = stream.toByteArray()
        converteStreamParaArquivo(
            ByteArrayInputStream(byteArray),
            "capa_perfil", "jpg"
        )
    }

    fun removerImagemCapaPersonalizada() {
        val file: File = File(
            Sessao.applicationContext!!.filesDir.path
                    + "/MyWay/capa_perfil.jpg"
        )
        if (file.exists()) file.delete()
    }

    fun obtemImagemCapaPersonalizada(): Bitmap {
        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        return BitmapFactory.decodeFile(
            Sessao.applicationContext!!.filesDir
                .absolutePath + "/MyWay/capa_perfil.jpg", options
        )
    }

    fun hasImagemCapaPersonalizada(): Boolean {
        val file: File = File(
            (Sessao.applicationContext!!.filesDir.absolutePath
                    + "/MyWay/capa_perfil.jpg")
        )
        return if (file.exists()) true else false
    }

    val diretorioDeImagemCapaServidor: File
        get() {
            val path = File(diretorioDoAplicativo.path + File.separator + "capa")
            if (!path.exists()) {
                path.mkdir()
            }
            return path
        }

    fun setImagemCapaCacheServidor(imagem: Bitmap, nome: String) {
        excluirImagemCapaCacheServidor()
        val imagemCapaCache = File(
            diretorioDeImagemCapaServidor,
            "$nome.jpg"
        )
        salvarBitmapArquivo(imagem, imagemCapaCache)
    }

    fun setImagemCacheServidor(imagem: Bitmap, nome: String) {
        excluirImagemCapaCacheServidor()
        val imagemCapaCache = File(
            diretorioDeImagemCapaServidor,
            "$nome.jpg"
        )
        salvarBitmapArquivo(imagem, imagemCapaCache)
    }

    fun salvarBitmapArquivo(bitmap: Bitmap, file: File?): Boolean {
        try {
            val fOut = FileOutputStream(file)
            val salvou = bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
            return salvou
        } catch (e: Exception) {
            return false
        }
    }

    fun getImagemCapaCacheServidor(nome: String): File {
        val diretorio = diretorioDeImagemCapaServidor
        return File(diretorio.path, "$nome.jpg")
    }

    fun getImagemCacheServidor(nome: String): File {
        val diretorio = diretorioDeImagemCapaServidor
        return File(diretorio.path, "$nome.jpg")
    }

    fun excluirImagemCapaCacheServidor() {
        val diretorio = diretorioDeImagemCapaServidor
        val imagens = diretorio.listFiles()
        try {
            for (file: File in imagens) {
                file.delete()
            }
        } catch (e: Exception) {
        }
    }

    // =========== FIM MÉTODOS IMAGEM CAPA  --------------------------------------------------------
    // =========== INÍCIO MÉTODOS IMAGEM LOGO  --------------------------------------------------------
    fun salvarUrlLogoCustomizada(context: Context?, url: String?) {
        Armazenamento.armazene(context, IMAGEM_LOGO_URL_KEY, url)
    }

    fun obterUrlLogoCustomizada(context: Context?): String? {
        return Armazenamento.recupereString(context, IMAGEM_LOGO_URL_KEY, "")
    }

    fun salvarUrlFundoCustomizada(context: Context?, url: String?) {
        Armazenamento.armazene(context, IMAGEM_FUNDO_URL_KEY, url)
    }

    fun obterUrlFundoCustomizada(context: Context?): String? {
        return Armazenamento.recupereString(context, IMAGEM_FUNDO_URL_KEY, "")
    }

    val diretorioDeImagemLogoCustomizada: File
        get() {
            val path = File(diretorioDoAplicativo.path + File.separator + "logo")
            if (!path.exists()) {
                path.mkdir()
            }
            return path
        }
    val fileImagemLogoCustomizada: File
        get() = File(diretorioDeImagemLogoCustomizada, "logo" + ".png")

    fun salvarImagemLogoCustomizada(bitmap: Bitmap) {
        val imagemLogoFile = fileImagemLogoCustomizada
        salvarBitmapArquivo(bitmap, imagemLogoFile)
    }

    val fileImagemFundoCustomizada: File
        get() = File(diretorioDeImagemLogoCustomizada, "fundo" + ".png")

    fun salvarImagemFundoCustomizada(bitmap: Bitmap) {
        val imagemLogoFile = fileImagemFundoCustomizada
        salvarBitmapArquivo(bitmap, imagemLogoFile)
    }

    // =========== FIM MÉTODOS IMAGEM LOGO  --------------------------------------------------------
    fun deleteDownloadedFiles() {
        val path: File = File(Sessao.applicationContext!!.filesDir, "MyWay")
        if (path != null) {
            val files = path.listFiles()
            if (files != null) {
                for (file: File in files) {
                    if (file.name.uppercase(Locale.getDefault()).endsWith(".PDF") /*||
                        file.name.startsWith(Relatorio.RELATORIO)*/
                    ) {
                        file.delete()
                    }
                }
            }
        }
    }

    fun abraPdf(context: Context, pdf: File?, mensagemErro: String?) {
        if (pdf == null || !is_pdf(pdf)) {
            //InfoToast.mostrarToastLongo(context, mensagemErro)
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setType("application/pdf")
        intent.setDataAndType(
            FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                pdf
            ), "application/pdf"
        )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //InfoToast.mostrarToastLongo(context, context.getString(R.string.nenhuma_app_pdf))
        }
    }

    fun abrirArquivo(context: Context?, arquivo: File) {
        var context = context
        try {
            if (context == null) {
                context = LGApplication.instance!!.baseContext
            }
            val mimeType = URLConnection.guessContentTypeFromName(
                arquivo.name
            )
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.setDataAndType(
                context?.let {
                    FileProvider.getUriForFile(
                        it,
                        context.applicationContext.packageName + ".provider",
                        arquivo
                    )
                }, mimeType
            )
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            if (context != null) {
                context.startActivity(intent)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context, context!!.getString(
                    R.string.aplicativo_apropriado_nao_encontrado
                ),
                Toast.LENGTH_LONG
            ).show()
        } catch (e: NullPointerException) {
            Toast.makeText(
                context,
                context!!.getString(R.string.erro_baixar_arquivo),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val diretorioDoAplicativo: File
        get() {
            val path: File = File(Sessao.applicationContext!!.filesDir, "MyWay")
            if (!path.exists()) {
                path.mkdir()
            }
            return path
        }

    fun criarRelatorioFile(nome: String, extensao: String): File {
        val diretorioDoAplicativo = diretorioDoAplicativo
        val file = File(diretorioDoAplicativo, "$nome.$extensao")
        file.deleteOnExit()
        return file
    }

    val diretorioDeCache: File
        get() {
            val path = File(diretorioDoAplicativo.path + File.separator + "cache")
            if (!path.exists()) {
                path.mkdir()
            }
            return path
        }
    val diretorioImagensDoDispositivo: File
        get() {
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            if (!path.exists()) {
                path.mkdir()
            }
            return path
        }
    val diretorioImagensComprovantesPonto: File
        get() {
            val path =
                File(diretorioImagensDoDispositivo.path + File.separator + "Comprovantes de Ponto")
            if (!path.exists()) {
                path.mkdir()
            }
            return path
        }

    fun chamarMediaScannerParaDisponibilizarNovaFotoParaGaleria(context: Context, foto: File?) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(foto)
        mediaScanIntent.setData(contentUri)
        context.sendBroadcast(mediaScanIntent)
    }

    fun resetCache() {
        try {
            val files = diretorioDeCache.listFiles()
            for (fileFoto: File in files) {
                fileFoto.delete()
            }
        } catch (e: Exception) {
        }
    }

    fun obterImagem(path: String?): Bitmap {
        return BitmapFactory.decodeFile(path)
    }

    @Throws(IOException::class)
    fun copiarStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var n = 0
        while (-1 != (input.read(buffer).also { n = it })) output.write(buffer, 0, n)
    }

    fun obterInSampleSize(
        `is`: InputStream?,
        reqWidth: Int,
        reqHeight: Int,
        options: BitmapFactory.Options
    ): Int {
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(`is`, null, options)
        return calculateInSampleSize(options, reqWidth, reqHeight)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth)
            ) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun salvarImagemDemonstracao(imagem: Bitmap, nome: String): File {
        val path = diretorioDeImagensPersonalizadasDemonstracao
        val imagemFile = File(path, "$nome.jpg")
        salvarBitmapArquivo(imagem, imagemFile)
        return imagemFile
    }

    val diretorioDeImagensPersonalizadasDemonstracao: File
        get() {
            val path = File(diretorioDoAplicativo.path + File.separator + "img_demonstracao")
            return criarDiretorioSeNaoExistir(path)
        }

    private fun criarDiretorioSeNaoExistir(path: File): File {
        if (!path.exists()) {
            path.mkdir()
        }
        return path
    }

    fun esvaziarDiretorioImagensPersonalizadasDemonstracao() {
        try {
            val files = diretorioDeImagensPersonalizadasDemonstracao.listFiles()
            for (fileFoto: File in files) {
                fileFoto.delete()
            }
        } catch (e: Exception) {
        }
    }

    fun is_pdf(file: File?): Boolean {
        try {
            val data: ByteArray = IOUtils.toByteArray(FileInputStream(file))
            if ((data != null) && (data.size > 4) && (
                        data[0].toInt() == 0x25) && ( // %
                        data[1].toInt() == 0x50) && ( // P
                        data[2].toInt() == 0x44) && ( // D
                        data[3].toInt() == 0x46) && ( // F
                        data[4].toInt() == 0x2D)
            ) { // -

                // version 1.3 file terminator
                if ((data[5].toInt() == 0x31) && (data[6].toInt() == 0x2E) && (data[7].toInt() == 0x34 || data[7].toInt() == 0x31) && (
                            data[data.size - 7].toInt() == 0x25) && ( // %
                            data[data.size - 6].toInt() == 0x25) && ( // %
                            data[data.size - 5].toInt() == 0x45) && ( // E
                            data[data.size - 4].toInt() == 0x4F) && ( // O
                            data[data.size - 3].toInt() == 0x46) && ( // F
                            data[data.size - 2].toInt() == 0x20) && ( // SPACE
                            data[data.size - 1].toInt() == 0x0A)
                ) { // EOL
                    return true
                }

                // version 1.3 file terminator
                if ((data[5].toInt() == 0x31) && (data[6].toInt() == 0x2E) && (data[7].toInt() == 0x34 || data[7].toInt() == 0x31) && (
                            data[data.size - 6].toInt() == 0x25) && ( // %
                            data[data.size - 5].toInt() == 0x25) && ( // %
                            data[data.size - 4].toInt() == 0x45) && ( // E
                            data[data.size - 3].toInt() == 0x4F) && ( // O
                            data[data.size - 2].toInt() == 0x46) && ( // F
                            data[data.size - 1].toInt() == 0x0A)
                ) { // EOL
                    return true
                }
            }
        } catch (e: Exception) {
        }
        return false
    }
}

