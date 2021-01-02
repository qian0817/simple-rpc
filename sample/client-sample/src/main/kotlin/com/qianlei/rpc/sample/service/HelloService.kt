package com.qianlei.rpc.sample.service

import com.qianlei.rpc.client.annotation.RpcClient

@RpcClient("helloService")
interface HelloService {
    fun hello(msg: String): String
}