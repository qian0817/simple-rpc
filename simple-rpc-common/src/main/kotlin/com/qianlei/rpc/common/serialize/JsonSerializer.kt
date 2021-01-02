package com.qianlei.rpc.common.serialize

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

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

    override fun parseRequest(message: ByteArray): RpcRequest {
        return jacksonObjectMapper().readValue(message, object : TypeReference<RpcRequest>() {})
    }

    override fun <T> parseArgs(message: String, clazz: Class<T>): T {
        return jacksonObjectMapper().readValue(message, object : TypeReference<T>() {})
    }

    override fun serializeArgs(obj: Any?): String {
        return jacksonObjectMapper().writeValueAsString(obj)
    }
}