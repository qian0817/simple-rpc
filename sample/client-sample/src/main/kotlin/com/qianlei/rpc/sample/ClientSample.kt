package com.qianlei.rpc.sample

import com.qianlei.rpc.client.proxy.BioInvocationHandler
import com.qianlei.rpc.common.serialize.JsonSerializer
import com.qianlei.rpc.sample.service.HelloService

fun main() {
    val helloService = BioInvocationHandler("127.0.0.1", 5678, HelloService::class.java, JsonSerializer()).proxy
    println(helloService.hello("world", "kotlin", "simple", "rpc"))
}