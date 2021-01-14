package com.qianlei.rpc.common.register

interface IRegisterService {
    fun findAllService(serviceName: String): List<Service>

    fun registerService(serviceName: String, port: Int, host: String)

    fun unregisterService(serviceName: String, port: Int, host: String)
}