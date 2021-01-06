package com.qianlei.rpc.sample

import com.qianlei.rpc.common.serialize.JsonSerializer
import com.qianlei.rpc.sample.service.HelloServiceImpl
import com.qianlei.rpc.server.BioServer

fun main() {
    val port = 5678
    val methods = mapOf("helloService#hello" to HelloServiceImpl::class.java.methods[0])
    val objects = mapOf("helloService" to HelloServiceImpl())
    BioServer(port, JsonSerializer(), methods, objects).start()
}