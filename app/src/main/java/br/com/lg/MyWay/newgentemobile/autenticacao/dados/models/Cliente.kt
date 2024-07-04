package br.com.lg.MyWay.newgentemobile.autenticacao.dados.models

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable

@DynamoDBTable(tableName = "Clientes")
class Cliente {

    var clienteID: String? = null
        @DynamoDBHashKey(attributeName = "ClienteID")
        get
        set

    var portal: String? = null
        @DynamoDBAttribute(attributeName = "Portal")
        get
        set

    var tenant: String? = null
        @DynamoDBAttribute(attributeName = "Tenant")
        get
        set
}
