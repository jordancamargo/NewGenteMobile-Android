package br.com.lg.MyWay.newgentemobile.configuracoes.demonstracao.dados

import android.app.Activity
import android.content.Context
import android.content.Intent
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.autenticacao.dados.LoginListener
import br.com.lg.MyWay.newgentemobile.autenticacao.dominio.ControladorLogin
import br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.models.Colaborador
import br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android.activities.MainActivity
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.ColaboradorSession
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Funcionalidade
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ConfiguracoesVindasDoServidorUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ExceptionUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ImageUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.TelaUtils
import br.com.lg.MyWay.newgentemobile.configuracoes.demonstracao.dados.models.Demo
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.Scanner


// CLASSE CRIADA PARA GERENCIAR PORTAIS DE DEMONSTRAÇÃO
object DemoUtils {
    const val PORTAL_DEMO = "demo"
    const val IMAGEM_LOGO_PERSONALIZADA = "IMAGEM_LOGO_PERSONALIZADA"
    const val IMAGEM_CAPA_PERSONALIZADA = "IMAGEM_CAPA_PERSONALIZADA"
    const val TEMA_PERSONALIZADO = "TEMA_PERSONALIZADO"
    fun ehPortalDemonstracao(portal: String): Boolean {
        return if (getDemo(portal) != null) true else false
    }

    fun loginDemonstracao(portal: String, campos: HashMap<String?, String?>): Boolean {
        val demo: Demo = getDemo(portal) ?: return false

        //linha copiada da versão antiga
        return campos.containsValue(demo.getUsuario()) && campos.containsValue(demo.getSenha())
    }

    fun getCorPortalDemo(portal: String): Array<String?>? {
        val demo: Demo? = getDemo(portal)
        return if (demo != null) demo.getCor() else null
    }

    fun getReferenciaImagemPortal(portal: String): Int {
        val demo: Demo? = getDemo(portal)
        return if (demo != null) demo.getReferenciaImagemCapa() else 0
    }

    fun getReferenciaImagemLogo(portal: String): Int {
        val demo: Demo? = getDemo(portal)
        return if (demo != null) demo.getReferenciaImagemLogo() else 0
    }

    private fun getDemo(portal: String): Demo? {
        val demos: Array<Demo> = portaisDemo
        for (demo in demos) {
            if (portal == demo.getPortal()) {
                return demo
            }
        }
        return null
    }

    private val portaisDemo: Array<Any>
        private get() = arrayOf<Demo>(
            Demo(
                PORTAL_DEMO, "demo", "demo", arrayOf<String>("210", "88", "39"),
                R.drawable.lg_headers, R.drawable.lg_logo, 50000
            )
        )

    fun getVersaoServidor(portal: String): Int {
        val demo: Demo? = getDemo(portal)
        return if (demo != null) demo.getVersaoServidor() else 0
    }

    fun obterFuncionalidadesDemo(portal: String): List<Funcionalidade> {
        val func: MutableList<Funcionalidade> = ArrayList<Funcionalidade>()
        if (portal == PORTAL_DEMO) {
            func.add(Funcionalidade("FUN.MBSV.001", "Marcação de ponto"))
            //func.add(new Funcionalidade("CONTROLEPONTO", "Controle de ponto"));
            func.add(Funcionalidade("FUN.MBSV.002", "Organograma"))
            func.add(Funcionalidade("FUN.MBSV.005", "Ponto Extensão"))
            func.add(Funcionalidade("FUN.MBSV.009", "Home Page"))
            func.add(Funcionalidade("FUN.MBSV.010", "Paketá Crédito"))
            //func.add(new Funcionalidade("ID_DASHBOARD", "Dashboard"));
            func.add(Funcionalidade("FUN.RHO.002", "Recibo de pagamento"))
            func.add(Funcionalidade("FUN.RHO.004", "Informe de rendimentos"))
            func.add(Funcionalidade("FUN.RHO.006", "Dependentes"))
            func.add(Funcionalidade("FUN.PWKF.000", "Atividades"))
            func.add(Funcionalidade("FUN.PWKF.001", "Funcionalidade de Atividades"))
            func.add(Funcionalidade("FUN.RHOFPR.000", "Relatórios"))
            func.add(Funcionalidade("FUN.RHOFPR.001", "Funcionalidade de Relatórios"))
            func.add(Funcionalidade("FUN.RHOLNK.000", "Links"))
            func.add(Funcionalidade("FUN.RHOLNK.001", "Funcionalidade de Links"))
        }
        return func
    }

    fun obterJSonMenuCustomizavelDemo(context: Context, portal: String): String? {
        var json: String? = null
        if (portal == PORTAL_DEMO) {
            val nomePasta = "JsonsMenuDemo"
            val nomeCompleto = nomePasta + File.separator + "menu_lg.mobile.json"
            val assetManager = context.assets
            try {
                val inputStream = assetManager.open(nomeCompleto)
                json = Scanner(inputStream).useDelimiter("\\A").next()
            } catch (e: IOException) {
                ExceptionUtils.printStackTraceIfDebug(e)
            } catch (e: Exception) {
            }
        }
        return json
    }

    fun entrarModoDemonstracao(activity: Activity, loginListener: LoginListener?) {
        var loginListener: LoginListener? = loginListener
        Sessao.addVersaoFuncional(
            ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_MARCACAO_PONTO,
            4
        )
        Sessao.setPortalAppNorber(null)
        var cor: Array<String?>? = null
        cor = if (ImageUtils.hasTemaPersonalizadoDemonstracao(activity)) {
            ImageUtils.getCoresPorChave(activity, TEMA_PERSONALIZADO)
        } else {
            getCorPortalDemo("demo")
        }
        TelaUtils.setCores(cor)
        if (loginListener == null) {
            loginListener = object : LoginListener() {
                fun onSucessoLogin(colaboradorLogado: Colaborador?, jsonObject: JSONObject?) {
                    val intent = Intent(
                        activity,
                        MainActivity::class.java
                    )
                    ColaboradorSession.saveLogado(colaboradorLogado)
                    ColaboradorSession.saveSelecionado(colaboradorLogado)
                    Sessao.setMinhasAtividadesEmAndamento(true)
                    Sessao.setMinhasAtividadesHistorico(true)
                    activity.startActivity(intent)
                    activity.overridePendingTransition(R.anim.fade_in_logon, R.anim.fade_out_logon)
                    activity.finish()
                }

                fun onFalhaConexao() {}
                fun onIntegradoNorber() {}
                fun onFalhaConexaoIntegracaoNorber(erro: String?) {}
                fun onSucessoPortal(json: String?) {}
                fun onMensagemRetornada(mensagem: String?) {}
                fun onAcaoRequerida(id: String?, descricao: String?, pagina: String?) {}
                fun onValidacaoTermo(matricula: Int, codigoEmpresa: Int) {}
                fun onTrocaSenha(
                    id: String?,
                    descricao: String?,
                    pagina: String?,
                    colaborador: Colaborador?
                ) {
                }

                fun onFinalizadoSegundoFator() {}
            }
        }
        val camposMap = HashMap<String, String>()
        camposMap["usuario"] = "demo"
        camposMap["senha"] = "demo"
        ControladorLogin.getInstancia().autentique(
            "demo", camposMap, loginListener, activity
        )
    }
}


