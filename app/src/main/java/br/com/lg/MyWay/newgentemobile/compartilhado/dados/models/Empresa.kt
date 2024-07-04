package br.com.lg.MyWay.newgentemobile.compartilhado.dados.models

import android.os.Parcel
import android.os.Parcelable


class Empresa : Parcelable {
    var codigo = 0
    var razaoSocial: String? = null
        private set
    var nomeFantasia: String? = null
    var endereco: String? = null
    var cnpj: String? = null
    var cnpjFilial = ""

    constructor(codigo: Int) {
        this.codigo = codigo
    }

    constructor(codigo: Int, razaoSocial: String?, nomeFantasia: String?) {
        this.codigo = codigo
        this.razaoSocial = razaoSocial
        this.nomeFantasia = nomeFantasia
    }

    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(codigo)
        dest.writeString(razaoSocial)
        dest.writeString(nomeFantasia)
        dest.writeString(endereco)
        dest.writeString(cnpj)
    }

    private fun readFromParcel(`in`: Parcel) {
        codigo = `in`.readInt()
        razaoSocial = `in`.readString()
        nomeFantasia = `in`.readString()
        endereco = `in`.readString()
        cnpj = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Empresa?> = object : Parcelable.Creator<Empresa?> {
            override fun createFromParcel(`in`: Parcel): Empresa {
                return Empresa(`in`)
            }

            override fun newArray(size: Int): Array<Empresa?> {
                return arrayOfNulls(size)
            }
        }
    }
}

