package edu.nd.pmcburne.hwapp.one.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.nd.pmcburne.hwapp.one.data.local.GameEntity
import edu.nd.pmcburne.hwapp.one.ui.theme.viewmodel.GameViewModel
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreboardScreen(viewModel: GameViewModel) {
    val games by viewModel.games.collectAsState()
    // Extract the boolean value from the MutableState
    val isRefreshing by viewModel.isLoading

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now()
            .atZone(java.time.ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    )

    // Initial load when app starts
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // FIX: Use ZoneOffset.UTC instead of ZoneId.systemDefault()
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneOffset.UTC) // Force UTC to avoid time-zone rollback
                            .toLocalDate()

                        viewModel.selectedDate.value = date
                        viewModel.refresh()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "College Basketball Scores", // Updated Title
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary // Matches your Purple40
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Controls: Date Selector and Gender Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(viewModel.selectedDate.value.toString())
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("M", style = MaterialTheme.typography.labelLarge)
                    Switch(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        checked = !viewModel.isMen.value, // Switched logic for Women toggle
                        onCheckedChange = {
                            viewModel.isMen.value = !it
                            viewModel.refresh()
                        }
                    )
                    Text("W", style = MaterialTheme.typography.labelLarge)
                }
            }

            // The Scrollable List with Pull-to-Refresh
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(games) { game ->
                        GameCard(game)
                    }
                }
            }
        }
    }
}
@Composable
fun GameCard(game: GameEntity) {
    val isFinal = game.status.lowercase().contains("final")
    val isLive = game.status.lowercase().contains("live")
    val isPre = game.status.lowercase().contains("pre") || (!isFinal && !isLive && game.homeScore == 0 && game.awayScore == 0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Live Time Remaining and Period Logic
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        isLive -> "🔴 LIVE • ${game.period}"
                        isFinal -> "✔ FINAL"
                        else -> "Tip-off: ${game.startTime}"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isLive) Color.Red else MaterialTheme.colorScheme.secondary
                )

                // Show the actual game clock if the game is Live
                if (isLive && game.timeRemaining.isNotEmpty()) {
                    Text(
                        text = game.timeRemaining,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 1. AWAY TEAM (Top)
            TeamRow(
                name = game.awayTeam,
                score = game.awayScore,
                isWinner = game.isAwayWinner,
                opponentScore = game.homeScore,
                isPre = isPre,
                label = "AWAY"
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            // 2. HOME TEAM (Bottom)
            TeamRow(
                name = game.homeTeam,
                score = game.homeScore,
                isWinner = game.isHomeWinner,
                opponentScore = game.awayScore,
                isPre = isPre,
                label = "HOME"
            )
        }
    }
}

@Composable
fun TeamRow(
    name: String,
    score: Int,
    isWinner: Boolean,
    opponentScore: Int,
    isPre: Boolean,
    label: String // "HOME" or "AWAY"
) {
    val scoreColor = when {
        isPre -> MaterialTheme.colorScheme.onSurfaceVariant
        score == opponentScore && score > 0 -> Color(0xFF03A9F4)
        isWinner || score > opponentScore -> Color(0xFF2E7D32)
        else -> Color(0xFFD32F2F)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Small label for Home/Away
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall, // Very small text
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                letterSpacing = 0.5.sp
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isWinner || (!isPre && score > opponentScore)) FontWeight.Bold else FontWeight.Normal,
                color = if (isWinner || !isPre && score >= opponentScore)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!isPre) {
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontFeatureSettings = "enum"
                ),
                fontWeight = if (isWinner || score > opponentScore) FontWeight.ExtraBold else FontWeight.Normal,
                color = scoreColor,
                modifier = Modifier.width(70.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
    }
}