package com.sectordefectuoso.encuentralo.data.repository

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class CategoryRepository : ICategoryRepository {
    override suspend fun getAll(): Flow<ResourceState<List<Category>>> ?{/*
        val docRef = FirebaseFirestore
            .getInstance()
            .collection("categories")
            .document("")

        val subscription = docRef.addSnapshotListener { snapshot, _ ->
            if(snapshot!!.exists()){
                val versionCode = snapshot.getLong("version")
                offer(ResourceState.Success(versionCode!!.toInt()))
            }
        }

        awaitClose { subscription.remove() } */
        return null
    }
}