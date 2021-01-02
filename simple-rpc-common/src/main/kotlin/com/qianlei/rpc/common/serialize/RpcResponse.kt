package com.qianlei.rpc.common.serialize

data class RpcResponse<T>(val value: T?, val exception: String?)