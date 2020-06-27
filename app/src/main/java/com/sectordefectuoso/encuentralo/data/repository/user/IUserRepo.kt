package com.sectordefectuoso.encuentralo.data.repository.user

import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IUserRepo {
    suspend fun get(): Flow<ResourceState<User>>
    suspend fun getById(uid: String): ResourceState<User>
    suspend fun create(user: User): ResourceState<User>
    suspend fun update(user: User): ResourceState<User>
    suspend fun createAuth(email: String, password: String): ResourceState<String>
    suspend fun updateEmail(newEmail: String, prevEmail: String, password: String): ResourceState<Boolean>
    suspend fun updatePassword(email: String): ResourceState<Boolean>
    suspend fun logout(): ResourceState<Boolean>
}