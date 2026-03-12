package edu.nd.pmcburne.hwapp.one.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey val gameId: String,
    val gender: String, // "men" or "women"
    val date: String,   // formatted as "yyyy/MM/dd"
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val status: String,    // "pregame", "live", or "final"
    val timeRemaining: String,
    val period: String,
    val startTime: String,
    val isHomeWinner: Boolean = false,
    val isAwayWinner: Boolean = false
)
