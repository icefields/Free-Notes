package luci.sixsixsix.homemessageshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import luci.sixsixsix.homemessageshare.presentation.SettingsViewModel
import luci.sixsixsix.homemessageshare.presentation.main.MainScreen
import luci.sixsixsix.homemessageshare.ui.theme.HomeMessageShareTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            settingsViewModel = hiltViewModel<SettingsViewModel>(this)
            val dynamicColor = settingsViewModel.state.isMaterialYouEnabled
            HomeMessageShareTheme(
                dynamicColor = dynamicColor
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(settingsViewModel)
                }
            }
        }
    }
}
