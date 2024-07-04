package br.com.lg.MyWay.newgentemobile.compartilhado.dados.models

import android.content.Context
import br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.models.Colaborador
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.Armazenamento
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.PreferenciasUtil
import br.com.lg.MyWay.newgentemobile.configuracoes.atualizacao.Versao
import br.com.lg.MyWay.newgentemobile.configuracoes.demonstracao.dados.DemoUtils
import org.json.JSONObject

object Sessao {
    const val isNovoFluxo = false

    var _portal: String? = null

    private var _aliasPortal: String? = null
    var aliasPortal: String?
        get() = _aliasPortal
        set(value) {
            _aliasPortal = value
        }

    private var _portalAppNorber: String? = null

    private var _ambienteId: String? = null
    var ambienteId: String?
        get() = _ambienteId
        set(value) {
            _ambienteId = value
        }

    private var _ambienteDiretorio: String? = null
    var ambienteDiretorio: String?
        get() = _ambienteDiretorio
        set(value) {
            _ambienteDiretorio = value
        }

    private var _ambienteTenant: String? = null
    var ambienteTenant: String?
        get() = _ambienteTenant
        set(value) {
            _ambienteTenant = value
        }

    private var _applicationContext: Context? = null
    var applicationContext: Context?
        get() = _applicationContext
        set(value) {
            _applicationContext = value
        }

    private var _idSessao: String? = null
    var idSessao: String?
        get() = _idSessao
        set(value) {
            _idSessao = value
        }

    private var _idSessaoNorber: String? = null
    var idSessaoNorber: String?
        get() = _idSessaoNorber
        set(value) {
            _idSessaoNorber = value
        }

    private var _dadosMenuJson: JSONObject? = null
    var dadosMenuJson: JSONObject?
        get() = _dadosMenuJson
        set(value) {
            _dadosMenuJson = value
        }

    private var _funcionalidades: List<Funcionalidade>? = null
    var funcionalidades: List<Funcionalidade>?
        get() = _funcionalidades
        set(value) {
            _funcionalidades = value
        }

    private var _funcionalidadesPermitidas: List<String>? = null

    private var _versoesFuncionais: MutableMap<String, Int> = HashMap()

    private var _versaoServidor: Int = Versao.DESCONHECIDA
    var versaoServidor: Int
        get() = _versaoServidor
        set(value) {
            _versaoServidor = value
        }

    private var _timeOutConexaoEmSegundos = 0
    var timeOutConexaoEmSegundos: Int
        get() = _timeOutConexaoEmSegundos
        set(value) {
            _timeOutConexaoEmSegundos = value
        }

    private var _isPermitirLoginAutomatico = true

    private var _idTelaPrincipalConfigurada: String? = null
    var idTelaPrincipalConfigurada: String?
        get() = _idTelaPrincipalConfigurada
        set(value) {
            _idTelaPrincipalConfigurada = value
        }

    private var _isNovaGeracao = false
    var isNovaGeracao: Boolean
        get() = _isNovaGeracao
        set(value) {
            _isNovaGeracao = value
        }

    private var _isAmbienteSaaS = false
    var isAmbienteSaaS: Boolean
        get() = _isAmbienteSaaS
        set(value) {
            _isAmbienteSaaS = value
        }

    private var _tenant: String? = null
    var tenant: String?
        get() = if (_tenant == null) {
            ""
        } else _tenant
        set(value) {
            _tenant = value
        }

    private var _telaDeAberturaExtensao: String? = null
    var telaDeAberturaExtensao: String?
        get() = _telaDeAberturaExtensao
        set(value) {
            _telaDeAberturaExtensao = value
        }

    private var _retornaExtensao: Boolean? = null
    var retornaExtensao: Boolean?
        get() = _retornaExtensao != null && _retornaExtensao as Boolean
        set(value) {
            _retornaExtensao = value
        }

    private var _extensaoNome: String? = null
    var extensaoNome: String?
        get() = _extensaoNome
        set(value) {
            _extensaoNome = value
        }

    private var _isHabilitarNovoPortalAutoatendimentoMobile = false
    var isHabilitarNovoPortalAutoatendimentoMobile: Boolean
        get() = _isHabilitarNovoPortalAutoatendimentoMobile
        set(value) {
            _isHabilitarNovoPortalAutoatendimentoMobile = value
        }

    private var _isUtilizarMarcacaoPontoNativaNaWebViewPortalAA = false
    var isUtilizarMarcacaoPontoNativaNaWebViewPortalAA: Boolean
        get() = _isUtilizarMarcacaoPontoNativaNaWebViewPortalAA
        set(value) {
            _isUtilizarMarcacaoPontoNativaNaWebViewPortalAA = value
        }

    private var _urlAutenticacaoManualAutoatendimento: String? = null
    var urlAutenticacaoManualAutoatendimento: String?
        get() = _urlAutenticacaoManualAutoatendimento
        set(value) {
            _urlAutenticacaoManualAutoatendimento = value
        }

    private var _urlAutenticacaoSamlAutoatendimento: String? = null
    var urlAutenticacaoSamlAutoatendimento: String?
        get() = _urlAutenticacaoSamlAutoatendimento
        set(value) {
            _urlAutenticacaoSamlAutoatendimento = value
        }

    private var _isLoginSAMLHabilitado = false
    var isLoginSAMLHabilitado: Boolean
        get() = _isLoginSAMLHabilitado
        set(value) {
            _isLoginSAMLHabilitado = value
        }

    private var _isExibirEntradaESaida = false
    var isExibirEntradaESaida: Boolean
        get() = _isExibirEntradaESaida
        set(value) {
            _isExibirEntradaESaida = value
        }

    private var _urlSAML: String? = null
    var urlSAML: String?
        get() = _urlSAML
        set(value) {
            _urlSAML = value
        }

    private var _urlSegundoFator: String? = null
    var urlSegundoFator: String?
        get() = _urlSegundoFator
        set(value) {
            _urlSegundoFator = value
        }

    private var _chavePublicaSAML: String? = null
    var chavePublicaSAML: String?
        get() = _chavePublicaSAML
        set(value) {
            _chavePublicaSAML = value
        }

    private var _ipServidor: String? = null
    var ipServidor: String?
        get() = _ipServidor
        set(value) {
            _ipServidor = value
        }

    private var _modoDesenvolvedor = false
    var modoDesenvolvedor: Boolean
        get() = _modoDesenvolvedor
        set(value) {
            _modoDesenvolvedor = value
        }

    private var _colaboradorLogado: Colaborador? = null
    var colaboradorLogado: Colaborador?
        get() = _colaboradorLogado
        set(value) {
            _colaboradorLogado = value
        }

    private var _colaboradorSelecionado: Colaborador? = null
    var colaboradorSelecionado: Colaborador?
        get() = _colaboradorSelecionado
        set(value) {
            _colaboradorSelecionado = value
        }

    private var _cookieSessaoSID: List<String>? = null
    var cookieSessaoSID: List<String>?
        get() {
            if (_cookieSessaoSID == null) {
                _cookieSessaoSID = ArrayList()
            }
            return _cookieSessaoSID
        }
        set(value) {
            _cookieSessaoSID = value
        }

    private var _cookieLoadBalancerName: String? = null
    var cookieLoadBalancerName: String?
        get() = _cookieLoadBalancerName
        set(value) {
            _cookieLoadBalancerName = value
        }

    private var _cookieLoadBalancerValue: String? = null
    var cookieLoadBalancerValue: String?
        get() = _cookieLoadBalancerValue
        set(value) {
            _cookieLoadBalancerValue = value
        }

    private var _tentativasDeConexao = 0
    var tentativasDeConexao: Int
        get() = _tentativasDeConexao
        set(value) {
            _tentativasDeConexao = value
        }

    private var _intervaloDeTentativas = 0
    var intervaloDeTentativas: Int
        get() = _intervaloDeTentativas
        set(value) {
            _intervaloDeTentativas = value
        }
    private var _isAutenticacaoForaDoSA3 = false
    var isAutenticacaoForaDoSA3: Boolean
        get() = _isAutenticacaoForaDoSA3
        set(value) {
            _isAutenticacaoForaDoSA3 = value
        }

    private var _isCriptografarDados = false
    var isCriptografarDados: Boolean
        get() = _isCriptografarDados
        set(value) {
            _isCriptografarDados = value
        }

    private var _isSegundoFator = false
    var isSegundoFator: Boolean
        get() = _isSegundoFator
        set(value) {
            _isSegundoFator = value
        }
    private var minhasAtividadesEmAndamento = false

    private var minhasAtividadesHistorico = false

    private var _termoDeBuscaWorkflow: HashMap<String, String>? = null
    var termoDeBuscaWorkflow: HashMap<String, String>?
        get() = _termoDeBuscaWorkflow
        set(value) {
            _termoDeBuscaWorkflow = value
        }
    private var _isVersaoMinima = true
    var isVersaoMinima: Boolean
        get() = _isVersaoMinima
        set(value) {
            _isVersaoMinima = value
        }
    private var _isVersaoMinimaObrigatoria = false
    var isVersaoMinimaObrigatoria: Boolean
        get() = _isVersaoMinimaObrigatoria
        set(value) {
            _isVersaoMinimaObrigatoria = value
        }
    private var _mensagemVersaoMinima = "Há uma atualização pendente, atualize para prosseguir."
    var mensagemVersaoMinima: String
        get() = _mensagemVersaoMinima
        set(value) {
            _mensagemVersaoMinima = value
        }
    private var _versaoMinimaAndroid = 0
    var versaoMinimaAndroid: Int
        get() = _versaoMinimaAndroid
        set(value) {
            _versaoMinimaAndroid = value
        }
    private var sugestaoUpdateApp = false

    private var _isUpdateAppObrigatoria = false
    var isUpdateAppObrigatoria: Boolean
        get() = _isUpdateAppObrigatoria
        set(value) {
            _isUpdateAppObrigatoria = value
        }

    fun setModoDemonstracao(contexto: Context?, modoDemonstracao: Boolean) {
        Armazenamento.armazene(contexto, "myWayModoDemonstracao", modoDemonstracao)
    }

    fun ehModoDemonstracao(contexto: Context?): Boolean {
        return Armazenamento.recupereBoolean(contexto, "myWayModoDemonstracao")
    }
    fun setTenant(tenant: String) { this._tenant = tenant }

    fun setPortal(novoPortal: String) {
        var novoPortal = novoPortal
        novoPortal = novoPortal.replace("\\s".toRegex(), "")
        if (!DemoUtils.ehPortalDemonstracao(novoPortal)) {
            if (novoPortal.startsWith("http://")) {
                novoPortal = novoPortal.replace("http://", "https://")
            }
            if (!novoPortal.startsWith("https://")) {
                novoPortal = "https://$novoPortal"
            }
            if (!novoPortal.endsWith("/")) {
                novoPortal = "$novoPortal/"
            }
        }
        _portal = novoPortal
    }

    fun getPortal(): String? {
        if(_portal == null)
            return ""
        return _portal
    }

    fun setFuncionalidadesPermitidas(list: List<String>?) {
        _funcionalidadesPermitidas = list
    }

    fun possuiPermissao(funcionalidade: String): Boolean {
        return _funcionalidadesPermitidas!!.contains(funcionalidade)
    }

    fun setPermitirLoginAutomatico(context: Context?, permitirLoginAutomatico: Boolean) {
        _isPermitirLoginAutomatico = permitirLoginAutomatico
        if (!permitirLoginAutomatico) {
            PreferenciasUtil.setManterLogado(context, false)
        }
    }

    fun getPermitirLoginAutomatico(): Boolean { return _isPermitirLoginAutomatico }

    fun getVersaoFuncional(key: String): Int {
        val versaoFuncional = _versoesFuncionais[key]
        return versaoFuncional ?: 0
    }

    fun addVersaoFuncional(key: String, valor: Int) {
        _versoesFuncionais[key] = valor
    }

    fun setVersoesFuncionais(versoesFuncionais: MutableMap<String, Int>) {
        _versoesFuncionais = versoesFuncionais
    }

    fun getPortalAppNorber(): String? {
        if (_portalAppNorber == null) _portalAppNorber = ""
        return _portalAppNorber
    }

    fun setPortalAppNorber(portalAppNorber: String) {
        var portalAppNorber = portalAppNorber
        if (portalAppNorber === "null") portalAppNorber = ""
        Sessao._portalAppNorber = portalAppNorber
    }

    fun ehMinhasAtividadesEmAndamento(): Boolean {
        return minhasAtividadesEmAndamento
    }

    fun setMinhasAtividadesEmAndamento(minhasAtividadesEmAndamento: Boolean) {
        Sessao.minhasAtividadesEmAndamento = minhasAtividadesEmAndamento
    }

    fun ehMinhasAtividadesHistorico(): Boolean {
        return minhasAtividadesHistorico
    }

    fun setMinhasAtividadesHistorico(minhasAtividadesHistorico: Boolean) {
        Sessao.minhasAtividadesHistorico = minhasAtividadesHistorico
    }

    fun sugestaoUpdateApp(): Boolean {
        return sugestaoUpdateApp
    }

    fun setSugestaoUpdateApp(sugestaoUpdateApp: Boolean) {
        Sessao.sugestaoUpdateApp = sugestaoUpdateApp
    }
}
