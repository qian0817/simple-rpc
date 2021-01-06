package com.qianlei.rpc.common.serialize

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.lang.reflect.Parameter
import kotlin.streams.asStream

class JsonSerializer : Serializer {
    override fun <T> serializeResponse(response: RpcResponse<T>): ByteArray {
        return jacksonObjectMapper().writeValueAsBytes(response)
    }

    override fun <T> parseResponse(message: ByteArray, clazz: Class<T>): RpcResponse<T> {
        return jacksonObjectMapper().readValue(message, object : TypeReference<RpcResponse<T>>() {})
    }

    override fun serializeRequest(request: RpcRequest): ByteArray {
        return jacksonObjectMapper().writeValueAsBytes(request)
    }

    override fun parseRequest(
        message: ByteArray,
        getParameter: (serviceName: String, methodName: String) -> Array<Parameter>
    ): RpcRequest {
        val mapper = jacksonObjectMapper()
        val jsonNode = mapper.readTree(message)
        val serviceName = jsonNode.get("serviceName").asText()
        val methodName = jsonNode.get("methodName").asText()
        val parameters = getParameter(serviceName, methodName)
        val args = jsonNode.get("args").elements().asSequence().mapIndexed { index, paramNode ->
            mapper.treeToValue(paramNode, parameters[index].type)
        }.asStream().toArray()
        return RpcRequest(args, serviceName, methodName)
    }
}