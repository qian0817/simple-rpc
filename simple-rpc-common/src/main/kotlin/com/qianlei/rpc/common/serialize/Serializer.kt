package com.qianlei.rpc.common.serialize

interface Serializer {
    fun <T> serializeResponse(response: RpcResponse<T>): ByteArray

    fun <T> parseResponse(message: ByteArray, clazz: Class<T>): RpcResponse<T>

    fun serializeRequest(request: RpcRequest): ByteArray

    fun parseRequest(message: ByteArray): RpcRequest

    fun serializeArgs(obj: Any?): String

    fun <T> parseArgs(message: String, clazz: Class<T>): T
}