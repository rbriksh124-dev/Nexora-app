package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.database.NexoraDao
import com.example.data.models.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NexoraRepository(
    private val dao: NexoraDao,
    private val firestore: FirebaseFirestore? = null
) {
    companion object {
        fun create(context: Context): NexoraRepository {
            val db = com.example.data.database.NexoraDatabase.getDatabase(context)
            val fs = try {
                val dbId = context.getString(R.string.firestore_database_id)
                FirebaseFirestore.getInstance(dbId)
            } catch (e: Exception) {
                null
            }
            return NexoraRepository(db.dao(), fs)
        }
    }

    private val auth get() = Firebase.auth
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    // History
    val historyList: Flow<List<HistoryItem>> = dao.getAllHistory()

    suspend fun recordHistory(toolId: String, toolName: String, category: String, input: String, result: String) {
        val item = HistoryItem(
            toolId = toolId,
            toolName = toolName,
            category = category,
            inputSummary = input,
            resultSummary = result,
            timestampMs = System.currentTimeMillis()
        )
        dao.insertHistory(item)

        // Sync with Firestore if user signed in
        val user = auth.currentUser
        if (user != null && firestore != null) {
            coroutineScope.launch {
                try {
                    val uid = user.uid
                    val payload = mapOf(
                        "userId" to uid,
                        "toolId" to toolId,
                        "toolName" to toolName,
                        "category" to category,
                        "inputSummary" to input.take(500),
                        "resultSummary" to result.take(500),
                        "timestamp" to FieldValue.serverTimestamp()
                    )
                    firestore.collection("users").document(uid).collection("history").document().set(payload)
                } catch (_: Exception) {}
            }
        }
    }

    suspend fun clearHistory() {
        dao.clearHistory()
    }

    // Favorites
    val favoritesList: Flow<List<FavoriteItem>> = dao.getAllFavorites()

    fun isFavorite(toolId: String): Flow<Boolean> = dao.isFavorite(toolId)

    suspend fun toggleFavorite(toolId: String, toolName: String, category: String, isFavNow: Boolean) {
        if (isFavNow) {
            dao.removeFavorite(toolId)
            val user = auth.currentUser
            if (user != null && firestore != null) {
                coroutineScope.launch {
                    try {
                        firestore.collection("users").document(user.uid).collection("favorites").document(toolId).delete()
                    } catch (_: Exception) {}
                }
            }
        } else {
            val fav = FavoriteItem(toolId = toolId, toolName = toolName, category = category, addedAtMs = System.currentTimeMillis())
            dao.addFavorite(fav)
            val user = auth.currentUser
            if (user != null && firestore != null) {
                coroutineScope.launch {
                    try {
                        val payload = mapOf(
                            "userId" to user.uid,
                            "toolId" to toolId,
                            "toolName" to toolName,
                            "category" to category,
                            "iconName" to "",
                            "addedAt" to FieldValue.serverTimestamp()
                        )
                        firestore.collection("users").document(user.uid).collection("favorites").document(toolId).set(payload)
                    } catch (_: Exception) {}
                }
            }
        }
    }

    // Todos
    val todosList: Flow<List<TodoEntity>> = dao.getAllTodos()

    suspend fun addTodo(title: String, description: String = "", priority: String = "Normal", category: String = "General") {
        dao.insertTodo(TodoEntity(title = title, description = description, priority = priority, category = category))
    }

    suspend fun updateTodo(todo: TodoEntity) {
        dao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoEntity) {
        dao.deleteTodo(todo)
    }

    // Notes
    val notesList: Flow<List<NoteEntity>> = dao.getAllNotes()

    suspend fun addNote(title: String, content: String, category: String = "General", colorHex: String = "#A855F7") {
        dao.insertNote(NoteEntity(title = title, content = content, category = category, colorHex = colorHex))
    }

    suspend fun updateNote(note: NoteEntity) {
        dao.updateNote(note)
    }

    suspend fun deleteNote(note: NoteEntity) {
        dao.deleteNote(note)
    }

    // Expenses
    val expensesList: Flow<List<ExpenseEntity>> = dao.getAllExpenses()

    suspend fun addExpense(title: String, amount: Double, category: String, currency: String, date: String) {
        dao.insertExpense(ExpenseEntity(title = title, amount = amount, category = category, currency = currency, date = date))
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        dao.deleteExpense(expense)
    }

    suspend fun clearAllLocalData() {
        dao.clearHistory()
        dao.clearTodos()
        dao.clearNotes()
        dao.clearExpenses()
    }
}
