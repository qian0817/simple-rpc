package com.qianlei.rpc.server.annotation

@Target(AnnotationTarget.CLASS)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class RpcService(val name: String)