package com.qianlei.rpc.sample

import com.qianlei.rpc.client.proxy.BioInvocationHandler
import com.qianlei.rpc.common.register.ConsulRegisterService
import com.qianlei.rpc.common.serialize.JsonSerializer
import com.qianlei.rpc.sample.service.HelloService

fun main() {
    val registerService = ConsulRegisterService()
    val helloService = BioInvocationHandler(registerService, JsonSerializer(), HelloService::class.java).proxy
    println(helloService.hello("world", "kotlin", "simple", "rpc"))
}