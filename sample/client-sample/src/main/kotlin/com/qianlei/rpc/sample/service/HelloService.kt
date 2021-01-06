package com.qianlei.rpc.sample.service

import com.qianlei.rpc.client.annotation.RpcClient

@RpcClient("helloService")
interface HelloService {
    fun hello(vararg msg: String): String
}