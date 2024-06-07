package com.books.app.common

class Consumable<T>(private val consumable: T, private val sideEffect: (T) -> Unit) : () -> T {
    override fun invoke(): T {
        sideEffect(consumable)
        return consumable
    }
}
