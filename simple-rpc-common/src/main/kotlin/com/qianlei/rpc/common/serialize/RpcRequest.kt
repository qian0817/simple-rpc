package com.qianlei.rpc.common.serialize

data class RpcRequest(val args: Array<out Any?>, val serviceName: String, val methodName: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RpcRequest

        if (!args.contentEquals(other.args)) return false
        if (serviceName != other.serviceName) return false
        if (methodName != other.methodName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = args.contentHashCode()
        result = 31 * result + serviceName.hashCode()
        result = 31 * result + methodName.hashCode()
        return result
    }
}