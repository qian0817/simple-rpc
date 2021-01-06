package com.qianlei.rpc.common.serialize

import java.lang.reflect.Parameter

interface Serializer {
    fun <T> serializeResponse(response: RpcResponse<T>): ByteArray

    fun <T> parseResponse(message: ByteArray, clazz: Class<T>): RpcResponse<T>

    fun serializeRequest(request: RpcRequest): ByteArray

    /**
     * 在解析 RpcRequest 时需要知道具体的参数类型
     * 需要先解析服务名和方法名
     * 通过传入的[getParameter]函数参数，根据服务名和方法名来获取到对应的参数
     */
    fun parseRequest(
        message: ByteArray,
        getParameter: (serviceName: String, methodName: String) -> Array<Parameter>
    ): RpcRequest
}