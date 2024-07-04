package br.com.lg.MyWay.newgentemobile.compartilhado.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.drawable.DrawableCompat
import br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android.activities.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar


object TelaUtils {
    private const val watcherState = false
    private var snackbar: Snackbar? = null
    private var cores: Array<String>? = null
    private var coresfundo: Array<String>? = null
    private var coresfonte: Array<String>? = null
    private var coresfontecontrole: Array<String>? = null
    fun getCores(): Array<String>? {
        if (cores == null || cores!!.size != 3) {
            setCores(null)
        }
        return cores
    }

    fun setCores(novasCores: Array<String>?) {
        if (novasCores == null || novasCores.size != 3) {
            cores = arrayOf("0", "150", "255")
        } else {
            cores = novasCores
        }
    }

    fun getCoresfundo(): Array<String>? {
        if (coresfundo == null || coresfundo!!.size != 3) {
            setCores(null)
        }
        return coresfundo
    }

    fun setCoresfundo(coresfundoretorno: Array<String>?) {
        if (coresfundoretorno == null || coresfundoretorno.size != 3) {
            coresfundo = arrayOf("239", "239", "245")
        } else {
            coresfundo = coresfundoretorno
        }
    }

    fun getCoresfonte(): Array<String>? {
        if (coresfonte == null || coresfonte!!.size != 3) {
            setCores(null)
        }
        return coresfonte
    }

    fun setCoresfonte(coresfonteretorno: Array<String>?) {
        if (coresfonteretorno == null || coresfonteretorno.size != 3) {
            coresfonte = arrayOf("0", "150", "255")
        } else {
            coresfonte = coresfonteretorno
        }
    }

    fun getCoresfontecontrole(): Array<String>? {
        if (coresfontecontrole == null || coresfontecontrole!!.size != 3) {
            setCores(null)
        }
        return coresfontecontrole
    }

    fun setCoresfontecontrole(coresfontecontroleret: Array<String>?) {
        if (coresfontecontroleret == null || coresfontecontroleret.size != 3) {
            coresfontecontrole = arrayOf("250", "250", "250")
        } else {
            coresfontecontrole = coresfontecontroleret
        }
    }

    val corTemaFundo: Int
        get() {
            var r = 250
            var g = 250
            var b = 250
            if (coresfundo != null) {
                r = coresfundo!![0].toInt()
                g = coresfundo!![1].toInt()
                b = coresfundo!![2].toInt()
            }
            return Color.rgb(r, g, b)
        }
    val corTemaRGBFundo: String
        get() {
            val r = coresfundo!![0].toInt()
            val g = coresfundo!![1].toInt()
            val b = coresfundo!![2].toInt()
            return "rgb($r,$g,$b)"
        }
    val corTemaFonte: Int
        get() {
            if (coresfonte == null) return Color.rgb(0, 0, 0)
            val r = coresfonte!![0].toInt()
            val g = coresfonte!![1].toInt()
            val b = coresfonte!![2].toInt()
            return Color.rgb(r, g, b)
        }
    val corTemaRGBFonte: String
        get() {
            val r = coresfonte!![0].toInt()
            val g = coresfonte!![1].toInt()
            val b = coresfonte!![2].toInt()
            return "rgb($r,$g,$b)"
        }
    val corTemaFcontroles: Int
        get() {
            val r = coresfontecontrole!![0].toInt()
            val g = coresfontecontrole!![1].toInt()
            val b = coresfontecontrole!![2].toInt()
            return Color.rgb(r, g, b)
        }
    val corTemaRGBFcontroles: String
        get() {
            val r = coresfontecontrole!![0].toInt()
            val g = coresfontecontrole!![1].toInt()
            val b = coresfontecontrole!![2].toInt()
            return "rgb($r,$g,$b)"
        }
    val corTema: Int
        get() {
            val r = cores!![0].toInt()
            val g = cores!![1].toInt()
            val b = cores!![2].toInt()
            return Color.rgb(r, g, b)
        }
    val corTemaRGB: String
        get() {
            val r = cores!![0].toInt()
            val g = cores!![1].toInt()
            val b = cores!![2].toInt()
            return "rgb($r,$g,$b)"
        }

    fun setUrlImagemBannerCapa(context: Context?, url: String?) {
        Armazenamento.armazene(context, "UrlHeaderPadrao", url)
    }

    val corDoItemPressionado: Int
        get() = corTema

    fun convertaDpParaPixels(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }

    fun converterDpParaPixel(dp: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        )
    }

    fun isTablet(context: Context): Boolean {
        val xlarge =
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == 4
        val large =
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE
        return xlarge || large
    }

    fun hideSoftKeyboard(editText: EditText, activity: Activity) {
        try {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    fun esconderSoftKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @JvmOverloads
    fun tematizeImagem(image: ImageView, color: Int = corTema) {
        val colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        image.colorFilter = colorFilter
    }

    fun tematizeTextView(textView: TextView) {
        textView.setTextColor(corTemaFonte)
    }

    fun tematizeTextViewComCorTema(textView: TextView) {
        textView.setTextColor(corTema)
    }

    fun tematizeEditText(editText: EditText) {
        editText.setTextColor(corTema)
    }

    fun tematizeTextViewClearBackground(textview: TextView) {
        textview.setTextColor(corTema)
    }

    fun tematizeTextViewcontroles(textView: Button) {
        try {
            textView.setTextColor(corTemaFcontroles)
        } catch (e: Exception) {
            Log.d("cor", e.message!!)
        }
    }

    fun tematizeBackgroundColor(view: View) {
        view.setBackgroundColor(corTemaFundo)
    }

    fun tematizeProgress(view: ProgressBar) {
        view.indeterminateDrawable.setColorFilter(corTema, PorterDuff.Mode.SRC_IN)
    }

    fun tematizeBackgroundColorSwitch(view: SwitchCompat) {
        val states = arrayOf(intArrayOf(-R.attr.state_checked), intArrayOf(R.attr.state_checked))
        val thumbColors = intArrayOf(
            Color.WHITE,
            corTema
        )
        val trackColors = intArrayOf(
            Color.WHITE,
            corTema
        )
        DrawableCompat.setTintList(
            DrawableCompat.wrap(view.thumbDrawable),
            ColorStateList(states, thumbColors)
        )
        DrawableCompat.setTintList(
            DrawableCompat.wrap(view.trackDrawable),
            ColorStateList(states, trackColors)
        )
    }

    fun tematizeBackgroundColorNovahome(view: View) {
        val backgroundGradient = view.background as GradientDrawable
        backgroundGradient.setColor(corTema)
    }

    fun tematizeActionBar(context: Context, layout: View?) {
        if (layout == null) {
            return
        }
        //todo: descomentar dps
//        val personalizarCorActionBar = context.resources.getBoolean(R.bool.customizar_cor_actionbar)
//        if (personalizarCorActionBar) {
//            val actionBar = layout.findViewById<View>(R.id.actionBar)
//            val titulo = layout.findViewById<View>(R.id.titulo) as TextView
//            if (actionBar != null && titulo != null) {
//                actionBar.setBackgroundColor(corTema)
//                titulo.setTextColor(Color.WHITE)
//            }
//        }
    }

    fun tematizeImagemFiltrandoBranco(button: ImageView, idImagem: Int, cores: Array<String>?) {
        if (cores != null && cores.size == 3) {
            val imagem = button.context.resources.getDrawable(idImagem) as BitmapDrawable
            val bitmap = imagem.bitmap
            button.setImageBitmap(ImageUtils.customizarBotaoBarraDeNavegacao(bitmap, cores))
        }
    }

    fun adicioneEstadoDeSelecaoParaBotao(button: ImageView, resIdNormal: Int, resIdAtivo: Int) {
        val resources = button.resources
        val normal = resources.getDrawable(resIdNormal)
        var pressionado = resources.getDrawable(resIdAtivo).mutate()
        var imagemPressionado = (pressionado as BitmapDrawable).bitmap
        imagemPressionado = ImageUtils.customizarBotaoBarraDeNavegacao(imagemPressionado, cores)
        pressionado = BitmapDrawable(resources, imagemPressionado)
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(R.attr.state_pressed), pressionado)
        drawable.addState(intArrayOf(), normal)
        button.setImageDrawable(drawable)
    }

    fun adicioneEstadoDeSelecaoParaBotaoPdf(resources: Resources, button: ImageView) {
        val normal = resources.getDrawable(br.com.lg.MyWay.newgentemobile.R.drawable.icone_pdf_ativo_hdpi).mutate()
        val pressionado: Drawable = BitmapDrawable(
            resources,
            ImageUtils.customizarBotaoBarraDeNavegacao((normal as BitmapDrawable).bitmap, cores)
        ).mutate()
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(R.attr.state_pressed), pressionado)
        drawable.addState(intArrayOf(), normal)
        button.setImageDrawable(drawable)
    }

    fun tematizeEstadoDeSelecao(textView: TextView) {
        val configCores = ColorStateList(
            arrayOf<IntArray>(intArrayOf(R.attr.state_pressed), intArrayOf()), intArrayOf(
                corTema,
                br.com.lg.MyWay.newgentemobile.R.color.dark_gray
            )
        )
        textView.setTextColor(configCores)
    }

    @JvmOverloads
    fun tematizeDrawable(image: Drawable, cor: Int = corTema): Drawable {
        val colorFilter = PorterDuffColorFilter(cor, PorterDuff.Mode.SRC_IN)
        image.colorFilter = colorFilter
        return image
    }

    fun tematizeBotao(button: Button) {
        button.background.setColorFilter(corTema, PorterDuff.Mode.SRC_IN)
    }

    fun tematizeSlider(slider: Slider) {
        slider.setThumbTintList(ColorStateList.valueOf(corTema))
        slider.setTrackTintList(ColorStateList.valueOf(corTema))
    }

    fun tematizeToggleButton(button: ToggleButton) {
        button.buttonDrawable!!.setColorFilter(corTema, PorterDuff.Mode.SRC_IN)
        button.setTextColor(corTema)
    }

    fun tematizeFloatingBotao(button: FloatingActionButton) {
        button.setBackgroundTintList(ColorStateList.valueOf(corTema))
    }

    fun tematizeFloatingBotaoInterno(button: FloatingActionButton) {
        button.setImageTintList(ColorStateList.valueOf(corTema))
        val drawable = button.getForeground() as GradientDrawable
        drawable.setStroke(3, corTema)
        button.setForeground(drawable)
    }

    fun tematizeBackgroundTextView(textView: TextView, cores: Array<String?>?): TextView {
        textView.background.setColorFilter(corTema, PorterDuff.Mode.SRC_IN)
        return textView
    }

    fun tematizeImagensLoginParaTemaPadrao(button: Button, image: ImageView) {
        button.background.setColorFilter(Color.rgb(26, 155, 252), PorterDuff.Mode.SRC_IN)
        image.colorFilter =
            PorterDuffColorFilter(Color.rgb(26, 155, 252), PorterDuff.Mode.SRC_IN)
    }

    fun getDensidadePixels(context: Context): Float {
        return context.resources.displayMetrics.density
    }

    fun getLarguraTela(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun loadBitmapFromView(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

    private val activity: Activity?
        private get() {
            try {
                val activityThreadClass = Class.forName("android.app.ActivityThread")
                val activityThread =
                    activityThreadClass.getMethod("currentActivityThread").invoke(null)
                val activitiesField = activityThreadClass.getDeclaredField("mActivities")
                activitiesField.isAccessible = true
                val activities = activitiesField[activityThread] as Map<Any, Any>
                    ?: return null
                for (activityRecord in activities.values) {
                    val activityRecordClass: Class<*> = activityRecord.javaClass
                    val pausedField = activityRecordClass.getDeclaredField("paused")
                    pausedField.isAccessible = true
                    if (!pausedField.getBoolean(activityRecord)) {
                        val activityField =
                            activityRecordClass.getDeclaredField("activity")
                        activityField.isAccessible = true
                        return activityField[activityRecord] as Activity
                    }
                }
            } catch (e: Exception) {
            }
            return null
        }

    fun exibirSnackBar(tentativas: Int) {
        val activity = activity
        if (activity != null) {
            if (activity is MainActivity) {
                (activity as MainActivity).obterViewParaSnackbar()?.let {
                    mostrarSnackBarConexao(
                        it,
                        tentativas
                    )
                }
            }
        }
    }

    private fun mostrarSnackBarConexao(view: View, tentativas: Int) {
        if (snackbar != null && snackbar?.isShown == true) {
            activity!!.runOnUiThread {
                (snackbar!!.getView()
                    .findViewById(com.google.android.material.R.id.snackbar_text) as TextView).text =
                    "Tentando conectar com o servidor, tentativa ($tentativas)"
            }
        } else {
            snackbar = Snackbar.make(
                view,
                "Tentando conectar com o servidor, tentativa ($tentativas)", Snackbar.LENGTH_SHORT
            )
            (snackbar!!.view
                .findViewById(com.google.android.material.R.id.snackbar_text) as TextView).setTextColor(
                Color.WHITE
            )
            snackbar!!.show()
        }
    }

    fun fecharSnackBar() {
        snackbar?.dismiss()
    }

    fun mostrarSnackBarGenerica(text: String?) {
        snackbar = text?.let {
            Snackbar.make(
                activity!!.window.decorView.findViewById(R.id.content),
                it,
                Snackbar.LENGTH_LONG
            )
        }
        (snackbar?.view
            ?.findViewById(com.google.android.material.R.id.snackbar_text) as TextView).setTextColor(
            Color.WHITE
        )
        snackbar!!.show()
    }
}

