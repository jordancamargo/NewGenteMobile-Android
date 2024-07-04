package br.com.lg.MyWay.newgentemobile.compartilhado.utils

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager

object PreferenciasUtil {
    private const val LEMBRAR_USUARIO = "LembrarUsuario"
    private const val MANTER_LOGADO = "ManterLogado"
    private const val USAR_BIOMETRIA = "UsarBiometria"
    private const val AUT_BIOMETRIA = "AutorizouBiometria"
    private const val SAIU_APP = "Saiu"
    private const val BIOMETRIA_CANCELADA = "Biometria_cancelada"
    private const val AUTENTIC_INDEVIDA = "acessoindevido"
    private const val SHORTCUT = "Shortcut"
    private const val CNPJ = "Cnpj"
    private const val LOGOUWEAR = "LOGOU"
    private const val DADOS_PORTAL_QRCODE = "dadosportalqrcode"
    private const val TROCOUPORTAL = "trocouportal"
    private const val PERMITEALTERARFOTO = "PermiteAlterarFoto"
    private const val PERMITEEXIBIRFOTO = "PermiteExibirFoto"
    private const val TAMANHOMAXIMODAFOTO = "TamanhoMaximoDaFoto"
    private const val EXPIROUSAML = "ExpirouSAML"
    private const val SEGUNDOFATOR = "segundofator"
    private const val NOTIFICACAOWORK = "notifyworkflow"
    private const val ABREPONTOGENTE = "abrepontogente"
    private const val RETRYCONFIGLOGIN = "retryconfiglogin"
    private const val CONTAGEMRETRY = "CONTAGEMRETRY"
    private const val URL_WEBVIEW_AA = "URL_WEBVIEW_AA"
    fun setManterLogado(context: Context?, manterLogado: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(MANTER_LOGADO, manterLogado)
        editor.commit()
    }

    fun setLembrarUsuario(context: Context?, lembrarUsuario: Boolean) {
        val prefere = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefere.edit()
        editor.putBoolean(LEMBRAR_USUARIO, lembrarUsuario)
        editor.commit()
    }

    fun getManterLogado(context: Context?): Boolean {
        val manterLogado: Boolean
        manterLogado = if (Armazenamento.contem(context, MANTER_LOGADO)) {
            Armazenamento.recupereBoolean(context, MANTER_LOGADO, false)
        } else {
            ConfiguracoesVindasDoServidorUtils.obterConfigBoolean(
                context,
                ConfiguracoesVindasDoServidorUtils.DEFAULT_LOGAR_AUTOMATICO
            )
        }
        return manterLogado
    }

    fun getLembrarUsuario(context: Context?): Boolean {
        val lembrarUsuario: Boolean
        lembrarUsuario = if (Armazenamento.contem(context, LEMBRAR_USUARIO)) {
            Armazenamento.recupereBoolean(context, LEMBRAR_USUARIO, true)
        } else {
            ConfiguracoesVindasDoServidorUtils.obterConfigBoolean(
                context,
                ConfiguracoesVindasDoServidorUtils.DEFAULT_LEMBRAR_USUARIO
            )
        }
        return lembrarUsuario
    }

    fun getUsarBiometria(context: Context?): Boolean {
        val usarbiometria: Boolean
        usarbiometria = if (Armazenamento.contem(context, USAR_BIOMETRIA)) {
            Armazenamento.recupereBoolean(context, USAR_BIOMETRIA, false)
        } else {
            false
        }
        return usarbiometria
    }

    fun setUsarBiometria(context: Context?, usarbiometria: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(USAR_BIOMETRIA, usarbiometria)
        editor.commit()
    }

    fun getAutorizouBiometria(context: Context?): Boolean {
        val autorizoubio: Boolean
        autorizoubio = if (Armazenamento.contem(context, AUT_BIOMETRIA)) {
            Armazenamento.recupereBoolean(context, AUT_BIOMETRIA, false)
        } else {
            false
        }
        return autorizoubio
    }

    fun setAutorizouBiometria(context: Context?, autorizoubio: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(AUT_BIOMETRIA, autorizoubio)
        editor.commit()
    }

    fun getSaiuApp(context: Context?): Boolean {
        val saiuapp: Boolean
        saiuapp = if (Armazenamento.contem(context, SAIU_APP)) {
            Armazenamento.recupereBoolean(context, SAIU_APP, false)
        } else {
            false
        }
        return saiuapp
    }

    fun setSaiuApp(context: Context?, saiuapp: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(SAIU_APP, saiuapp)
        editor.commit()
    }

    fun getBiometriaCancelada(context: Context?): Boolean {
        val biocancelada: Boolean
        biocancelada = if (Armazenamento.contem(context, BIOMETRIA_CANCELADA)) {
            Armazenamento.recupereBoolean(context, BIOMETRIA_CANCELADA, false)
        } else {
            false
        }
        return biocancelada
    }

    fun setBiometriaCancelada(context: Context?, biocancelada: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(BIOMETRIA_CANCELADA, biocancelada)
        editor.commit()
    }

    fun getAcessoIndevido(context: Context?): String? {
        val acessoindevido: String?
        acessoindevido = if (Armazenamento.contem(context, AUTENTIC_INDEVIDA)) {
            Armazenamento.recupereString(context, AUTENTIC_INDEVIDA, "")
        } else {
            ""
        }
        return acessoindevido
    }

    fun setAcessoIndevido(context: Context?, acessoindevido: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(AUTENTIC_INDEVIDA, acessoindevido)
        editor.commit()
    }

    fun getMarcacaoExterna(context: Context?): Boolean {
        val marcacaoexterna: Boolean
        marcacaoexterna = if (Armazenamento.contem(context, SHORTCUT)) {
            Armazenamento.recupereBoolean(context, SHORTCUT, false)
        } else {
            false
        }
        return marcacaoexterna
    }

    fun setMarcacaoExterna(context: Context?, marcacaoexterna: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(SHORTCUT, marcacaoexterna)
        editor.commit()
    }

    fun getCnpj(context: Context?): String? {
        val cnpj: String?
        cnpj = if (Armazenamento.contem(context, CNPJ)) {
            Armazenamento.recupereString(context, CNPJ)
        } else {
            ""
        }
        return cnpj
    }

    fun setCnpj(context: Context?, cnpj: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(CNPJ, cnpj)
        editor.commit()
    }

    fun getLogouWear(context: Context?): Boolean {
        val logouwear: Boolean
        if (Armazenamento.contem(context, LOGOUWEAR)) {
            logouwear = Armazenamento.recupereBoolean(context, LOGOUWEAR, false)
            return logouwear
        } else {
            logouwear = false
        }
        return logouwear
    }

    fun setLogouWear(context: Context?, logouwear: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(LOGOUWEAR, logouwear)
        editor.commit()
    }

    fun getDadosPortalQrcode(context: Context?): String? {
        val acessoindevido: String?
        acessoindevido = if (Armazenamento.contem(context, DADOS_PORTAL_QRCODE)) {
            Armazenamento.recupereString(context, DADOS_PORTAL_QRCODE, "")
        } else {
            ""
        }
        return acessoindevido
    }

    fun setDadosPortalQrcode(context: Context?, dadosportal: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(DADOS_PORTAL_QRCODE, dadosportal)
        editor.commit()
    }

    fun getDadosTrocouPortal(context: Context?): Boolean {
        val acessoindevido: Boolean
        acessoindevido = if (Armazenamento.contem(context, TROCOUPORTAL)) {
            Armazenamento.recupereBoolean(context, TROCOUPORTAL, false)
        } else {
            false
        }
        return acessoindevido
    }

    fun setgetDadosTrocouPortal(context: Context?, dadosportal: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(TROCOUPORTAL, dadosportal)
        editor.commit()
    }

    fun getPermiteAlterarFoto(context: Context?): Boolean {
        val PermiteAlterarFoto: Boolean
        PermiteAlterarFoto = if (Armazenamento.contem(context, PERMITEALTERARFOTO)) {
            Armazenamento.recupereBoolean(context, PERMITEALTERARFOTO, false)
        } else {
            false
        }
        return PermiteAlterarFoto
    }

    fun setPermiteAlterarFoto(context: Context?, PermiteAlterarFoto: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(PERMITEALTERARFOTO, PermiteAlterarFoto)
        editor.commit()
    }

    fun getPermiteExibirFoto(context: Context?): Boolean {
        val PermiteExibirFoto: Boolean
        PermiteExibirFoto = if (Armazenamento.contem(context, PERMITEEXIBIRFOTO)) {
            Armazenamento.recupereBoolean(context, PERMITEEXIBIRFOTO, false)
        } else {
            false
        }
        return PermiteExibirFoto
    }

    fun setPermiteExibirFoto(context: Context?, PermiteExibirFoto: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(PERMITEEXIBIRFOTO, PermiteExibirFoto)
        editor.commit()
    }

    fun getTamanhoMaximoDaFoto(context: Context?): Int {
        val TamanhoMaximoDaFoto: Int
        TamanhoMaximoDaFoto = if (Armazenamento.contem(context, TROCOUPORTAL)) {
            Armazenamento.recuperarInt(context, TAMANHOMAXIMODAFOTO, 1000)
        } else {
            1000
        }
        return TamanhoMaximoDaFoto
    }

    fun setTamanhoMaximoDaFoto(context: Context?, TamanhoMaximoDaFoto: Int) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putInt(TAMANHOMAXIMODAFOTO, TamanhoMaximoDaFoto)
        editor.commit()
    }

    fun getEXPIROUSAML(context: Context?): Boolean {
        val expirousaml: Boolean
        expirousaml = if (Armazenamento.contem(context, EXPIROUSAML)) {
            Armazenamento.recupereBoolean(context, EXPIROUSAML, false)
        } else {
            false
        }
        return expirousaml
    }

    fun setEXPIROUSAML(context: Context?, expirousaml: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(EXPIROUSAML, expirousaml)
        editor.commit()
    }

    fun getNOTIFICACAOWORK(context: Context?): Boolean {
        val expirousaml: Boolean
        expirousaml = if (Armazenamento.contem(context, NOTIFICACAOWORK)) {
            Armazenamento.recupereBoolean(context, NOTIFICACAOWORK, false)
        } else {
            false
        }
        return expirousaml
    }

    fun setNOTIFICACAOWORK(context: Context?, expirousaml: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(NOTIFICACAOWORK, expirousaml)
        editor.commit()
    }

    fun getabrepontogente(context: Context?): Boolean {
        val expirousaml: Boolean
        expirousaml = if (Armazenamento.contem(context, ABREPONTOGENTE)) {
            Armazenamento.recupereBoolean(context, ABREPONTOGENTE, false)
        } else {
            false
        }
        return expirousaml
    }

    fun setabrepontogente(context: Context?, expirousaml: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(ABREPONTOGENTE, expirousaml)
        editor.commit()
    }

    fun getretryconfiglogin(context: Context?): Boolean {
        val retry: Boolean
        retry = if (Armazenamento.contem(context, RETRYCONFIGLOGIN)) {
            Armazenamento.recupereBoolean(context, RETRYCONFIGLOGIN, false)
        } else {
            false
        }
        return retry
    }

    fun setretryconfiglogin(context: Context?, retry: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(RETRYCONFIGLOGIN, retry)
        editor.commit()
    }

    fun getSegundoFator(context: Context?, valor: String?): Boolean {
        val SegundoFator: Boolean
        SegundoFator = if (Armazenamento.contem(context, valor)) {
            Armazenamento.recupereBoolean(context, valor, false)
        } else {
            false
        }
        return SegundoFator
    }

    fun setSegundoFator(context: Context?, valor: String?, SegundoFator: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putBoolean(valor, SegundoFator)
        editor.commit()
    }

    fun getCONTAGEMRETRY(context: Context?): Int {
        val contador: Int
        contador = if (Armazenamento.contem(context, CONTAGEMRETRY)) {
            Armazenamento.recuperarInt(context, CONTAGEMRETRY, 0)
        } else {
            0
        }
        return contador
    }

    fun setCONTAGEMRETRY(context: Context?, contador: Int) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putInt(CONTAGEMRETRY, contador)
        editor.commit()
    }

    fun setUrlDeRetornoPortalAA(context: Context, url: String?) {
        val prefs = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        val edit = prefs.edit()
        edit.putString(URL_WEBVIEW_AA, url)
        edit.commit()
    }

    fun getUrlDeRetornoPortalAA(context: Context): String? {
        val prefs = context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        return prefs.getString(URL_WEBVIEW_AA, "")
    }
}

