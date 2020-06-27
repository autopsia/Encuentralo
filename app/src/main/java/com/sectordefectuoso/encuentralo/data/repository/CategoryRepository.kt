package com.sectordefectuoso.encuentralo.data.repository

import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.utils.ResourceState
import com.google.firebase.firestore.FirebaseFirestore
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CategoryRepository @Inject constructor() : ICategoryRepository {
    private val categoryRef = FirebaseFirestore
        .getInstance()
        .collection("categories")

    override suspend fun getAll(): Flow<ResourceState<List<Category>>> = callbackFlow {
        val subscription = categoryRef.addSnapshotListener { snapshot, exception ->
            offer(
                ResourceState.Success(snapshot!!.toObjects(Category::class.java))
            )

            exception?.let {
                offer(ResourceState.Failed(it.message.toString()))
                cancel(it.message.toString())
            }
        }
        awaitClose {
            subscription.remove()
            cancel()
        }
    }

    override suspend fun getSubcategoriesFromCategory(id:String): Flow<ResourceState<List<SubCategory>>> = callbackFlow {
        val subscription = categoryRef.document(id).collection("subcategories").addSnapshotListener { snapshot, exception ->
            offer(
                ResourceState.Success(snapshot!!.toObjects(SubCategory::class.java))
            )

            exception?.let {
                offer(ResourceState.Failed(it.message.toString()))
                cancel(it.message.toString())
            }
        }
        awaitClose {
            subscription.remove()
            cancel()
        }
    }

    suspend fun getSubCategoriesByCategory(id:String): Flow<ResourceState<List<SubCategory>>> = callbackFlow {
        val subscription = categoryRef.document(id).collection("subcategories").get().addOnSuccessListener { document ->
            offer(
                ResourceState.Success(document!!.toObjects(SubCategory::class.java))
            )
        }.addOnFailureListener {
            offer(ResourceState.Failed(it.message.toString()))
            cancel(it.message.toString())
        }
        awaitClose {
            cancel()
        }
    }
}