package com.mathgate.app.core.exception

import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.utils.io.CancellationException
import java.net.UnknownHostException

fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is SocketTimeoutException -> AppException.Network.Timeout
        is UnknownHostException -> AppException.Network.NoInternet
        is CancellationException -> throw this
        is IllegalArgumentException -> throw this
        else -> AppException.Unknown
    }
}