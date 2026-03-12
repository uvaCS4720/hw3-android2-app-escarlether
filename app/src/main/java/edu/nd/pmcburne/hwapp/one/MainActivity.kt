package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.nd.pmcburne.hwapp.one.data.local.AppDatabase
import edu.nd.pmcburne.hwapp.one.data.remote.NcaaApi
import edu.nd.pmcburne.hwapp.one.data.repository.GameRepository
import edu.nd.pmcburne.hwapp.one.ui.theme.NcaaTheme
import edu.nd.pmcburne.hwapp.one.ui.theme.screens.ScoreboardScreen
import edu.nd.pmcburne.hwapp.one.ui.theme.viewmodel.GameViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize Retrofit (The Network)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ncaa-api.henrygd.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(NcaaApi::class.java)

        // 2. Initialize Room (The Local Database)
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.gameDao()

        // 3. Initialize Repository
        val repository = GameRepository(api, dao)

        // 4. Create the ViewModel
        val viewModel = GameViewModel(repository)

        setContent {
            // FIX: Use NcaaTheme instead of MaterialTheme to apply your custom colors
            NcaaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScoreboardScreen(viewModel)
                }
            }
        }

        // Initial data fetch
        viewModel.refresh()
    }
}
