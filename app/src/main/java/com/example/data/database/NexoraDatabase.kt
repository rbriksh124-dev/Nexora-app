package com.example.data.database

import androidx.room.*
import com.example.data.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NexoraDao {
    // History
    @Query("SELECT * FROM local_history ORDER BY timestampMs DESC LIMIT 100")
    fun getAllHistory(): Flow<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: HistoryItem)

    @Query("DELETE FROM local_history")
    suspend fun clearHistory()

    // Favorites
    @Query("SELECT * FROM local_favorites ORDER BY addedAtMs DESC")
    fun getAllFavorites(): Flow<List<FavoriteItem>>

    @Query("SELECT EXISTS(SELECT 1 FROM local_favorites WHERE toolId = :toolId)")
    fun isFavorite(toolId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(item: FavoriteItem)

    @Query("DELETE FROM local_favorites WHERE toolId = :toolId")
    suspend fun removeFavorite(toolId: String)

    // Todos
    @Query("SELECT * FROM local_todos ORDER BY createdAtMs DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    // Notes
    @Query("SELECT * FROM local_notes ORDER BY createdAtMs DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // Expenses
    @Query("SELECT * FROM local_expenses ORDER BY createdAtMs DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM local_todos")
    suspend fun clearTodos()

    @Query("DELETE FROM local_notes")
    suspend fun clearNotes()

    @Query("DELETE FROM local_expenses")
    suspend fun clearExpenses()
}

@Database(
    entities = [HistoryItem::class, FavoriteItem::class, TodoEntity::class, NoteEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NexoraDatabase : RoomDatabase() {
    abstract fun dao(): NexoraDao

    companion object {
        @Volatile
        private var INSTANCE: NexoraDatabase? = null

        fun getDatabase(context: android.content.Context): NexoraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NexoraDatabase::class.java,
                    "nexora_utility.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
