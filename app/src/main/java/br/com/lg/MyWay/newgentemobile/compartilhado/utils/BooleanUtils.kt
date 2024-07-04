package br.com.lg.MyWay.newgentemobile.compartilhado.utils

import android.util.Log
import org.json.JSONObject

class BooleanUtils {

    companion object{
        @JvmStatic
        fun tryParse(jsonObject: JSONObject?, key: String?): Boolean {
            if(key == null || key == ""){
                Log.d(key, "Chave vazia ou nula")
                return false
            } else if(jsonObject == null) {
                Log.d(key, "jsonObject vazio ou nulo")
                return false
            }

            return if(jsonObject.toString().contains(key)){
                jsonObject.getBoolean(key)
            } else {
                false
            }
        }
    }
}