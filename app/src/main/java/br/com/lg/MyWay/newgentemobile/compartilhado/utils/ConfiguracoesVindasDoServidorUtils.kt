package br.com.lg.MyWay.newgentemobile.compartilhado.utils

import android.content.Context
import android.text.TextUtils
import br.com.lg.MyWay.newgentemobile.R
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.addVersaoFuncional
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.chavePublicaSAML
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.isAutenticacaoForaDoSA3
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.isExibirEntradaESaida
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.isLoginSAMLHabilitado
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.isVersaoMinima
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.isVersaoMinimaObrigatoria
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.mensagemVersaoMinima
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.setMinhasAtividadesEmAndamento
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.setMinhasAtividadesHistorico
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.setVersoesFuncionais
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.timeOutConexaoEmSegundos
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.urlSAML
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.urlSegundoFator
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Sessao.versaoMinimaAndroid
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.Armazenamento.armazene
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.Armazenamento.recuperarInt
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.Armazenamento.recupereBoolean
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.Armazenamento.recupereString
import br.com.lg.MyWay.newgentemobile.compartilhado.utils.ConfiguracoesVindasDoServidorUtils.FILTRO_SUBORDINADO_DIRETOS
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.CriptografiaUtil.criptografarAES
import org.json.JSONObject


object ConfiguracoesVindasDoServidorUtils {
    const val TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_PRIMEIRO_ACESSO =
        "TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_PRIMEIRO_ACESSO"
    const val TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_ESQUECI_SENHA =
        "TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_ESQUECI_SENHA"
    const val NAO_INICIALIZA_FLUXO_WORKFLOW = "NAO_INICIALIZA_FLUXO_WORKFLOW"
    const val TIMEOUT = "Timeout"
    const val PERMISSAO_WORKFLOW_APENAS_MOBILE = "PERMISSAO_WORKFLOW_APENAS_MOBILE"
    const val SENHA_CADASTRO_LOCAL_PONTO = "SENHA_CADASTRO_LOCAL_PONTO"
    const val TELA_CONFIG_LOGIN_SAML_HABILITADO = "TELA_CONFIG_LOGIN_SAML_HABILITADO"
    const val EXIBIR_ENTRADA_E_SAIDA_LISTA_MARCACOES = "EXIBIR_ENTRADA_E_SAIDA_LISTA_MARCACOES"
    const val TELA_CONFIG_LINK_SERVICO_SAML = "TELA_CONFIG_LINK_SERVICO_SAML"
    const val TELA_CONFIG_CHAVE_PUBLICA_SAML = "TELA_CONFIG_CHAVE_PUBLICA_SAML"
    const val TELA_CONFIG_CHAVE_PRIVADA_SAML = "TELA_CONFIG_CHAVE_PRIVADA_SAML"
    const val AUTENTICACAO_PELO_SA3 = "autenticacaoForaDoSA3"
    const val DEFAULT_LOGAR_AUTOMATICO = "DEFAULT_LOGAR_AUTOMATICO"
    const val DEFAULT_LEMBRAR_USUARIO = "DEFAULT_LEMBRAR_USUARIO"
    const val LIMITE_RAIO_CADASTRO_LOCAIS_PONTO = "LIMITE_RAIO_CADASTRO_LOCAIS_PONTO"
    const val MENSSAGEM_MARCACAO_PENDENTE = "MENSSAGEM_MARCACAO_PENDENTE"
    const val PONTO_TEMPO_ALERTA_MARCACAO_DUPLICADA = "PONTO_TEMPO_ALERTA_MARCACAO_DUPLICADA"
    const val COOKIE_LOADBALANCE = "COOKIE_LOADBALANCE"
    const val FILTRO_SUBORDINADO_DIRETOS = "FILTRO_SUBORDINADO_DIRETOS"
    const val FILTRO_SUBORDINADO_AFASTAMENTO = "FILTRO_SUBORDINADO_AFASTAMENTO"
    const val FILTRO_SUBORDINADO_FERIAS = "FILTRO_SUBORDINADO_FERIAS"
    const val FILTRO_SUBORDINADO_RECISAO = "FILTRO_SUBORDINADO_RECISAO"
    const val FILTRO_SUBORDINADO_ATIVIDADENORMAL = "FILTRO_SUBORDINADO_ATIVIDADENORMAL"
    const val FILTRO_SUBORDINADO_VISIBILIDADEAPLICADA = "FILTRO_SUBORDINADO_VISIBILIDADEAPLICADA"
    const val FILTRO_SUBORDINADO_INDIRETOS = "FILTRO_SUBORDINADO_INDIRETOS"
    const val FILTRO_SUBORDINADO_RECESSO = "FILTRO_SUBORDINADO_RECESSO"
    private val arrayKeysFiltroSubordinados = arrayOf(
        FILTRO_SUBORDINADO_DIRETOS,
        FILTRO_SUBORDINADO_AFASTAMENTO,
        FILTRO_SUBORDINADO_FERIAS,
        FILTRO_SUBORDINADO_RECISAO,
        FILTRO_SUBORDINADO_ATIVIDADENORMAL,
        FILTRO_SUBORDINADO_VISIBILIDADEAPLICADA,
        FILTRO_SUBORDINADO_INDIRETOS,
        FILTRO_SUBORDINADO_RECESSO
    )
    const val VERSAO_FUNCIONAL_CRIPTOGRAFIA = "VersaoCriptografia"
    const val VERSAO_FUNCIONAL_MARCACAO_PONTO = "VersaoMarcacaoPonto"
    const val VERSAO_FUNCIONAL_PESQUISA_SATISFACAO = "VersaoPesquisaDeSatisfacao"
    const val VERSAO_FUNCIONAL_PINNING = "VersaoPinning"
    const val SANTANDER_NUMERO_TELEFONE_FALE_CONOSCO = "NUMERO_TELEFONE_FALE_CONOSCO"
    const val MINHAS_ATIVIDADES = "MinhasAtividades"
    const val HISTORICO_HABILITADO = "HistoricoHabilitado"
    const val EM_ANDAMENTO_HABILITADO = "EmAndamentoHabilitado"
    const val VERSAO_MINIMA = "VersaoMinima"
    const val VERSAO_MINIMA_HABILITADO = "Habilitado"
    const val VERSAO_MINIMA_ATUALIZACAO_OBRIGATORIA = "AtualizacaoObrigatoria"
    const val VERSAO_MINIMA_MENSAGEM = "Mensagem"
    const val VERSAO_MINIMA_VERSAO = "VersaoAndroid"
    fun salvarConfiguracoesVindasDoServidor(context: Context?, jsonObj: JSONObject) {
        val versoesFuncionais = jsonObj.optJSONObject("VersoesFuncionais")
        setVersoesFuncionais(HashMap())
        if (versoesFuncionais != null) {
            val versaoCriptografia = versoesFuncionais.optInt(VERSAO_FUNCIONAL_CRIPTOGRAFIA, 0)
            addVersaoFuncional(VERSAO_FUNCIONAL_CRIPTOGRAFIA, versaoCriptografia)
            val versaoMarcacaoPonto = versoesFuncionais.optInt(VERSAO_FUNCIONAL_MARCACAO_PONTO, 0)
            addVersaoFuncional(VERSAO_FUNCIONAL_MARCACAO_PONTO, versaoMarcacaoPonto)
            val versaoPesquisaDeSatisfacao = versoesFuncionais.optInt(
                VERSAO_FUNCIONAL_PESQUISA_SATISFACAO, 0
            )
            addVersaoFuncional(VERSAO_FUNCIONAL_PESQUISA_SATISFACAO, versaoPesquisaDeSatisfacao)
            val versaoPinning = versoesFuncionais.optInt(VERSAO_FUNCIONAL_PINNING, 0)
            addVersaoFuncional(VERSAO_FUNCIONAL_PINNING, versaoPinning)
        } else {
            addVersaoFuncional(VERSAO_FUNCIONAL_CRIPTOGRAFIA, 0)
            addVersaoFuncional(VERSAO_FUNCIONAL_MARCACAO_PONTO, 0)
            addVersaoFuncional(VERSAO_FUNCIONAL_PESQUISA_SATISFACAO, 0)
            addVersaoFuncional(VERSAO_FUNCIONAL_PINNING, 0)
        }
        salveAsConfiguracoesDeWorkflow(jsonObj)
        salveAsConfiguracoesDeVersaoMinima(jsonObj)
        val joConfiguracoesParaOApp = jsonObj.optJSONObject("ConfiguracoesParaOApp")
        if (joConfiguracoesParaOApp != null) {
            isLoginSAMLHabilitado = java.lang.Boolean.parseBoolean(
                joConfiguracoesParaOApp.optString(
                    TELA_CONFIG_LOGIN_SAML_HABILITADO, "False"
                )
            )
            urlSAML = joConfiguracoesParaOApp.optString(TELA_CONFIG_LINK_SERVICO_SAML, "")
            urlSegundoFator = joConfiguracoesParaOApp.optString(TELA_CONFIG_LINK_SERVICO_SAML, "")
            chavePublicaSAML = joConfiguracoesParaOApp.optString(TELA_CONFIG_CHAVE_PUBLICA_SAML, "")
            isExibirEntradaESaida = java.lang.Boolean.parseBoolean(
                joConfiguracoesParaOApp.optString(
                    EXIBIR_ENTRADA_E_SAIDA_LISTA_MARCACOES, "True"
                )
            )
            isAutenticacaoForaDoSA3 =
                joConfiguracoesParaOApp.optBoolean(AUTENTICACAO_PELO_SA3, false)
            val strNaoMostrarBotaoPrimeiroAcesso = joConfiguracoesParaOApp.optString(
                TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_PRIMEIRO_ACESSO, "False"
            )
            val bNaoMostrarBotaoPrimeiroAcesso =
                java.lang.Boolean.parseBoolean(strNaoMostrarBotaoPrimeiroAcesso)
            armazene(
                context,
                TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_PRIMEIRO_ACESSO,
                bNaoMostrarBotaoPrimeiroAcesso
            )
            val strNaoMostrarBotaoEsqueciSenha = joConfiguracoesParaOApp.optString(
                TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_ESQUECI_SENHA, "False"
            )
            val bNaoMostrarBotaoEsqueciSenha =
                java.lang.Boolean.parseBoolean(strNaoMostrarBotaoEsqueciSenha)
            armazene(
                context,
                TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_ESQUECI_SENHA,
                bNaoMostrarBotaoEsqueciSenha
            )
            val timeOutConexao = joConfiguracoesParaOApp.optInt(TIMEOUT, 0)
            timeOutConexaoEmSegundos = timeOutConexao
            val strNaoMostrarBotaoNovoFluxoWorkflow = joConfiguracoesParaOApp.optString(
                NAO_INICIALIZA_FLUXO_WORKFLOW, "False"
            )
            val bNaoMostrarBotaoNovoFluxoWorkflow =
                java.lang.Boolean.parseBoolean(strNaoMostrarBotaoNovoFluxoWorkflow)
            armazene(context, NAO_INICIALIZA_FLUXO_WORKFLOW, bNaoMostrarBotaoNovoFluxoWorkflow)
            val strInserirWorkflowNoMenuParaAMatriculaEEmpresaEspecificadas =
                joConfiguracoesParaOApp.optString(
                    PERMISSAO_WORKFLOW_APENAS_MOBILE, ""
                )
            armazene(
                context,
                PERMISSAO_WORKFLOW_APENAS_MOBILE,
                strInserirWorkflowNoMenuParaAMatriculaEEmpresaEspecificadas
            )
            var strSenhaCadastroLocalPonto = joConfiguracoesParaOApp.optString(
                SENHA_CADASTRO_LOCAL_PONTO, ""
            )
            if (!TextUtils.isEmpty(strSenhaCadastroLocalPonto)) {
                strSenhaCadastroLocalPonto = criptografarAES(
                    strSenhaCadastroLocalPonto
                )
            }
            armazene(context, SENHA_CADASTRO_LOCAL_PONTO, strSenhaCadastroLocalPonto)
            val strDefaultLembrarUsuario = joConfiguracoesParaOApp.optString(
                DEFAULT_LEMBRAR_USUARIO, "True"
            )
            val bDefaultLembrarUsuario = java.lang.Boolean.parseBoolean(strDefaultLembrarUsuario)
            armazene(context, DEFAULT_LEMBRAR_USUARIO, bDefaultLembrarUsuario)
            val strDefaultLogarAutomatico = joConfiguracoesParaOApp.optString(
                DEFAULT_LOGAR_AUTOMATICO, "False"
            )
            val bDefaultLogarAutomatico = java.lang.Boolean.parseBoolean(strDefaultLogarAutomatico)
            armazene(context, DEFAULT_LOGAR_AUTOMATICO, bDefaultLogarAutomatico)
            val strLimiteRaioCadastroLocaisPonto = joConfiguracoesParaOApp.optString(
                LIMITE_RAIO_CADASTRO_LOCAIS_PONTO, "0"
            )
            val intLimiteRaioCadastroLocaisPonto = strLimiteRaioCadastroLocaisPonto.toInt()
            armazene(context, LIMITE_RAIO_CADASTRO_LOCAIS_PONTO, intLimiteRaioCadastroLocaisPonto)
            val mensagemMarcacaoSemProcessamento = joConfiguracoesParaOApp.optString(
                MENSSAGEM_MARCACAO_PENDENTE, ""
            )
            armazene(context, MENSSAGEM_MARCACAO_PENDENTE, mensagemMarcacaoSemProcessamento)
            val strTempoMarcacaoPontoDuplicada = joConfiguracoesParaOApp.optString(
                PONTO_TEMPO_ALERTA_MARCACAO_DUPLICADA, "0"
            )
            val intTempoMarcacaoPontoDuplicada = strTempoMarcacaoPontoDuplicada.toInt()
            armazene(context, PONTO_TEMPO_ALERTA_MARCACAO_DUPLICADA, intTempoMarcacaoPontoDuplicada)
            val santanderNumeroTelefoneFaleConosco = joConfiguracoesParaOApp.optString(
                SANTANDER_NUMERO_TELEFONE_FALE_CONOSCO, ""
            )
            armazene(
                context,
                SANTANDER_NUMERO_TELEFONE_FALE_CONOSCO,
                santanderNumeroTelefoneFaleConosco
            )
            preenchaFiltrosArmazenados(context)
        } else {
            armazene(context, TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_PRIMEIRO_ACESSO, false)
            armazene(context, TIPO_CONFIG_APP_NAO_MOSTRAR_BOTAO_ESQUECI_SENHA, false)
            armazene(context, NAO_INICIALIZA_FLUXO_WORKFLOW, false)
            armazene(context, PERMISSAO_WORKFLOW_APENAS_MOBILE, "")
            armazene(context, SENHA_CADASTRO_LOCAL_PONTO, "")
            armazene(context, DEFAULT_LEMBRAR_USUARIO, true)
            armazene(context, DEFAULT_LOGAR_AUTOMATICO, false)
            armazene(context, LIMITE_RAIO_CADASTRO_LOCAIS_PONTO, 0)
            armazene(context, MENSSAGEM_MARCACAO_PENDENTE, "")
            armazene(context, PONTO_TEMPO_ALERTA_MARCACAO_DUPLICADA, 0)
            armazene(context, SANTANDER_NUMERO_TELEFONE_FALE_CONOSCO, "")
            preenchaFiltrosArmazenados(context)
        }
    }

    private fun salveAsConfiguracoesDeWorkflow(jsonObj: JSONObject) {
        val jsonMinhasAtividades = jsonObj.optJSONObject(MINHAS_ATIVIDADES)
        var emAndamentoHabilitado = false
        var historicoHabilitado = false
        if (jsonMinhasAtividades != null) {
            historicoHabilitado = BooleanUtils.tryParse(jsonMinhasAtividades, HISTORICO_HABILITADO)
            emAndamentoHabilitado =
                BooleanUtils.tryParse(jsonMinhasAtividades, EM_ANDAMENTO_HABILITADO)
        }
        setMinhasAtividadesHistorico(historicoHabilitado)
        setMinhasAtividadesEmAndamento(emAndamentoHabilitado)
    }

    private fun salveAsConfiguracoesDeVersaoMinima(jsonObj: JSONObject) {
        val jsonMinhasAtividades = jsonObj.optJSONObject(VERSAO_MINIMA)
        var versaoMinimaHabilitado = false
        var atualizacaoObrigatoria = false
        var mensagem: String? = ""
        var versaoAndroid = 0
        if (jsonMinhasAtividades != null) {
            versaoMinimaHabilitado =
                BooleanUtils.tryParse(jsonMinhasAtividades, VERSAO_MINIMA_HABILITADO)
            atualizacaoObrigatoria =
                BooleanUtils.tryParse(jsonMinhasAtividades, VERSAO_MINIMA_ATUALIZACAO_OBRIGATORIA)
            mensagem = jsonMinhasAtividades.optString(VERSAO_MINIMA_MENSAGEM, "")
            versaoAndroid = jsonMinhasAtividades.optInt(VERSAO_MINIMA_VERSAO, 0)
        }
        isVersaoMinima = versaoMinimaHabilitado
        isVersaoMinimaObrigatoria = atualizacaoObrigatoria
        mensagemVersaoMinima = mensagem!!
        versaoMinimaAndroid = versaoAndroid
        //TODO: adicionar setter de atualizacao do app
    }

    fun deParaChaveFiltroSubordinado(context: Context, chave: String): String {
        return if (chave == context.getString(R.string.chave_filtro_subordinados_diretos)) {
            FILTRO_SUBORDINADO_DIRETOS
        } else if (chave == context.getString(R.string.chave_filtro_afastamento)) {
            FILTRO_SUBORDINADO_AFASTAMENTO
        } else if (chave == context.getString(R.string.chave_filtro_ferias)) {
            FILTRO_SUBORDINADO_FERIAS
        } else if (chave == context.getString(R.string.chave_filtro_recisao)) {
            FILTRO_SUBORDINADO_RECISAO
        } else if (chave == context.getString(R.string.chave_filtro_atividade_normal)) {
            FILTRO_SUBORDINADO_ATIVIDADENORMAL
        } else if (chave == context.getString(R.string.chave_filtro_visibilidade_aplicada)) {
            FILTRO_SUBORDINADO_VISIBILIDADEAPLICADA
        } else if (chave == context.getString(R.string.chave_filtro_subordinados_indiretos)) {
            FILTRO_SUBORDINADO_INDIRETOS
        } else if (chave == context.getString(R.string.chave_filtro_recesso)) {
            FILTRO_SUBORDINADO_RECESSO
        } else {
            ""
        }
    }

    private fun preenchaFiltrosArmazenados(context: Context?) {
        for (keyFiltroSubordinados in arrayKeysFiltroSubordinados) {
            var valor = false
            valor = if (keyFiltroSubordinados == FILTRO_SUBORDINADO_DIRETOS) {
                obterConfigBoolean(context, keyFiltroSubordinados, true)
            } else {
                obterConfigBoolean(context, keyFiltroSubordinados, false)
            }
            armazene(context, keyFiltroSubordinados, valor)
        }
    }

    @JvmOverloads
    fun obterConfigBoolean(context: Context?, chave: String?, fallback: Boolean = false): Boolean {
        return recupereBoolean(context, chave, fallback)
    }

    fun obterConfigString(context: Context?, chave: String?): String? {
        return recupereString(context, chave, "")
    }

    fun obterConfigInt(context: Context?, chave: String?): Int {
        return recuperarInt(context, chave, 0)
    }
}

