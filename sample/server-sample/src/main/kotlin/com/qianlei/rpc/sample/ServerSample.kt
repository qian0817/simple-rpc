package com.qianlei.rpc.sample

import com.qianlei.rpc.common.serialize.JsonSerializer
import com.qianlei.rpc.server.BioServer
import com.qianlei.rpc.sample.service.HelloServiceImpl

fun main() {
    BioServer(
        5678,
        JsonSerializer(),
        mapOf("helloService#hello" to (HelloServiceImpl() to HelloServiceImpl::class.java.methods[0]))
    ).start()
}