package com.qianlei.rpc.client.annotation

@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class RpcClient(val name: String)