package net.adhikary.mrtbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.greetings
import mrtbuddy.composeapp.generated.resources.language
import net.adhikary.mrtbuddy.dao.DemoDao
import net.adhikary.mrtbuddy.database.AppDatabase
import net.adhikary.mrtbuddy.model.CardState
import net.adhikary.mrtbuddy.model.Transaction
import net.adhikary.mrtbuddy.nfc.getNFCManager
import net.adhikary.mrtbuddy.ui.components.MainScreen
import net.adhikary.mrtbuddy.ui.theme.MRTBuddyTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.log

@Composable
@Preview
fun App(dao: DemoDao) {
    var isRescanRequested = mutableStateOf(false)
    val scope = rememberCoroutineScope()
    val nfcManager = getNFCManager()
    val cardState by nfcManager.cardState.collectAsStateWithLifecycle()
    val transactions by nfcManager.transactions.collectAsStateWithLifecycle()

    val McardState = remember { mutableStateOf<CardState>(CardState.WaitingForTap) }
    val Mtransactions = remember { mutableStateOf<List<Transaction>>(emptyList()) }

    if(isRescanRequested.value){
        nfcManager.startScan()
        isRescanRequested.value = false
    }

    scope.launch {
        nfcManager.transactions.collectLatest {
            Mtransactions.value = it
        }
    }
    scope.launch {
        nfcManager.cardState.collectLatest {
            McardState.value = it
        }
    }

    nfcManager.startScan()

    MRTBuddyTheme {
        var lang by remember { mutableStateOf(Language.English.isoFormat) }
        LocalizedApp (
            language= lang
        ) {
            Scaffold {
                Box(
                    Modifier.systemBarsPadding()
                ) {
                    Column {
                        TopAppBar(
                            title = {
                                Text(text = stringResource(Res.string.greetings))
                            },
                            backgroundColor = MaterialTheme.colors.background,
                            actions = {
                                Button(onClick = {
                                    lang = switchLanguage(lang)
                                    changeLang(lang)
                                }) {
                                    Text(text = stringResource(Res.string.language))
                                }
                            }
                        )

                        MainScreen(
                            cardState = McardState.value,
                            transactions = Mtransactions.value,
                            onTapClick = {
                                if(getPlatform().name != "android"){
                                    isRescanRequested.value = true
                                }
                            }
                        )
                    }
                }
            }


        }

    }
}

private fun switchLanguage(lang: String) : String{
    print("Switching language")
    return when (lang) {
        Language.English.isoFormat -> Language.Bangla.isoFormat
        Language.Bangla.isoFormat -> Language.English.isoFormat
        else -> Language.English.isoFormat
    }
}
