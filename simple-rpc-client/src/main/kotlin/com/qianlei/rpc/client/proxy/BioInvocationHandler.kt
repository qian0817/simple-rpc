package com.qianlei.rpc.client.proxy

import cn.hutool.core.util.HexUtil
import com.qianlei.rpc.client.annotation.RpcClient
import com.qianlei.rpc.common.register.IRegisterService
import com.qianlei.rpc.common.serialize.RpcRequest
import com.qianlei.rpc.common.serialize.Serializer
import mu.KotlinLogging
import net.sf.cglib.proxy.InvocationHandler
import net.sf.cglib.proxy.Proxy
import java.lang.reflect.Method
import java.net.Socket

class BioInvocationHandler<T>(
    private val registerService: IRegisterService,
    private val serializer: Serializer,
    private val target: Class<T>,
) : InvocationHandler {
    private val logger = KotlinLogging.logger { }

    @Suppress("UNCHECKED_CAST")
    val proxy: T = Proxy.newProxyInstance(target.javaClass.classLoader, arrayOf(target), this) as T

    override fun invoke(proxy: Any?, method: Method, args: Array<out Any?>): Any? {
        if (!target.isAnnotationPresent(RpcClient::class.java)) {
            throw RuntimeException("请在 Rpc 客户端上加上 RpcClient 注解")
        }
        return startRpc(target.getAnnotation(RpcClient::class.java).name, method, args)
    }

    private fun startRpc(serviceName: String, method: Method, args: Array<out Any?>): Any? {
        val service = registerService.findAllService(serviceName).randomOrNull() ?: throw RuntimeException("无可用服务")
        logger.debug { "服务提供方${service}" }
        Socket(service.host, service.port).use { socket ->
            logger.debug { "向${socket.remoteSocketAddress}发送请求，请求服务名:${serviceName}" }
            logger.debug { "向${socket.remoteSocketAddress}发送请求，请求方法名:${method.name}" }
            logger.debug { "向${socket.remoteSocketAddress}发送请求，请求参数:${args.contentDeepToString()}" }
            val request = serializer.serializeRequest(RpcRequest(args, serviceName, method.name))
            val encodeRequest = HexUtil.encodeHexStr(request)
            socket.getOutputStream().write("$encodeRequest\n".encodeToByteArray())

            val encodeResponse = socket.getInputStream().bufferedReader().readLine()
            val response = HexUtil.decodeHex(encodeResponse)
            val rpcResponse = serializer.parseResponse(response, method.returnType)

            logger.debug { "接受${socket.remoteSocketAddress}的响应:${rpcResponse}" }
            if (rpcResponse.exception != null) {
                throw RuntimeException(rpcResponse.exception)
            } else {
                return rpcResponse.value
            }
        }
    }
}
