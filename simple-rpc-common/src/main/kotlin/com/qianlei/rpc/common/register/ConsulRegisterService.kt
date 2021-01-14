package com.qianlei.rpc.common.register

import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import com.orbitz.consul.model.agent.Registration


class ConsulRegisterService(consulBuilder: Consul.Builder = Consul.builder()) : IRegisterService {
    private val client = consulBuilder.build()

    override fun findAllService(serviceName: String): List<Service> {
        return client
            .healthClient()
            .getAllServiceInstances(serviceName)
            .response
            .map { Service(it.service.address, it.service.port, it.service.service) }
    }

    override fun registerService(serviceName: String, port: Int, host: String) {
        val service = ImmutableRegistration.builder()
            .address(host)
            .id("$serviceName:$host:$port")
            .name(serviceName)
            .check(Registration.RegCheck.tcp("$host:$port", 10, 5))
            .port(port)
            .build()
        client.agentClient().register(service)
    }

    override fun unregisterService(serviceName: String, port: Int, host: String) {
        client.agentClient().deregister("$serviceName:$host:$port")
    }
}