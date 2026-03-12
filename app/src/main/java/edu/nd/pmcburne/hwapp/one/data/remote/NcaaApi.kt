package edu.nd.pmcburne.hwapp.one.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

interface NcaaApi {
    @GET("scoreboard/basketball-{gender}/d1/{year}/{month}/{day}/data.json")
    suspend fun getScoreboard(
        @Path("gender") gender: String,
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    ): ScoreboardResponse
}

data class ScoreboardResponse(
    // The JSON returns a list of objects that each contain a "game"
    val games: List<GameWrapper>?
)

data class GameWrapper(
    val game: NetworkGame?
)

data class NetworkGame(
    @SerializedName("gameID") val gameId: String?,
    val home: TeamData?,
    val away: TeamData?,
    @SerializedName("gameState") val status: String?,
    @SerializedName("currentPeriod") val period: String?,
    val startTime: String?,
    val startDate: String?
)

data class TeamData(
    val names: TeamNames?,
    val score: String?,
    val winner: Boolean?
)

data class TeamNames(
    val short: String?,
    val full: String?
)