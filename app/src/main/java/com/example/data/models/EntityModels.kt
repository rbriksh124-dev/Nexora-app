package com.example.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@Entity(tableName = "local_history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val toolId: String = "",
    val toolName: String = "",
    val category: String = "",
    val inputSummary: String = "",
    val resultSummary: String = "",
    val timestampMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "local_favorites")
data class FavoriteItem(
    @PrimaryKey val toolId: String = "",
    val toolName: String = "",
    val category: String = "",
    val addedAtMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "local_todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: String = "Normal",
    val category: String = "General",
    val createdAtMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "local_notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val category: String = "General",
    val colorHex: String = "#A855F7",
    val createdAtMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "local_expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "General",
    val currency: String = "₹",
    val date: String = "",
    val createdAtMs: Long = System.currentTimeMillis()
)

// Firestore Synchronizable Models with no-arg constructors
@IgnoreExtraProperties
data class FirestoreFavorite(
    @DocumentId val id: String = "",
    val userId: String = "",
    val toolId: String = "",
    val toolName: String = "",
    val category: String = "",
    val iconName: String = "",
    @ServerTimestamp val addedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreHistory(
    @DocumentId val id: String = "",
    val userId: String = "",
    val toolId: String = "",
    val toolName: String = "",
    val category: String = "",
    val inputSummary: String = "",
    val resultSummary: String = "",
    @ServerTimestamp val timestamp: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreTodo(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val priority: String = "Normal",
    val category: String = "General",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreNote(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val content: String = "",
    val category: String = "General",
    val colorHex: String = "#A855F7",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class FirestoreExpense(
    @DocumentId val id: String = "",
    val userId: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val category: String = "General",
    val currency: String = "₹",
    val date: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
