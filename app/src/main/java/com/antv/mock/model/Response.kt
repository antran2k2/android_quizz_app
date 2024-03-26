package com.antv.mock.model

sealed class Response<T> (val data: T?, val message: String?) {
    class Success<T>(data: T?): Response<T>(data, message = null)
    class Fail<T>(message: String?): Response<T>(null, message)
}