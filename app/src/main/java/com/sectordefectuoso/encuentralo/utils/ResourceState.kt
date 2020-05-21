package com.sectordefectuoso.encuentralo.utils
/**
 * Created by Gastón Saillén on 15 January 2020
 */
sealed class ResourceState<out T> {
    class Loading<T> : ResourceState<T>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Failed<T>(val message: String) : ResourceState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}