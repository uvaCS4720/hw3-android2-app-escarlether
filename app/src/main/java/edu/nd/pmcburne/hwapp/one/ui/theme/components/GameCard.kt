package edu.nd.pmcburne.hwapp.one.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.data.local.GameEntity

@Composable
fun GameCard(game: GameEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Away Team Row
            TeamRow(name = game.awayTeam, score = game.awayScore, isWinner = game.isAwayWinner)

            Spacer(modifier = Modifier.height(8.dp))

            // Home Team Row
            TeamRow(name = game.homeTeam, score = game.homeScore, isWinner = game.isHomeWinner)

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            // Status Logic(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            // Status Logic (Requirement: Final vs Time Remaining)
            val statusColor = if (game.status.lowercase() == "live") Color.Red else Color.Gray
            val statusText = when (game.status.lowercase()) {
                "final" -> "FINAL"
                "live" -> "${game.period} • ${game.timeRemaining}"
                else -> "Tip-off: ${game.startTime}" // Upcoming
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.labelLarge,
                color = statusColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TeamRow(name: String, score: Int, isWinner: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = score.toString(),
            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal
        )
    }
}