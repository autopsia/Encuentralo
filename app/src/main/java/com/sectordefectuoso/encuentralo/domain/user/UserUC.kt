package com.sectordefectuoso.encuentralo.domain.user

import com.sectordefectuoso.encuentralo.data.model.User
import com.sectordefectuoso.encuentralo.data.repository.user.IUserRepo
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.flow.Flow

class UserUC(private val repo: IUserRepo) : IUserUC {
    override suspend fun get(): Flow<ResourceState<User>> = repo.get()

    override suspend fun getById(uid: String): ResourceState<User> = repo.getById(uid)

    override suspend fun create(user: User): Flow<ResourceState<Boolean>> = repo.create(user)

    override suspend fun update(user: User): Flow<ResourceState<Boolean>> = repo.update(user)

    override suspend fun updateLastLogin(uid: String): Flow<ResourceState<Boolean>> = repo.updateLastLogin(uid)

    override suspend fun createAuth(email: String, password: String): ResourceState<String> = repo.createAuth(email, password)

    override suspend fun updatePassword(email: String): ResourceState<Boolean> = repo.updatePassword(email)

    override suspend fun logout(): ResourceState<Boolean> = repo.logout()
}