package com.qianlei.rpc.common.serialize

data class RpcResponse<T>(val value: T? = null, val exception: String? = null)