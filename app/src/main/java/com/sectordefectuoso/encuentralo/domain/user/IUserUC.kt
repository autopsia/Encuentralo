package com.sectordefectuoso.encuentralo.domain.user

import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

interface IUserUC {
    suspend fun get(): Flow<ResourceState<User>>
    suspend fun getById(uid: String): ResourceState<User>
    suspend fun create(user: User): Flow<ResourceState<Boolean>>
    suspend fun update(user: User): Flow<ResourceState<Boolean>>
    suspend fun updateLastLogin(uid: String): Flow<ResourceState<Boolean>>
    suspend fun createAuth(email: String, password: String): ResourceState<String>
    suspend fun updatePassword(email: String): ResourceState<Boolean>
    suspend fun logout(): ResourceState<Boolean>
}