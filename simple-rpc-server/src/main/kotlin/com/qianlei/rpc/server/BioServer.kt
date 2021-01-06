package com.qianlei.rpc.server

import cn.hutool.core.util.HexUtil
import com.qianlei.rpc.common.serialize.RpcResponse
import com.qianlei.rpc.common.serialize.Serializer
import mu.KotlinLogging
import java.lang.reflect.Method
import java.net.ServerSocket
import kotlin.concurrent.thread

class BioServer(
    private val port: Int,
    private val serializer: Serializer,
    private val methods: Map<String, Method>,
    private val objects: Map<String, Any>
) {
    private val logger = KotlinLogging.logger { }
    fun start() {
        ServerSocket(port).use { serverSocket ->
            while (true) {
                val socket = serverSocket.accept()
                logger.debug { "接收到socket请求,来自于${socket.remoteSocketAddress}" }
                thread {
                    try {
                        val encodeRequest = socket.getInputStream().bufferedReader().readLine()
                        val rawRequest = HexUtil.decodeHex(encodeRequest)
                        val rpcRequest = serializer.parseRequest(rawRequest) { serviceName, methodName ->
                            val key = "$serviceName#$methodName"
                            methods[key]?.parameters ?: throw RuntimeException("编码信息错误")
                        }

                        logger.debug { "${socket.remoteSocketAddress}请求服务名:${rpcRequest.serviceName} " }
                        logger.debug { "${socket.remoteSocketAddress}请求方法名:${rpcRequest.methodName} " }
                        logger.debug { "${socket.remoteSocketAddress}请求参数:${rpcRequest.args.contentDeepToString()} " }
                        val rpcResponse = try {
                            val obj = objects[rpcRequest.serviceName]
                            val method = methods["${rpcRequest.serviceName}#${rpcRequest.methodName}"]
                            val result = method?.invoke(obj, *rpcRequest.args)
                            RpcResponse(result)
                        } catch (e: Exception) {
                            e.cause.let {
                                if (it == null) {
                                    RpcResponse(exception = "未知异常")
                                } else {
                                    RpcResponse(exception = it.message)
                                }
                            }
                        }
                        logger.debug { "向${socket.remoteSocketAddress}返回响应结果: $rpcResponse" }
                        val rawResponse = serializer.serializeResponse(rpcResponse)
                        val response = HexUtil.encodeHexStr(rawResponse)
                        socket.getOutputStream().write("$response\n".encodeToByteArray())
                    } catch (e: Exception) {
                        logger.error(e) { "出现异常" }
                    } finally {
                        socket.close()
                    }
                }
            }
        }
    }
}