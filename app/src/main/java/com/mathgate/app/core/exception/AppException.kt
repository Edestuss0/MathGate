package com.mathgate.app.core.exception

sealed class AppException(override val message: String) : Throwable() {
    sealed class Network(override val message: String) : AppException(message) {
        data class ServerError(val code: Int) : Network("Ошибка сервера: $code")
        data object NoInternet : Network("Нет подключения к интернету")
        data object Timeout : Network("Превышено время ожидания")
    }

    data object Unknown : AppException("Неизвестная ошибка")
}