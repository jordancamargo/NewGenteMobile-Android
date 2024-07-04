package br.com.lg.MyWay.newgentemobile.autenticacao.dados

import android.content.Context
import android.os.AsyncTask
import android.util.Log

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.regions.Regions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

import br.com.lg.MyWay.newgentemobile.autenticacao.dados.models.Cliente
//import br.com.lg.MyWay.newgentemobile.configuracoes.ajustes.ui.componentes.Console
import br.com.lg.MyWay.newgentemobile.configuracoes.seguranca.ArmazenamentoSeguroNDK
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient


object PortalAlias {

    private val PRE_FIXO_ALIAS = arrayOf("#", "@")
    private var asyncTaskContext: AsyncTask<Void, Void, Cliente?>? = null

    fun buscarPortal(context: Context, alias: String, listener: PortalAliasListener): AsyncTask<Void, Void, Cliente?> {
        cancelaThreadAtual()

        val asyncTask = object : AsyncTask<Void, Void, Cliente?>() {
            override fun doInBackground(vararg params: Void): Cliente? {
                var cliente: Cliente? = null
                try {
                    cliente = obtenhaClientePeloAws(context, alias)
                } catch (ex: Exception) {
                    try {
                        cliente = obtenhaClientePeloFirebase(alias)
                    } catch (ex2: Exception) {
                        return null
                    }
                }
                return cliente
            }

            override fun onPostExecute(cliente: Cliente?) {
                super.onPostExecute(cliente)
                try {
                    cliente?.let {
                        val portal = it.portal
                        val tenant = it.tenant
                        listener.onSucessoObterPortal(portal, tenant)
                    } ?: run {
                        listener.onErroObterPortal("Não foi possível coletar as informações do portal, verifique sua conexão e tente novamente.")
                    }
                } catch (e: Exception) {
                    listener.onErroObterPortal("Não foi possível coletar as informações do portal, verifique sua conexão e tente novamente.")
                }
            }

            override fun onCancelled(cliente: Cliente?) {
                super.onCancelled(cliente)
                Log.d("PortalAlias", "AVISO: Thread Cancelada para não sobrepor thread anterior;")
            }
        }

        asyncTask.execute()
        asyncTaskContext = asyncTask
        return asyncTask
    }

    private fun obtenhaClientePeloFirebase(alias: String): Cliente? {
        var clienteFirebase: Cliente? = null
        val db = FirebaseFirestore.getInstance()
        val portalAlias = obterPortalAliasClienteID(alias)

        db.collection("clients")
            .whereEqualTo("ClientID", portalAlias)
            .get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { query ->
                val clienteFirebaseOnSuccess = query.toObjects(Cliente::class.java).stream().findFirst()
                clienteFirebase = clienteFirebaseOnSuccess.orElse(null)
            })

        return clienteFirebase
    }

    private fun obtenhaClientePeloAws(context: Context, alias: String): Cliente? {
        val identityPoolId = ArmazenamentoSeguroNDK.getIdentityPoolId()
            ?: throw RuntimeException("ID do Identity Pool não encontrado.")

        val credentialsProvider = CognitoCachingCredentialsProvider(
            context, // Context
            identityPoolId,
            Regions.US_EAST_1 // Region
        )

        val clientConfiguration = ClientConfiguration()
        clientConfiguration.connectionTimeout = 5000
        clientConfiguration.maxErrorRetry = 2

        val ddbClient = AmazonDynamoDBClient(credentialsProvider, clientConfiguration)
        val mapper = DynamoDBMapper(ddbClient)
        return mapper.load(Cliente::class.java, obterPortalAliasClienteID(alias))
    }

    private fun obterPortalAliasClienteID(alias: String): String {
        return alias.trim().lowercase()
    }

    private fun cancelaThreadAtual() {
        asyncTaskContext?.cancel(true)
        asyncTaskContext = null
    }

    fun isPortalAlias(portal: String): Boolean {
        if (portal.contains(".") || portal.contains("/")) {
            PRE_FIXO_ALIAS.forEach {
                if (portal.startsWith(it)) {
                    return true
                }
            }
            return false
        }
        return true
    }

    interface PortalAliasListener {
        fun onSucessoObterPortal(portal: String?, tenant: String?)
        fun onErroObterPortal(erro: String)
    }
}
