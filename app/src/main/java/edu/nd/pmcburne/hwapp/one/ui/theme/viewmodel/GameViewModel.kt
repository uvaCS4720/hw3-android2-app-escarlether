package edu.nd.pmcburne.hwapp.one.ui.theme.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hwapp.one.data.local.GameEntity
import edu.nd.pmcburne.hwapp.one.data.repository.GameRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    // UI State
    var isLoading = mutableStateOf(false)
    var selectedDate = mutableStateOf(LocalDate.now())
    var isMen = mutableStateOf(true)

    // The logic to convert UI selection into a DB Query
    @OptIn(ExperimentalCoroutinesApi::class)
    val games: StateFlow<List<GameEntity>> = snapshotFlow {
        Pair(selectedDate.value, isMen.value)
    }.flatMapLatest { (date, men) ->
        val dateString = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        val gender = if (men) "men" else "women"
        repository.getGamesFromDb(dateString, gender)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun refresh() {
        viewModelScope.launch {
            isLoading.value = true
            val date = selectedDate.value
            repository.refreshGames(
                gender = if (isMen.value) "men" else "women",
                year = date.year.toString(),
                month = String.format("%02d", date.monthValue),
                day = String.format("%02d", date.dayOfMonth)
            )
            isLoading.value = false
        }
    }
}