package br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.models


import android.os.Parcel
import android.os.Parcelable
import br.com.lg.MyWay.newgentemobile.compartilhado.dados.models.Empresa
import com.google.firebase.crashlytics.FirebaseCrashlytics


class Colaborador : Parcelable {
    var matricula = 0
    private var ehGestor = false
    private var ehRescindido = false
    var nome: String? = null
    var nomeCivil: String? = null
    var grupoHierarquico: String? = null
    var cPF: String? = null
    var pis: String? = null
    private var empresa: Empresa? = null
    var usuarioAutenticacao: String? = null
    private var contratos: List<Contrato>? = null
    var isRescindidoPodeVisualizarTodosMenus = false

    constructor(matricula: Int, nome: String?, empresa: Empresa?) {
        this.matricula = matricula
        this.nome = nome
        this.empresa = empresa
    }

    constructor(matricula: Int, nome: String?, ehGestor: Boolean, empresa: Empresa?) {
        this.nome = nome
        this.empresa = empresa
        this.ehGestor = ehGestor
        this.matricula = matricula
    }

    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    fun ehGestor(): Boolean {
        return ehGestor
    }

    fun ehRescindido(): Boolean {
        return ehRescindido
    }

    fun setEhRescindido(res: Boolean) {
        ehRescindido = res
    }

    fun getEmpresa(): Empresa? {
        return empresa
    }

    fun getContratos(): List<Contrato>? {
        return if (contratos == null) {
            ArrayList<Contrato>()
        } else contratos
    }

    fun setContratos(list: List<Contrato>?) {
        contratos = list
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || o !is Colaborador) {
            return false
        }
        val outroColaborador = o
        return (matricula == outroColaborador.matricula
                && empresa!!.codigo === outroColaborador.empresa!!.codigo)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        try {
            dest.writeInt(matricula)
            dest.writeInt(if (ehGestor) 0 else -1)
            dest.writeInt(if (ehRescindido) 0 else -1)
            dest.writeString(nome)
            dest.writeString(grupoHierarquico)
            dest.writeString(pis)
            dest.writeParcelable(empresa, flags)
            if (contratos != null) {
                dest.writeTypedList(contratos)
            }
            dest.writeString(usuarioAutenticacao)
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().log(ex.message + "WriteColaborador")
        }
    }

    private fun readFromParcel(`in`: Parcel) {
        try {
            matricula = `in`.readInt()
            matricula = `in`.readInt()
            matricula = `in`.readInt()
            matricula = `in`.readInt()
            ehGestor = `in`.readInt() == 0
            ehRescindido = `in`.readInt() == 0
            nome = `in`.readString()
            grupoHierarquico = `in`.readString()
            pis = `in`.readString()
            empresa = `in`.readParcelable(Empresa::class.java.getClassLoader())
            usuarioAutenticacao = `in`.readString()
            contratos = `in`.createTypedArrayList<Contrato>(Contrato.CREATOR)
        } catch (ex: Exception) {
            FirebaseCrashlytics.getInstance().log(ex.message + "ReadColaborador")
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Colaborador?> = object : Parcelable.Creator<Colaborador?> {
            override fun createFromParcel(`in`: Parcel): Colaborador {
                return Colaborador(`in`)
            }

            override fun newArray(size: Int): Array<Colaborador?> {
                return arrayOfNulls(size)
            }
        }
    }
}

