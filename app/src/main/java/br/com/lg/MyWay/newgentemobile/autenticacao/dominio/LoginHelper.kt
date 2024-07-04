package br.com.lg.MyWay.newgentemobile.autenticacao.dominio

//import br.com.lg.MyWay.newgentemobile.R.customizacoes.dominio.ControladorDiversos
//import br.com.lg.MyWay.newgentemobile.compartilhado.customizacoes.dados.ServicoDiversos
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.WorkerThread
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.autenticacao.dados.PortalAlias
import br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.models.Colaborador
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.TimerSessao
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.Armazenamento
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ConfiguracoesVindasDoServidorUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.NetworkUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.PreferenciasUtil
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.TelaUtils
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import java.util.concurrent.ExecutionException

object LoginHelper {

    const val PATH_DADOS_LOGIN = "/retornoautentique"
    private const val DADOS_PORTAL = "DADOS_PORTAL"
    private const val START_ACTIVITY_PATH = "/start-activity"
    private const val TAG = "MainActivity"
    private var portalAliasRequest: AsyncTask<*, *, *>? = null

    fun cancelarRequisicoes() {
        portalAliasRequest?.cancel(true)
        portalAliasRequest = null
    }

    fun formatarPortalDigitado(portal: String): String {
        return portal.trim().lowercase().replace("https://", "").replace("http://", "")
    }

    fun validarPortal(portal: String): Boolean {
        return portal.isNotEmpty()
    }

    fun buscarDadosDoPortalConsiderandoPortalAlias(
        context: Context,
        portal: String,
        portalListener: PortalListener
    ) {
        if (PortalAlias.isPortalAlias(portal)) {
            if (!NetworkUtils.isConnected(context)) {
                portalListener.onErro(context.getString(R.string.sem_conexao_texto))
                return
            }

            portalAliasRequest =
                PortalAlias.buscarPortal(context, portal, object : PortalAlias.PortalAliasListener {
                    override fun onSucessoObterPortal(portalEfetivo: String, tenant: String) {
                        Armazenamento.armazeneTenant(context, tenant)
                        buscarDadosPortal(context, portal, portalEfetivo, portalListener)
                    }

                    override fun onErroObterPortal(erro: String) {
                        portalListener.onErro(erro)
                    }
                })
        } else {
            buscarDadosPortal(context, portal, portal, portalListener)
        }
    }

    private fun buscarDadosPortal(
        context: Context,
        portalInformado: String,
        portalEfetivo: String,
        portalListener: PortalListener
    ) {
        Armazenamento.recupereTenant(context)?.let { Sessao.tenant.(it) }

        ControladorLogin.getInstancia()
            .buscarDadosPeloPortal(portalEfetivo, object : LoginListener {
                override fun onSucessoLogin(colaboradorLogado: Colaborador) {
                    Console.setLog("EVENTO:sucessoLogin")
                    Console.setLog("Nome: ${colaboradorLogado.nome}")
                }

                override fun onFinalizadoSegundoFator() {}

                override fun onSucessoLogin(
                    colaboradorLogado: Colaborador,
                    jsonObject: JSONObject
                ) {
                }

                override fun onFalhaConexao() {
                    Console.setLog("EVENTO:onFalhaConexao")
                }

                override fun onIntegradoNorber() {}

                override fun onFalhaConexaoIntegracaoNorber(erro: String) {
                    Toast.makeText(context, erro, Toast.LENGTH_LONG).show()
                }

                override fun onSucessoPortal(json: String) {
                    Console.setLog("EVENTO:sucessoPortal")
                    try {
                        val jsonObj = JSONObject(json)
                        ConfiguracoesVindasDoServidorUtils.salvarConfiguracoesVindasDoServidor(
                            context,
                            jsonObj
                        )
                        val dadosPortal = converterJsonParaDadosPortal(jsonObj)
                        dadosPortal.portalEfetivo = portalEfetivo
                        dadosPortal.portalInformado = portalInformado

                        Sessao.aliasPortal(portalInformado)
                        Sessao.setPortalAppNorber(dadosPortal.urlNorber)
                        Sessao.setNovaGeracao(dadosPortal.ehAmbienteNovaGeracao)
                        Sessao.setAmbienteSaaS(dadosPortal.ehAmbienteSaas)
                        Sessao.setCookieLoadBalancerName(dadosPortal.cookieParaBalanceamento)
                        Sessao.setIntervaloDeTentativas(dadosPortal.intervaloDeTentativas)
                        Sessao.setTentativasDeConexao(dadosPortal.tentativasDeConexao)

                        // Segundo fator
                        Sessao.setUrlSegundoFator(dadosPortal.UrlSegundoFator)
                        Sessao.setSegundoFator(dadosPortal.SegundoFator)

                        // Habilitar portal AA no mobile
                        Sessao.setHabilitarNovoPortalAutoatendimentoMobile(dadosPortal.habilitarNovoPortalAutoatendimentoMobile)
                        Sessao.setUrlAutenticacaoManualAutoatendimento(dadosPortal.urlAutenticacaoManualAutoatendimento)
                        Sessao.setUrlAutenticacaoSamlAutoatendimento(dadosPortal.urlAutenticacaoSamlAutoatendimento)
                        Sessao.setUtilizarMarcacaoPontoNativaNaWebViewPortalAA(dadosPortal.utilizarMarcacaoPontoNativaNaWebViewPortalAA)

                        dadosPortal.campoAmbiente?.let {
                            Sessao.setAmbienteId(it.id)
                            Sessao.setAmbienteDiretorio(it.diretorioVirtual)
                            Sessao.setAmbienteTenant(it.tenant)
                        }

                        portalListener.onSucesso(dadosPortal)
                    } catch (e: Exception) {
                        portalListener.onErro(json)
                    }
                }

                override fun onMensagemRetornada(mensagem: String) {
                    Console.setLog("EVENTO:onMensagemRetornada - $mensagem")
                }

                override fun onAcaoRequerida(id: String, descricao: String, pagina: String) {
                    Console.setLog("EVENTO:onAcaoRequerida - $descricao")
                }

                override fun onValidacaoTermo(matricula: Int, codigoEmpresa: Int) {}

                override fun onTrocaSenha(
                    id: String,
                    descricao: String,
                    pagina: String,
                    colaborador: Colaborador
                ) {
                }

                @WorkerThread
                override fun sendStartActivityMessage(node: String) {
                    val sendMessageTask: Task<Int> = Wearable.getMessageClient(context)
                        .sendMessage(node, START_ACTIVITY_PATH, byteArrayOf())

                    try {
                        val result: Int = Tasks.await(sendMessageTask)
                        Log.d(TAG, "Task succeeded: $result")
                    } catch (exception: ExecutionException) {
                        Log.d(TAG, "Task failed: $exception")
                    } catch (interruptedException: InterruptedException) {
                        Log.d(TAG, "Interrupt occurred: $interruptedException")
                    }
                }

                @WorkerThread
                private fun getNodes(): Collection<String> {
                    val results: MutableSet<String> = HashSet()

                    try {
                        val nodes: List<Node> =
                            Tasks.await(Wearable.getNodeClient(context).connectedNodes)
                        for (node in nodes) {
                            results.add(node.id)
                        }
                    } catch (exception: ExecutionException) {
                        Log.d(TAG, "Task failed: $exception")
                    } catch (interruptedException: InterruptedException) {
                        Log.d(TAG, "Interrupt occurred: $interruptedException")
                    }

                    return results
                }
            }, context)
    }

    @Throws(JSONException::class)
    fun converterJsonParaDadosPortal(jsonObj: JSONObject): DadosPortal {
        val dadosPortal = DadosPortal()
        dadosPortal.corPersonalizada = jsonObj.optString("CorBase", "")
        dadosPortal.corPersonalizadaFundo = jsonObj.optString("CorFundo", null) ?: null
        dadosPortal.corPersonalizadaFonte = jsonObj.optString("CorFonte", null) ?: jsonObj.optString("CorBase", null)

        if (!jsonObj.isNull("CorFonteControles")) {
            dadosPortal.corPersonalizadaFonteControles = jsonObj.optString("CorFonteControles", "")
            dadosPortal.novatematizacao = true
        } else {
            dadosPortal.corPersonalizadaFonteControles = jsonObj.optString("CorBase", "")
        }

        dadosPortal.urlLogo = jsonObj.optString("UrlLogo", "")
        if (!jsonObj.isNull("UrlFundo")) {
            dadosPortal.urlFundo = jsonObj.optString("UrlFundo", "")
        }

        dadosPortal.tempoSessao = jsonObj.optInt("TempoSessao", 0)
        dadosPortal.permiteAlterarBanner = jsonObj.optBoolean("PermitePersonalizarHeader", true)
        dadosPortal.urlBanner = jsonObj.optString("UrlHeaderPadrao", "")
        dadosPortal.permiteLoginAutomatico = jsonObj.optBoolean("PermiteLoginAutomatico", true)
        dadosPortal.versaoServidor = jsonObj.optInt("VersaoServidor", 1)
        dadosPortal.rotuloCampoUsuario = jsonObj.optString("RotuloCampoUsuario", "")
        dadosPortal.ipServidor = jsonObj.optString("ServidorIp", "")
        if (dadosPortal.rotuloCampoUsuario.equals("null", ignoreCase = true)) {
            dadosPortal.rotuloCampoUsuario = ""
        }
        dadosPortal.idAmbiente = jsonObj.optString("IdAmbiente", "")
        dadosPortal.urlNorber = jsonObj.optString("UrlAppNorber", "")
        dadosPortal.ehAmbienteNovaGeracao = jsonObj.optBoolean("EhAmbienteNovaGeracao", false)
        dadosPortal.ehAmbienteSaas = jsonObj.optBoolean("EhAmbienteSaaS", false)
        dadosPortal.cookieParaBalanceamento = jsonObj.optString("CookieParaBalanceamento", "")
        dadosPortal.tentativasDeConexao = jsonObj.optInt("TentativasDeConexao", 3)
        dadosPortal.intervaloDeTentativas = jsonObj.optInt("IntervaloDeTentativas", 1)
        // Segundo fator
        dadosPortal.SegundoFator = jsonObj.optBoolean("SegundoFator", false)
        dadosPortal.UrlSegundoFator = jsonObj.optString("UrlSegundoFator", "")
        // Configurações para exibição do portal AA no mobile
        dadosPortal.habilitarNovoPortalAutoatendimentoMobile = jsonObj.optBoolean("HabilitarNovoPortalAutoatendimentoMobile", false)
        dadosPortal.utilizarMarcacaoPontoNativaNaWebViewPortalAA = jsonObj.optBoolean("UtilizarGeolocalizacaoMarcacaoPontoNovoPortalAutoatendimentoMobile", false)
        dadosPortal.urlAutenticacaoManualAutoatendimento = jsonObj.optString("UrlAutenticacaoManualAutoatendimento", "")
        dadosPortal.urlAutenticacaoSamlAutoatendimento = jsonObj.optString("UrlAutenticacaoSamlAutoatendimento", "")

        try {
            val camposAutJsArray = jsonObj.getJSONArray("CamposAutenticacao")
            val campoAutenticacaoList = mutableListOf<CampoAutenticacao>()

            for (i in 0 until camposAutJsArray.length()) {
                val campoAutJsObj = camposAutJsArray.getJSONObject(i)
                val campoAut = CampoAutenticacao(
                    campoAutJsObj.getString("Id"),
                    campoAutJsObj.getString("Nome"),
                    campoAutJsObj.getString("Tipo")
                )
                campoAutenticacaoList.add(campoAut)
            }
            dadosPortal.campoAutenticacaoList = campoAutenticacaoList
        } catch (ex: Exception) {
            // Handle exception
        }

        try {
            val camposAmb = jsonObj.getJSONObject("AmbienteConexao")
            val campoAmb = CampoAmbiente(
                camposAmb.optString("Id", ""),
                camposAmb.optString("Oid", ""),
                camposAmb.optString("Nome", ""),
                camposAmb.optString("DiretorioVirtual", ""),
                camposAmb.optString("Tenant", "")
            )
            dadosPortal.campoAmbiente = campoAmb
        } catch (ex: Exception) {
            // Handle exception
        }

        return dadosPortal
    }

    fun realizarConfiguracoesAoReceberDadosPortal(
        context: Context,
        dadosPortal: br.com.lg.MyWay.autenticacao.dominio.LoginHelper.DadosPortal
    ) {
        Armazenamento.salvarDadosPortal(context, dadosPortal)
        realizarConfiguracoesDoAppComDadosPortal(context, dadosPortal)
    }

    fun realizarConfiguracoesDoAppComDadosPortal(
        context: Context,
        dadosPortal: DadosPortal
    ) {
        try {
            if (dadosPortal.corPersonalizada == null) TelaUtils.setCores(null) else TelaUtils.setCores(
                dadosPortal.corPersonalizada!!.substring(4).replace(")", "").split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray())
            if (dadosPortal.corPersonalizadaFundo == null) TelaUtils.setCoresfundo(null) else if (context.packageName.contains(
                    "Santander"
                )
            ) {
                dadosPortal.corPersonalizadaFundo = "rgb(236,0,0)"
            } else TelaUtils.setCoresfundo(
                dadosPortal.corPersonalizadaFundo!!.substring(4).replace(")", "").split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray())
            if (dadosPortal.corPersonalizadaFonte == null) TelaUtils.setCoresfonte(null) else TelaUtils.setCoresfonte(
                dadosPortal.corPersonalizadaFonte.substring(4).replace(")", "").split(",".toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray())
            if (dadosPortal.corPersonalizadaFonteControles == null) TelaUtils.setCoresfontecontrole(
                null
            ) else TelaUtils.setCoresfontecontrole(
                dadosPortal.corPersonalizadaFonteControles.substring(
                    4
                ).replace(")", "").split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray())
        } catch (e: java.lang.Exception) {
        }
        Sessao.setVersaoServidor(dadosPortal.versaoServidor)
        Sessao.setIpServidor(dadosPortal.ipServidor)
        Sessao.setPermitirLoginAutomatico(context, dadosPortal.permiteLoginAutomatico)
        TimerSessao.setTempoDaSessaoEmSegundos(dadosPortal.tempoSessao)
        AjustesFragment.setPermitePersonalizarHeader(context, dadosPortal.permiteAlterarBanner)
        TelaUtils.setUrlImagemBannerCapa(context, dadosPortal.urlBanner)
    }

    fun inserirImagemLogo(
        context: Context?,
        url: String?,
        imvLogo: ImageView,
        callback: Runnable?
    ) {
        ControladorDiversos.getInstancia()
            .obterImagemLogo(context, url, object : Listener<Drawable?>() {
                fun onFinalizado(drawable: Drawable?) {
                    if (drawable != null) {
                        imvLogo.setImageDrawable(drawable)
                    }
                    executarCallback()
                }

                private fun executarCallback() {
                    callback?.run()
                }
            })
    }


    fun excluirOuManterValoresAutenticacao(
        context: Context?,
        portalEfetivoSalvo: String,
        portalEfetivoRecebido: String
    ) {
        if (portalEfetivoRecebido != portalEfetivoSalvo) {
            Armazenamento.excluirValoresCamposAutenticacao(context)
            PreferenciasUtil.setManterLogado(context, false)
        }
    }

    fun excluirDadosPortalEAutenticacao(context: Context?) {
        Armazenamento.excluirValoresCamposAutenticacao(context)
        Armazenamento.salvarDadosPortal(context, null)
    }


    interface PortalListener {
        fun onSucesso(dadosPortal: DadosPortal?)
        fun onErro(erro: String?)
    }


    class DadosPortal : Serializable {
        var portalInformado: String? = null
        var portalEfetivo: String? = null
        var corPersonalizada: String? = null
        var corPersonalizadaFundo: String? = null
        var corPersonalizadaFonte: String? = null
        var corPersonalizadaFonteControles: String? = null
        var novatematizacao = false
        var urlLogo: String? = null
        var urlFundo: String? = null
        var urlBanner: String? = null
        var permiteAlterarBanner = false
        var permiteLoginAutomatico = false
        var ehAmbienteSaas = false
        var ehAmbienteNovaGeracao = false
        var tempoSessao = 0
        var versaoServidor = 0
        var rotuloCampoUsuario: String? = null
        var ipServidor: String? = null
        var idAmbiente: String? = null
        var urlNorber: String? = null
        var cookieParaBalanceamento: String? = null
        var UrlSegundoFator: String? = null
        var tentativasDeConexao = 0
        var intervaloDeTentativas = 0
        var SegundoFator = false
        var habilitarNovoPortalAutoatendimentoMobile = false
        var urlAutenticacaoManualAutoatendimento: String? = null
        var urlAutenticacaoSamlAutoatendimento: String? = null
        var campoAutenticacaoList: List<CampoAutenticacao>? = null
        var campoAmbiente: CampoAmbiente? = null
        var utilizarMarcacaoPontoNativaNaWebViewPortalAA = false
    }


    class CampoAutenticacao : Serializable {
        var id: String? = null
        var nome: String? = null
        var tipo: String? = null
    }


    class CampoAmbiente : Serializable {
        var id: String? = null
        var diretorioVirtual: String? = null
        var oid: String? = null
        var nome: String? = null
        var tenant: String? = null
    }

}