package br.com.lg.MyWay.newgentemobile.compartilhado.colaborador.dados.models

import android.os.Parcel
import android.os.Parcelable


class Contrato : Parcelable {
    var matricula: String? = null
    var nome: String? = null
    var codigoCargo: String? = null
    var cargoDescricao: String? = null
    var codigoEmpresa: String? = null
    var empresaDescricao: String? = null
    var empresaCNPJ: String? = null
    var empresaEndereco: String? = null
    var dataAdmissao: String? = null
    private var ehRescindido: Boolean? = null
    private var rescindidoPodeVisualizarTodosMenus: Boolean? = null

    constructor(
        matricula: String?, nome: String?, codigoCargo: String?,
        cargoDescricao: String?, codigoEmpresa: String?, empresaDescricao: String?,
        empresaCNPJ: String?, empresaEndereco: String?, dataAdmissao: String?,
        ehRescindido: Boolean?, rescindidoPodeVisualizarTodosMenus: Boolean?
    ) {
        this.matricula = matricula
        this.nome = nome
        this.codigoCargo = codigoCargo
        this.cargoDescricao = cargoDescricao
        this.codigoEmpresa = codigoEmpresa
        this.empresaDescricao = empresaDescricao
        this.empresaCNPJ = empresaCNPJ
        this.empresaEndereco = empresaEndereco
        this.dataAdmissao = dataAdmissao
        this.ehRescindido = ehRescindido
        this.rescindidoPodeVisualizarTodosMenus = rescindidoPodeVisualizarTodosMenus
    }

    constructor()
    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    fun ehRescindido(): Boolean {
        return ehRescindido!!
    }

    fun getRescindidoPodeVisualizarTodosMenus(): Boolean {
        return rescindidoPodeVisualizarTodosMenus!!
    }

    fun setEhRescindido(ehRes: Boolean?) {
        ehRescindido = ehRes
    }

    fun setRescindidoPodeVisualizarTodosMenus(rescindidoPodeVisualizarTodosMenus: Boolean?) {
        this.rescindidoPodeVisualizarTodosMenus = rescindidoPodeVisualizarTodosMenus
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || o !is Contrato) {
            return false
        }
        val outroContrato = o
        return (matricula === outroContrato.matricula
                && codigoEmpresa === outroContrato.codigoEmpresa)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(matricula)
        dest.writeInt(if (ehRescindido!!) 0 else -1)
        dest.writeInt(if (rescindidoPodeVisualizarTodosMenus!!) 0 else -1)
        dest.writeString(nome)
        dest.writeString(codigoCargo)
        dest.writeString(cargoDescricao)
        dest.writeString(empresaDescricao)
        dest.writeString(codigoEmpresa)
        dest.writeString(dataAdmissao)
    }

    private fun readFromParcel(`in`: Parcel) {
        matricula = `in`.readString()
        ehRescindido = `in`.readInt() == 0
        rescindidoPodeVisualizarTodosMenus = `in`.readInt() == 0
        nome = `in`.readString()
        cargoDescricao = `in`.readString()
        empresaDescricao = `in`.readString()
        codigoCargo = `in`.readString()
        codigoEmpresa = `in`.readString()
        dataAdmissao = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Contrato?> = object : Parcelable.Creator<Contrato?> {
            override fun createFromParcel(`in`: Parcel): Contrato {
                return Contrato(`in`)
            }

            override fun newArray(size: Int): Array<Contrato?> {
                return arrayOfNulls(size)
            }
        }
    }
}

