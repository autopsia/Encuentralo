package com.sectordefectuoso.encuentralo.utils
/**
 * Created by Gastón Saillén on 15 January 2020
 */
sealed class ResourceState<out T> {
    object Loading : ResourceState<Nothing>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Failed<T>(val message: String) : ResourceState<T>()
}