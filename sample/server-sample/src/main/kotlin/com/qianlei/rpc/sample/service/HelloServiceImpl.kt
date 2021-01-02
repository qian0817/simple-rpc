package com.qianlei.rpc.sample.service

import com.qianlei.rpc.server.annotation.RpcService

/**
 * @author qianlei
 */
@RpcService(name = "helloService")
class HelloServiceImpl {
    fun hello(msg: String): String {
        println("收到客户端消息:$msg")
        return "hello $msg"
    }
}