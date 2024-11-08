package net.adhikary.mrtbuddy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.currentBalance
import mrtbuddy.composeapp.generated.resources.enableNfc
import mrtbuddy.composeapp.generated.resources.nfcOff
import mrtbuddy.composeapp.generated.resources.noNfcSupport
import mrtbuddy.composeapp.generated.resources.readingCard
import mrtbuddy.composeapp.generated.resources.requiredNfc
import mrtbuddy.composeapp.generated.resources.rescan
import mrtbuddy.composeapp.generated.resources.tapToRead
import net.adhikary.mrtbuddy.getPlatform
import net.adhikary.mrtbuddy.model.CardState
import net.adhikary.mrtbuddy.translateNumber
import org.jetbrains.compose.resources.stringResource

@Composable
fun BalanceCard(
    cardState: CardState,
    onTapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(24.dp), // Increased corner radius
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Box(Modifier.fillMaxSize().padding(24.dp)) { // Increased padding
            if (getPlatform().name != "android") {
                Text(
                    text = stringResource(Res.string.rescan),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable { onTapClick() },
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.primary
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (cardState) {
                    is CardState.Balance -> BalanceContent(amount = cardState.amount)
                    CardState.Reading -> ReadingContent()
                    CardState.WaitingForTap -> WaitingContent()
                    is CardState.Error -> ErrorContent(message = cardState.message)
                    CardState.NoNfcSupport -> NoNfcSupportContent()
                    CardState.NfcDisabled -> NfcDisabledContent()
                }
            }
        }
    }
}

@Composable
private fun BalanceContent(amount: Int) {
    Text(
        text = stringResource(Res.string.currentBalance),
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "৳ ${translateNumber(amount)}", // Added currency symbol
        style = MaterialTheme.typography.h4.copy(
            fontWeight = FontWeight.SemiBold // Less bold for iOS
        ),
        color = MaterialTheme.colors.onSurface
    )
}

@Composable
private fun ReadingContent() {
    Text(
        text = stringResource(Res.string.readingCard),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface
    )
}

@Composable
private fun WaitingContent() {
    Text(
        text = stringResource(Res.string.tapToRead),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colors.onSurface,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ErrorContent(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.error,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun NoNfcSupportContent() {
    Text(
        text = stringResource(Res.string.noNfcSupport),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.error,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(Res.string.requiredNfc),
        style = MaterialTheme.typography.h4,
        color = MaterialTheme.colors.error.copy(alpha = 0.7f),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun NfcDisabledContent() {
    Text(
        text = stringResource(Res.string.nfcOff),
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.error,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(Res.string.enableNfc),
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colors.error.copy(alpha = 0.7f),
        textAlign = TextAlign.Center
    )
}
