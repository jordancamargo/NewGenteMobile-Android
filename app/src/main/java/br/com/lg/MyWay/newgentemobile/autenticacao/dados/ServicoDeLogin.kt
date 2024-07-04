package br.com.lg.MyWay.newgentemobile.autenticacao.dados

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
//import br.com.lg.MyWay.newgentemobile.BuildConfig
import br.com.lg.MyWay.newgentemobile.autenticacao.ui.splash_screen.SplashScreen
import br.com.lg.MyWay.newgentemobile.autenticacao.ui.splash_screen.SplashScreenSantander
import br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.ServicoDeColaborador
import br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.models.Colaborador
import br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.models.Contrato
import br.com.lg.MyWay.newgentemobile.compartilhado.componentes_de_android.LGApplication
import br.com.lg.MyWay.newgentemobile.compartilhado.customizacoes.dominio.DefinicoesCustomizacoes
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.ColaboradorSession
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Empresa
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Funcionalidade
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao
import br.com.lg.MyWay.newgentemobile.compartilhado.network.Conexao
import br.com.lg.MyWay.newgentemobile.compartilhado.network.models.EnumTipoRequisicao
import br.com.lg.MyWay.newgentemobile.compartilhado.network.models.Requisicao
import br.com.lg.MyWay.newgentemobile.compartilhado.network.models.Servico
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ConfiguracoesVindasDoServidorUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ExceptionUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.JsonUtils
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.PreferenciasUtil
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.StringAndTextUtils
import br.com.lg.MyWay.newgentemobile.configuracoes.atualizacao.ServicoUpdateVersion
import br.com.lg.MyWay.newgentemobile.configuracoes.atualizacao.Versao
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.CriptografiaUtil
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.GerenciadorDeCriptografiaDeServico
import br.com.lg.MyWay.newgentemobile.funcionalidades.portal_aa.dados.PortalAAListener
import com.dynatrace.android.agent.Dynatrace
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.json.JSONException
import org.json.JSONObject
import java.util.LinkedList


class ServicoDeLogin : Servico() {
    private var listener: LoginListener? = null
    private var portalAAListener: PortalAAListener? = null
    private var listnerSplash: SplashScreen? = null
    private var listnerSplashSantander: SplashScreenSantander? = null
    private val context: Context = LGApplication.instance!!.applicationContext
    fun executeAutenticao(
        portal: String?,
        campos: HashMap<String?, String?>,
        listener: LoginListener?
    ) {
        this.listener = listener
        if (portal != null) {
            Sessao.setPortal(portal)
        }
        var params: MutableList<NameValuePair> = LinkedList()
        val it: Iterator<*> = campos.entries.iterator()
        while (it.hasNext()) {
            val (key, value1) = it.next() as Map.Entry<*, *>
            val id = key as String
            val value = value1 as String
            params.add(BasicNameValuePair(id, value))
            if (DefinicoesCustomizacoes.isVersaoSantander() && id == "usuario") {
                Dynatrace.identifyUser(value)
            }
        }
        params.add(BasicNameValuePair("versaoCliente", Versao.ULTIMA + ""))
        params.add(BasicNameValuePair("idTenant", Sessao.tenant))
        params.add(BasicNameValuePair("confiavel", "true")) // Por padrão os devices são confiáveis.
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/Autentique",
                params,
                EnumTipoRequisicao.LOGIN
            )
        } else {
            params = GerenciadorDeCriptografiaDeServico.criptografarDadosSeEstiverHabilitado(params)
            requisicao = Requisicao(
                "Autenticacao/Autentique",
                params,
                EnumTipoRequisicao.LOGIN
            )
        }
        execute(requisicao)
    }

    fun executeIntegracaoNorber(listener: LoginListener?) {
        this.listener = listener
        var params: MutableList<NameValuePair> = LinkedList()
        params.add(BasicNameValuePair("idSessao", Sessao.getIdSessao()))
        params.add(BasicNameValuePair("idTenant", Sessao.getTenant()))
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/GereIntegracao",
                params,
                EnumTipoRequisicao.INTEGRACAONORBER
            )
        } else {
            params = GerenciadorDeCriptografiaDeServico.criptografarDadosSeEstiverHabilitado(params)
            requisicao = Requisicao(
                "Autenticacao/GereIntegracao",
                params,
                EnumTipoRequisicao.INTEGRACAONORBER
            )
        }
        execute(requisicao)
    }

    fun executeAutenticaoDireto(
        portal: String?,
        campos: Map<String?, CharArray?>,
        listener: SplashScreen?
    ) {
        listnerSplash = listener
        Sessao.setPortal(portal)
        var params: MutableList<NameValuePair> = LinkedList()
        val it: Iterator<*> = campos.entries.iterator()
        while (it.hasNext()) {
            val (key, value1) = it.next() as Map.Entry<*, *>
            var value: String?
            val id = key as String
            value = try {
                value1 as String?
            } catch (e: Exception) {
                String((value1 as CharArray?)!!)
            }
            params.add(BasicNameValuePair(id, value))
            if (DefinicoesCustomizacoes.isVersaoSantander() && id == "usuario") {
                Dynatrace.identifyUser(value)
            }
        }
        addParametroDispositivoConfiavelSFA(params)
        params.add(BasicNameValuePair("versaoCliente", Versao.ULTIMA + ""))
        params.add(BasicNameValuePair("idTenant", Sessao.getTenant()))
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/Autentique",
                params,
                EnumTipoRequisicao.LOGIN
            )
        } else {
            params = GerenciadorDeCriptografiaDeServico.criptografarDadosSeEstiverHabilitado(params)
            requisicao = Requisicao(
                "Autenticacao/Autentique",
                params,
                EnumTipoRequisicao.LOGIN
            )
        }
        execute(requisicao)
    }

    fun executeAutenticaoDiretoSantander(
        portal: String?,
        campos: Map<String?, CharArray?>,
        listener: SplashScreenSantander?
    ) {
        listnerSplashSantander = listener
        Sessao.setPortal(portal)
        var params: MutableList<NameValuePair> = LinkedList()
        val it: Iterator<*> = campos.entries.iterator()
        while (it.hasNext()) {
            val (key, value1) = it.next() as Map.Entry<*, *>
            var value: String?
            val id = key as String
            value = try {
                value1 as String?
            } catch (e: Exception) {
                String((value1 as CharArray?)!!)
            }
            params.add(BasicNameValuePair(id, value))
            if (DefinicoesCustomizacoes.isVersaoSantander() && id == "usuario") {
                Dynatrace.identifyUser(value)
            }
        }
        params.add(BasicNameValuePair("versaoCliente", Versao.ULTIMA + ""))
        params.add(BasicNameValuePair("idTenant", Sessao.getTenant()))
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/Autentique",
                params,
                EnumTipoRequisicao.LOGIN
            )
        } else {
            params = GerenciadorDeCriptografiaDeServico.criptografarDadosSeEstiverHabilitado(params)
            requisicao = Requisicao(
                "Autenticacao/Autentique",
                params,
                EnumTipoRequisicao.LOGIN
            )
        }
        execute(requisicao)
    }

    fun executeAutenticaoSAML(portal: String?, dados: String?, listener: LoginListener?) {
        this.listener = listener
        Sessao.setPortal(portal)
        val params: MutableList<NameValuePair> = LinkedList()
        params.add(BasicNameValuePair("dados", dados))
        params.add(BasicNameValuePair("versaoCliente", Versao.ULTIMA + ""))
        params.add(BasicNameValuePair("idTenant", Sessao.getTenant()))
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/AutentiqueSAML",
                params,
                EnumTipoRequisicao.LOGIN
            )
        } else {
            requisicao = Requisicao(
                "Autenticacao/AutentiqueSAML",
                params,
                EnumTipoRequisicao.LOGIN
            )
        }
        execute(requisicao)
    }

    fun executeAutenticaoSegundoFator(portal: String?, dados: String?, listener: LoginListener?) {
        this.listener = listener
        Sessao.setPortal(portal)
        val params: MutableList<NameValuePair> = LinkedList()
        params.add(BasicNameValuePair("dados", dados))
        params.add(BasicNameValuePair("versaoCliente", Versao.ULTIMA + ""))
        params.add(BasicNameValuePair("idTenant", Sessao.getTenant()))
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/ValideSegundoFator",
                params,
                EnumTipoRequisicao.LOGINSEGUNDOFATOR
            )
        } else {
            requisicao = Requisicao(
                "Autenticacao/ValideSegundoFator",
                params,
                EnumTipoRequisicao.LOGINSEGUNDOFATOR
            )
        }
        execute(requisicao)
    }

    fun recupereSessaoParaPortalAA(portal: String?, dados: String?, listener: PortalAAListener?) {
        portalAAListener = listener
        Sessao.setPortal(portal)
        val params: MutableList<NameValuePair> = LinkedList()
        params.add(BasicNameValuePair("dados", dados))
        params.add(BasicNameValuePair("versaoCliente", Versao.ULTIMA + ""))
        //            params.add(new BasicNameValuePair("idTenant", Sessao.getTenant()));
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                val paramName = params[i].name
                if (paramName != "idTenant" && paramName != "idSessao") {
                    params.removeAt(i)
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/RecupereSessao",
                params,
                EnumTipoRequisicao.RECUPERA_SESSAO_SA3
            )
        } else {
            requisicao = Requisicao(
                "Autenticacao/RecupereSessao",
                params,
                EnumTipoRequisicao.RECUPERA_SESSAO_SA3
            )
        }
        execute(requisicao)
    }

    fun valideCookieBalance() {
        var params: MutableList<NameValuePair> = LinkedList()
        params.add(BasicNameValuePair("idSessao", Sessao.getIdSessao()))
        params = GerenciadorDeCriptografiaDeServico.criptografarDadosSeEstiverHabilitado(params)
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/ValideCookieBalance",
                params,
                EnumTipoRequisicao.VALIDA_COOKIE_LOAD_BALANCE
            )
        } else {
            requisicao = Requisicao(
                "Autenticacao/ValideCookieBalance",
                params,
                EnumTipoRequisicao.VALIDA_COOKIE_LOAD_BALANCE
            )
        }
        execute(requisicao)
    }

    fun buscarDadosDoClientePeloPortal(portal: String?, listener: LoginListener?) {
        this.listener = listener
        Sessao.setPortal(portal)
        Sessao.setVersaoServidor(Versao.DESCONHECIDA)
        buscarDadosDoClientePeloPortal(Versao.DESCONHECIDA)
    }

    private fun buscarDadosDoClientePeloPortal(versaoDoServidorCompativel: Int) {
        val params: MutableList<NameValuePair> = LinkedList()
        params.add(BasicNameValuePair("identificador", "LOGARAUTOMATICO"))
        params.add(BasicNameValuePair("idTenant", Sessao.getTenant()))
        val requisicao: Requisicao
        val ParametrosJson = gerarJsonNovaCriptografia(params)
        for (i in params.indices) {
            if (params[i].name != "idTenant") {
                if (params[i].name != "idSessao") {
                    params.removeAt(i)
                }
            }
        }
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) < GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            requisicao = Requisicao(
                "Autenticacao/" + SERVICO_CONFIGURACOESLOGIN, params,
                EnumTipoRequisicao.LOGIN_PORTAL
            )
            requisicao.setVersaoServicoCompativel(versaoDoServidorCompativel)
        } else {
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/" + SERVICO_CONFIGURACOESLOGIN, params,
                EnumTipoRequisicao.LOGIN_PORTAL
            )
            requisicao.setVersaoServicoCompativel(versaoDoServidorCompativel)
        }
        execute(requisicao)
    }

    private fun gerarJsonNovaCriptografia(lista: List<NameValuePair>): String {
        val jsonObject = JSONObject()
        try {
            for (i in lista.indices) {
                jsonObject.put(lista[i].name, lista[i].value)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }

    fun executeFinalizeSessao() {
        if (Sessao.getIdSessao() == null) { // não existe sessao, logo, não faremos nada.
            return
        }
        val params: MutableList<NameValuePair> = LinkedList()
        params.add(BasicNameValuePair("idSessao", Sessao.getIdSessao()))
        params.add(BasicNameValuePair("idTenant", Sessao.getTenant()))
        val requisicao: Requisicao
        if (Sessao.getVersaoFuncional(
                ConfiguracoesVindasDoServidorUtils.VERSAO_FUNCIONAL_CRIPTOGRAFIA
            ) >= GerenciadorDeCriptografiaDeServico.CRIPTOGRAFIA_GERALV5
        ) {
            val ParametrosJson = gerarJsonNovaCriptografia(params)
            for (i in params.indices) {
                if (params[i].name != "idTenant") {
                    if (params[i].name != "idSessao") {
                        params.removeAt(i)
                    }
                }
            }
            params.add(
                BasicNameValuePair(
                    "payload",
                    CriptografiaUtil.criptografarAES(ParametrosJson)
                )
            )
            requisicao = Requisicao(
                "AutenticacaoGenteMobile/FinalizeSessao",
                params,
                EnumTipoRequisicao.FINALIZAR_SESSAO
            )
        } else {
            requisicao = Requisicao(
                "Autenticacao/FinalizeSessao",
                params,
                EnumTipoRequisicao.FINALIZAR_SESSAO
            )
        }
        execute(requisicao)
    }

    fun onRequisicaoCompletada(tipo: EnumTipoRequisicao?, respostaJson: String) {
//        if (listener == null)
//            return;
        when (tipo) {
            LOGIN -> try {
                if (listnerSplash != null) trataLoginDireto(respostaJson) else if (listener != null) trataLogin(
                    respostaJson
                ) else if (listnerSplashSantander != null) trataLoginDiretoSantander(respostaJson) else return
            } catch (e: Exception) {
                e.printStackTrace()
                FirebaseCrashlytics.getInstance().log(e.message!!)
            }

            LOGINSEGUNDOFATOR -> try {
                if (listener != null) {
                    Sessao.setSegundoFator(false)
                    trataLoginSegundoFator(respostaJson)
                } else return
            } catch (e: Exception) {
                e.printStackTrace()
                FirebaseCrashlytics.getInstance().log(e.message!!)
            }

            FINALIZAR_SESSAO -> trataFinalizeSessao(respostaJson)
            LOGIN_PORTAL -> trataPortal(respostaJson)
            VALIDA_COOKIE_LOAD_BALANCE -> System.err.println(respostaJson)
            INTEGRACAONORBER -> tratarintegracao(respostaJson)
            RECUPERA_SESSAO_SA3 -> trataRecuperacaoDaSessao(respostaJson)
            else -> {
                listener.onFalhaConexao()
                System.err.println("Servico de login - O callback foi chamado mas não existe tratamento para o retorno da consulta")
            }
        }
    }

    private fun trataRecuperacaoDaSessao(respostaJson: String) {
        if (respostaJson == Conexao.GENTE_MOBILE_ERRO_CONEXAO) {
            portalAAListener.onFalhaConexao()
            return
        }
        val colaborador: Colaborador
        val json: JSONObject
        try {
            json = JSONObject(respostaJson)

            //TODO: Adaptar os campos a seguir para o modelo retornado pelo json de resposta.
            val retornoAutenticao = JSONObject(json.getString("RetornoAutenticao"))
            val id = retornoAutenticao.optString("Id", "")
            val mensagemErro = retornoAutenticao.getString("Descricao")
            when (id) {
                LoginListener.AUTENTICADO -> {
                    colaborador = montaColaborador(json)
                    ColaboradorSession.saveLogado(colaborador)
                    ColaboradorSession.saveSelecionado(colaborador)
                    portalAAListener.onSucessoRecuperacaoSessao()
                    if (mensagemErro.contains("sem permissão para acessar o mobile")) portalAAListener.onMensagemRetornada(
                        mensagemErro
                    )
                }

                LoginListener.NAOAUTENTICADO -> portalAAListener.onFalhaRecuperacaoSessao(
                    mensagemErro
                )
            }
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
            portalAAListener.onFalhaConexao()
        }
    }

    private fun trataPortal(respostaJson: String) {

        //
        // Se não sabemos ainda qual a versão do server,
        // E a primeira busca de configurações foi realizada
        // para o padrão de URL atual (/Produtos/Mobile/Autenticacao)
        // E falhou, vamos tentar buscar as configurações no padrão de
        // url antigo, onde o MyWay mobile possuía WebSite próprio no IIS
        //
        val checaJson: Boolean = !JsonUtils.ehJsonValido(respostaJson)
        val versaoServer: Int = Sessao.getVersaoServidor()
        if (checaJson && versaoServer == Versao.DESCONHECIDA) {
            Sessao.setVersaoServidor(Versao.ULTIMA_COM_WEBSITE_PROPRIO)
            buscarDadosDoClientePeloPortal(Versao.ULTIMA_COM_WEBSITE_PROPRIO)
        } else {
            listener.onSucessoPortal(respostaJson)
        }
    }

    private fun tratarintegracao(respostaJson: String) {
        val retorno = respostaJson.replace("\"", "")
        val retornofinal = retorno.replace("\n", "")
        Sessao.setIdSessaoNorber(retornofinal)
        listener.onIntegradoNorber()
    }

    private fun trataLogin(respostaJson: String) {
        if (respostaJson == Conexao.GENTE_MOBILE_ERRO_CONEXAO) {
            listener.onFalhaConexao()
            return
        }

        // para manter o mesmo comportamento em versões sem a melhoria no servidor (que sempre mostrava mensagem de erro em usuário e senha independente do erro real), foi necessário manter assim, visto que a melhoria foi feita em uma versão 2014.2 e não em todas as já lançadas intermediárias das 2015.1, 2015.2 e 2016.1
        if ("" == respostaJson) {
            listener.onMensagemRetornada("Não foi possível efetuar o login com o usuário/senha informados. Verifique as informações e tente novamente.")
            return
        }
        var colaborador: Colaborador
        val json: JSONObject
        try {
            json = JSONObject(respostaJson)
            try {
                val retornoAutenticao = JSONObject(json.getString("RetornoAutenticao"))
                val id = retornoAutenticao.optString("Id", "")
                when (id) {
                    LoginListener.AUTENTICADO -> throw Exception()
                    LoginListener.INFORMACAO_TROCA_SENHA -> {
                        colaborador = montaColaborador(json)
                        listener.onTrocaSenha(
                            id,
                            retornoAutenticao.optString("Descricao", ""),
                            retornoAutenticao.optString("Pagina", ""),
                            colaborador
                        )
                        return
                    }

                    LoginListener.TROCA_SENHA, LoginListener.ESQUECEU_SENHA, LoginListener.PRIMEIRO_ACESSO, LoginListener.PRIMEIRO_ACESSO_REDIRECIONAMENTO -> {
                        val descricao = retornoAutenticao.optString("Descricao", "")
                        val pagina = retornoAutenticao.optString("Pagina", "")
                        listener.onAcaoRequerida(id, descricao, pagina)
                        return
                    }

                    LoginListener.ACEITAR_TERMO -> {
                        trataAceiteTermo(respostaJson)
                        return
                    }

                    LoginListener.SEGUNDO_FATOR -> {
                        Sessao.setUrlSegundoFator(retornoAutenticao.optString("Pagina", ""))
                        Sessao.setSegundoFator(true)
                        throw Exception()
                    }
                }
                val mensagemErro = retornoAutenticao.getString("Descricao")
                listener.onMensagemRetornada(mensagemErro)
                return
            } catch (e: Exception) {
            }
            colaborador = montaColaborador(json)
            val jsonObj = JSONObject(json.toString())
            listener.onSucessoLogin(colaborador, jsonObj)
            return
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        listener.onFalhaConexao()
    }

    @SuppressLint("MissingPermission")
    private fun trataLoginSegundoFator(respostaJson: String) {
        if (respostaJson == Conexao.GENTE_MOBILE_ERRO_CONEXAO) {
            listener.onFalhaConexao()
            return
        }

        // para manter o mesmo comportamento em versões sem a melhoria no servidor (que sempre mostrava mensagem de erro em usuário e senha independente do erro real), foi necessário manter assim, visto que a melhoria foi feita em uma versão 2014.2 e não em todas as já lançadas intermediárias das 2015.1, 2015.2 e 2016.1
        if ("" == respostaJson) {
            listener.onMensagemRetornada("Não foi possível efetuar o login com o usuário/senha informados. Verifique as informações e tente novamente.")
            return
        }
        val colaborador: Colaborador
        var json: JSONObject? = null
        try {
            json = JSONObject(respostaJson)
            colaborador = montaColaborador(json)
            PreferenciasUtil.setSegundoFator(
                context,
                "confiavel" + "_" + colaborador.getEmpresa()
                    .getCodigo() + "_" + colaborador.getMatricula(),
                true
            )
            listener.onFinalizadoSegundoFator()
            return
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        listener.onFalhaConexao()
    }

    private fun trataLoginDireto(respostaJson: String) {
        var retornoAutenticao: JSONObject? = null
        if (respostaJson == Conexao.GENTE_MOBILE_ERRO_CONEXAO) {
            listener.onFalhaConexao()
            return
        }

        // para manter o mesmo comportamento em versões sem a melhoria no servidor (que sempre mostrava mensagem de erro em usuário e senha independente do erro real), foi necessário manter assim, visto que a melhoria foi feita em uma versão 2014.2 e não em todas as já lançadas intermediárias das 2015.1, 2015.2 e 2016.1
        if ("" == respostaJson) {
            listener.onMensagemRetornada("Não foi possível efetuar o login com o usuário/senha informados. Verifique as informações e tente novamente.")
            return
        }
        var colaborador: Colaborador
        var empresa: Empresa
        var json: JSONObject? = null
        try {
            json = JSONObject(respostaJson)
            try {
                retornoAutenticao = JSONObject(json.getString("RetornoAutenticao"))
                val id = retornoAutenticao.optString("Id", "")
                when (id) {
                    LoginListener.AUTENTICADO -> throw Exception()
                    LoginListener.INFORMACAO_TROCA_SENHA -> {
                        colaborador = montaColaborador(json)
                        listnerSplash.onTrocaSenha(
                            id,
                            retornoAutenticao.optString("Descricao", ""),
                            retornoAutenticao.optString("Pagina", ""),
                            colaborador
                        )
                        return
                    }

                    LoginListener.TROCA_SENHA, LoginListener.ESQUECEU_SENHA, LoginListener.PRIMEIRO_ACESSO, LoginListener.PRIMEIRO_ACESSO_REDIRECIONAMENTO -> {
                        val descricao = retornoAutenticao.optString("Descricao", "")
                        val pagina = retornoAutenticao.optString("Pagina", "")
                        listnerSplash.onAcaoRequerida(id, descricao, pagina)
                        return
                    }

                    LoginListener.ACEITAR_TERMO -> {
                        trataAceiteTermo(respostaJson)
                        return
                    }

                    LoginListener.SEGUNDO_FATOR -> {
                        Sessao.setUrlSegundoFator(retornoAutenticao.optString("Pagina", ""))
                        Sessao.setSegundoFator(true)
                        throw Exception()
                    }
                }
                val mensagemErro = retornoAutenticao.getString("Descricao")
                listnerSplash.onMensagemRetornada(mensagemErro)
                return
            } catch (e: Exception) {
            }
            colaborador = montaColaborador(json)
            val jsonObj = JSONObject(json.toString())
            listnerSplash.onSucessoLogin(colaborador, jsonObj)
            return
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        try {
            listnerSplash.onFalhaConexao(retornoAutenticao!!.getString("Descricao"))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun trataLoginDiretoSantander(respostaJson: String) {
        if (respostaJson == Conexao.GENTE_MOBILE_ERRO_CONEXAO) {
            listener.onFalhaConexao()
            return
        }

        // para manter o mesmo comportamento em versões sem a melhoria no servidor (que sempre mostrava mensagem de erro em usuário e senha independente do erro real), foi necessário manter assim, visto que a melhoria foi feita em uma versão 2014.2 e não em todas as já lançadas intermediárias das 2015.1, 2015.2 e 2016.1
        if ("" == respostaJson) {
            listener.onMensagemRetornada("Não foi possível efetuar o login com o usuário/senha informados. Verifique as informações e tente novamente.")
            return
        }
        var colaborador: Colaborador
        var empresa: Empresa
        var json: JSONObject? = null
        try {
            json = JSONObject(respostaJson)
            try {
                val retornoAutenticao = JSONObject(json.getString("RetornoAutenticao"))
                val id = retornoAutenticao.optString("Id", "")
                when (id) {
                    "autenticado" -> throw Exception()
                    LoginListener.INFORMACAO_TROCA_SENHA -> {
                        colaborador = montaColaborador(json)
                        listener.onTrocaSenha(
                            id,
                            retornoAutenticao.optString("Descricao", ""),
                            retornoAutenticao.optString("Pagina", ""),
                            colaborador
                        )
                        return
                    }

                    LoginListener.TROCA_SENHA, LoginListener.ESQUECEU_SENHA, LoginListener.PRIMEIRO_ACESSO, LoginListener.PRIMEIRO_ACESSO_REDIRECIONAMENTO -> {
                        val descricao = retornoAutenticao.optString("Descricao", "")
                        val pagina = retornoAutenticao.optString("Pagina", "")
                        listener.onAcaoRequerida(id, descricao, pagina)
                        return
                    }

                    LoginListener.ACEITAR_TERMO -> {
                        trataAceiteTermo(respostaJson)
                        return
                    }
                }
                val mensagemErro = retornoAutenticao.getString("Descricao")
                listener.onMensagemRetornada(mensagemErro)
                return
            } catch (e: Exception) {
            }
            colaborador = montaColaborador(json)
            listnerSplashSantander.onSucessoLogin(colaborador)
            return
        } catch (e: JSONException) {
            ExceptionUtils.printStackTraceIfDebug(e)
        } catch (e: NullPointerException) {
            ExceptionUtils.printStackTraceIfDebug(e)
        } catch (e: Exception) {
            ExceptionUtils.printStackTraceIfDebug(e)
        }
        listnerSplashSantander.onFalhaConexao()
    }

    @Throws(JSONException::class)
    private fun montaColaborador(json: JSONObject): Colaborador {
        val empresa: Empresa
        val colaborador: Colaborador
        Sessao.setIdSessao(json["Id"].toString())
        Sessao.setIdSessaoNorber(json.optString("IdNorber", ""))
        val jsonColaborador = JSONObject(json.getString("ColaboradorLogado"))
        val nomeColaborador = jsonColaborador.getString("Nome")
        val matricula = jsonColaborador.getString("Matricula").toInt()
        val ehGestor = java.lang.Boolean.parseBoolean(jsonColaborador.getString("EhGestor"))
        var ehRescindido = false
        if (jsonColaborador.has("EhRescindido")) {
            ehRescindido = java.lang.Boolean.parseBoolean(jsonColaborador.getString("EhRescindido"))
        }
        var rescindidoPodeVisualizarTodosMenus = false
        if (jsonColaborador.has("RescindidoPodeVisualizarTodosMenus")) {
            rescindidoPodeVisualizarTodosMenus =
                java.lang.Boolean.parseBoolean(jsonColaborador.getString("RescindidoPodeVisualizarTodosMenus"))
        }
        val listaContratos: List<Contrato> =
            ServicoDeColaborador().obtemListaDeContratos(jsonColaborador)
        val grupoHierarquico = jsonColaborador.optString("GrupoHierarquico", "")
        val cpf = jsonColaborador.optString("CPF", "")
        val pis = jsonColaborador.optString("PIS", "")
        val jsonEmpresa = JSONObject(jsonColaborador.getString("Empresa"))
        val codigoEmpresa = jsonEmpresa.getInt("Codigo")
        val razaoSocial = jsonEmpresa.getString("RazaoSocial")
        val nomeFantasia = jsonEmpresa.getString("NomeFantasia")
        var endereco = jsonEmpresa.optString("Endereco", "")
        if (endereco == "null") {
            endereco = ""
        }
        val cnpj = jsonEmpresa.optString("CNPJ", "")
        val cnpjFilial = jsonEmpresa.optString("CNPJ Filial", "")
        empresa = Empresa(codigoEmpresa, razaoSocial, nomeFantasia)
        empresa.setEndereco(endereco)
        empresa.setCnpj(cnpj)
        if (cnpjFilial != null && cnpjFilial != "null") {
            empresa.setCnpjFilial(cnpjFilial)
        }
        colaborador = Colaborador(matricula, nomeColaborador, ehGestor, empresa)
        colaborador.setGrupoHierarquico(grupoHierarquico)
        colaborador.setCPF(cpf)
        colaborador.setPis(pis)
        colaborador.setEhRescindido(ehRescindido)
        colaborador.setRescindidoPodeVisualizarTodosMenus(rescindidoPodeVisualizarTodosMenus)
        colaborador.setContratos(listaContratos)

        // Valida se o colaborador possue a propriedade NomeCivil no json de resposta.
        // Essa propriedade só possui valor quando o colaborador possuir nome social.
        // O nome social é recebido na propriedade nomeColaborador.
        // A propriedade nomeCivil é utilizada no comprovante de marcação do ponto.
        if (jsonColaborador.has("NomeCivil")
            && !StringAndTextUtils.isNullOrEmpty(jsonColaborador.getString("NomeCivil"))
        ) {
            val nomeCivil = jsonColaborador.getString("NomeCivil")
            colaborador.setNomeCivil(nomeCivil)
        }
        val jsonListaFunc = json.getJSONArray("FuncionalidadesAutorizadas")
        val funcionalidadesPermitidas: MutableList<String> = ArrayList()
        val listaFunc: MutableList<Funcionalidade> = ArrayList<Funcionalidade>()
        for (i in 0 until jsonListaFunc.length()) {
            val jsonFunc = jsonListaFunc.getJSONObject(i)
            val novaFuncionalidade =
                Funcionalidade(jsonFunc.getString("Id"), jsonFunc.getString("Descricao"))
            if (jsonFunc.has("VisivelParaRescindido")) {
                novaFuncionalidade.setVisivelParaRescindido(
                    java.lang.Boolean.parseBoolean(
                        jsonFunc.getString(
                            "VisivelParaRescindido"
                        )
                    )
                )
            }
            listaFunc.add(novaFuncionalidade)
            funcionalidadesPermitidas.add(jsonFunc.getString("Id"))
        }
        Sessao.setFuncionalidades(listaFunc)
        Sessao.setFuncionalidadesPermitidas(funcionalidadesPermitidas)
        return colaborador
    }

    private fun trataAceiteTermo(respostaJson: String) {
        var json: JSONObject? = null
        try {
            json = JSONObject(respostaJson)
            Sessao.setIdSessao(json["Id"].toString())
            Sessao.setIdSessaoNorber(json.optString("IdNorber", ""))
            val jsonColaborador = JSONObject(json.getString("ColaboradorLogado"))
            val matricula = jsonColaborador.getString("Matricula").toInt()
            val jsonEmpresa = JSONObject(jsonColaborador.getString("Empresa"))
            val codigoEmpresa = jsonEmpresa.getInt("Codigo")
            listener.onValidacaoTermo(matricula, codigoEmpresa)
        } catch (e: Exception) {
        }
    }

    private fun trataFinalizeSessao(resposta: String?) {
        if (resposta == null || resposta == "") {
            return
        }
    }

    fun trataVersaoMinima(listener: SplashScreensListener) {
        val versaoDaAplicacao: Int = BuildConfig.VERSION_CODE
        val versaoNecessaria: Int = Sessao.getVersaoMinimaAndroid()
        val versaoMinimaObrigatoria: Boolean = Sessao.isVersaoMinimaObrigatoria()
        val mensagem: String = Sessao.getMensagemVersaoMinima()
        if (versaoDaAplicacao < versaoNecessaria) {
            if (versaoMinimaObrigatoria) {
                listener.executaMensagemVersaoMinimaObrigatoria(mensagem)
            } else {
                listener.executaMensagemVersaoMinimaNaoObrigatoria(mensagem)
            }
        } else {
            listener.versaoMinimaHabilitadaEAtendida()
        }
    }

    fun trataUpdateApp(appUpdateManager: AppUpdateManager?, activity: Activity?) {
        val isUpdateApp: Boolean = Sessao.sugestaoUpdateApp()
        val isUpdateAppObrigatoria: Boolean = Sessao.isUpdateAppObrigatoria()

        //TODO: dps rever se essa logica esta correta
        if (isUpdateApp) {
            if (isUpdateAppObrigatoria) {
                ServicoUpdateVersion.verificaUpdate(
                    appUpdateManager,
                    activity,
                    isUpdateAppObrigatoria
                )
            } else {
                ServicoUpdateVersion.verificaUpdate(
                    appUpdateManager,
                    activity,
                    isUpdateAppObrigatoria
                )
            }
        }
    }

    private fun addParametroDispositivoConfiavelSFA(params: MutableList<NameValuePair>) {
        var segundofatorvalidado = false
        if (Sessao.getColaboradorLogado() != null) segundofatorvalidado =
            PreferenciasUtil.getSegundoFator(
                context,
                "confiavel" + "_" + Sessao.getColaboradorLogado().getEmpresa()
                    .getCodigo() + "_" + Sessao.getColaboradorLogado().getMatricula()
            )
        params.add(BasicNameValuePair("confiavel", segundofatorvalidado.toString()))
    }

    companion object {
        const val SERVICO_CONFIGURACOESLOGIN = "configuracoeslogin"
    }
}


