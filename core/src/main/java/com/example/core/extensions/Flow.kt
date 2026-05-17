package com.example.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

fun <T> Flow<T>.mapToResult(): Flow<Result<T>> =
    map { Result.success(it) }.catch { emit(Result.failure(it)) }
