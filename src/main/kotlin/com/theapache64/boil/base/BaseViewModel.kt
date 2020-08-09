package com.theapache64.boil.base

abstract class BaseViewModel<T> {
    abstract suspend fun call(command: T): Int
}