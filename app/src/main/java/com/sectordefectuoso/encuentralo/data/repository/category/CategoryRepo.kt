package com.sectordefectuoso.encuentralo.data.repository.category

import com.google.firebase.firestore.FirebaseFirestore
import com.sectordefectuoso.encuentralo.data.model.Category
import com.sectordefectuoso.encuentralo.data.model.SubCategory
import com.sectordefectuoso.encuentralo.utils.ResourceState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class CategoryRepo : ICategoryRepo {
    private val db = FirebaseFirestore.getInstance()
    private val categoryRef = db.collection("categories")

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

    override suspend fun getSubcategoriesByCategory(idCategory: String): Flow<ResourceState<List<SubCategory>>> = callbackFlow {
        val subscription = categoryRef.document(idCategory).collection("subcategories").addSnapshotListener { snapshot, exception ->
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

    override suspend fun getCategoryBySubcategory(idSubCategory: String): Flow<ResourceState<String>> = flow {
        var key = String()
        val documents = db.collectionGroup("subcategories").get().await()
        for (document in documents) {
            if(document.id == idSubCategory) {
                key = document.reference.parent.parent!!.id
            }
        }

        emit(ResourceState.Success(key))
    }
}