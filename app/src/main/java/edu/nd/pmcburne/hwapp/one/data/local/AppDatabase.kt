package edu.nd.pmcburne.hwapp.one.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// 1. Define the entities and the version
@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. Connect the DAO
    abstract fun gameDao(): GameDao

    // 3. Create a Singleton to prevent multiple instances of the DB opening
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ncaa_scores_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}