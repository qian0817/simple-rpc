package com.qianlei.rpc.server

import cn.hutool.core.util.HexUtil
import com.qianlei.rpc.common.serialize.JsonSerializer
import com.qianlei.rpc.common.serialize.RpcResponse
import com.qianlei.rpc.common.serialize.Serializer
import mu.KotlinLogging
import java.lang.Exception
import java.lang.reflect.Method
import java.net.ServerSocket
import kotlin.concurrent.thread

class BioServer(
    private val port: Int,
    private val serializer: Serializer,
    private val methods: Map<String, Pair<Any, Method>>
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
                        val rpcRequest = serializer.parseRequest(rawRequest)
                        logger.debug { "${socket.remoteSocketAddress}请求服务名:${rpcRequest.serviceName} " }
                        logger.debug { "${socket.remoteSocketAddress}请求方法名:${rpcRequest.methodName} " }

                        val key = rpcRequest.serviceName + "#" + rpcRequest.methodName
                        val rpcResponse = try {
                            if (methods.containsKey(key)) {
                                val res = methods.getValue(key).let { (obj, method) ->
                                    val args = method.parameters.mapIndexed { index, parameter ->
                                        val value = serializer.serializeArgs(rpcRequest.args[index])
                                        serializer.parseArgs(value, parameter.type)
                                    }.toTypedArray()
                                    logger.debug { "${socket.remoteSocketAddress}请求参数:${args.contentDeepToString()} " }
                                    method.invoke(obj, *args)
                                }
                                RpcResponse(res, null)
                            } else {
                                RpcResponse(null, "未找到需要调用方法")
                            }
                        } catch (e: Exception) {
                            val cause = e.cause
                            if (cause == null) {
                                RpcResponse(null, "未知异常")
                            } else {
                                RpcResponse(null, cause.message)
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