package edu.nd.pmcburne.hwapp.one.data.repository

import android.util.Log
import edu.nd.pmcburne.hwapp.one.data.local.GameDao
import edu.nd.pmcburne.hwapp.one.data.local.GameEntity
import edu.nd.pmcburne.hwapp.one.data.remote.NcaaApi
import kotlinx.coroutines.flow.Flow

class GameRepository(
    private val api: NcaaApi,
    private val dao: GameDao
) {
    // 1. Get data from Local Database (Flow allows real-time UI updates)
    fun getGamesFromDb(date: String, gender: String): Flow<List<GameEntity>> {
        return dao.getGames(date, gender)
    }

    // 2. Fetch from API and Update Local Database
    suspend fun refreshGames(gender: String, year: String, month: String, day: String) {
        try {
            // 1. Fetch from Network
            val response = api.getScoreboard(gender, year, month, day)

            // We now map over GameWrapper objects
            val wrapperList = response.games ?: emptyList()

            // 2. Map Network objects to Database Entities
            val entities = wrapperList.mapNotNull { wrapper ->
                // Dig into the 'game' object inside the wrapper
                val g = wrapper.game ?: return@mapNotNull null

                // Ensure we have a valid ID and team data
                val gameId = g.gameId ?: return@mapNotNull null
                val homeData = g.home ?: return@mapNotNull null
                val awayData = g.away ?: return@mapNotNull null

                GameEntity(
                    gameId = gameId,
                    gender = gender,
                    // Use the same format used in the ViewModel query: yyyy/MM/dd
                    date = "$year/$month/$day",
                    homeTeam = homeData.names?.short ?: "Unknown",
                    awayTeam = awayData.names?.short ?: "Unknown",
                    homeScore = homeData.score?.toIntOrNull() ?: 0,
                    awayScore = awayData.score?.toIntOrNull() ?: 0,
                    status = g.status ?: "",
                    // Using currentPeriod as a fallback for period
                    period = g.period ?: "",
                    timeRemaining = "", // Update this if your API provides a specific 'time' field
                    startTime = g.startTime ?: "",
                    isHomeWinner = homeData.winner ?: false,
                    isAwayWinner = awayData.winner ?: false
                )
            }

            // 3. Save to Database
            if (entities.isNotEmpty()) {
                dao.insertGames(entities)
                Log.d("DB_CHECK", "Successfully inserted ${entities.size} games into Room")
            } else {
                Log.d("DB_CHECK", "No valid games found in the response for this date")
            }
        } catch (e: Exception) {
            Log.e("DB_CHECK", "Critical error in Repository: ${e.message}")
            e.printStackTrace()
        }
    }
}