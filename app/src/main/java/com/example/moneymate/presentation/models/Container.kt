package com.example.moneymate.presentation.models

sealed class Container<T>

class SuccessContainer<T>(
    val value: T
) : Container<T>()

class ErrorContainer<T>(
    val error: Throwable
) : Container<T>()

class PendingContainer<T> : Container<T>()