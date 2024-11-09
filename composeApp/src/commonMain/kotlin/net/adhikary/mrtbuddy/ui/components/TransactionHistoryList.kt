package net.adhikary.mrtbuddy.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDateTime
import mrtbuddy.composeapp.generated.resources.Res
import mrtbuddy.composeapp.generated.resources.agargaon
import mrtbuddy.composeapp.generated.resources.balanceUpdate
import mrtbuddy.composeapp.generated.resources.bangladeshSecretariat
import mrtbuddy.composeapp.generated.resources.bijoySarani
import mrtbuddy.composeapp.generated.resources.dhakaUniversity
import mrtbuddy.composeapp.generated.resources.farmgate
import mrtbuddy.composeapp.generated.resources.karwanBazar
import mrtbuddy.composeapp.generated.resources.kazipara
import mrtbuddy.composeapp.generated.resources.mirpur10
import mrtbuddy.composeapp.generated.resources.mirpur11
import mrtbuddy.composeapp.generated.resources.motijheel
import mrtbuddy.composeapp.generated.resources.pallabi
import mrtbuddy.composeapp.generated.resources.recentJourneys
import mrtbuddy.composeapp.generated.resources.shahbagh
import mrtbuddy.composeapp.generated.resources.shewrapara
import mrtbuddy.composeapp.generated.resources.uttaraCenter
import mrtbuddy.composeapp.generated.resources.uttaraNorth
import mrtbuddy.composeapp.generated.resources.uttaraSouth
import net.adhikary.mrtbuddy.model.TransactionType
import net.adhikary.mrtbuddy.model.TransactionWithAmount
import net.adhikary.mrtbuddy.nfc.service.TimestampService
import net.adhikary.mrtbuddy.translateNumber
import net.adhikary.mrtbuddy.ui.theme.LightPositiveGreen
import net.adhikary.mrtbuddy.ui.theme.DarkPositiveGreen
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionHistoryList(transactions: List<TransactionWithAmount>) {
    Card(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
        ) {
            Text(
                text =  stringResource(Res.string.recentJourneys),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.SemiBold
            )
            Divider(
                modifier = Modifier.padding(top = 12.dp, bottom = 16.dp),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(transactions) { transactionWithAmount ->
                    val isCommute = transactionWithAmount.transaction.fixedHeader.startsWith(
                        "08 52 10 00"
                    )
                    TransactionItem(
                        type = if (isCommute) TransactionType.Commute else TransactionType.BalanceUpdate,
                        date = transactionWithAmount.transaction.timestamp,
                        fromStation = transactionWithAmount.transaction.fromStation,
                        toStation = transactionWithAmount.transaction.toStation,
                        balance = "৳ ${transactionWithAmount.transaction.balance}",
                        amount = transactionWithAmount.amount?.let { "৳ ${translateNumber(it)}" } ?: "N/A",
                        amountValue = transactionWithAmount.amount
                    )

                    if (transactionWithAmount != transactions.last()) {
                        Divider(
                            modifier = Modifier.padding(top = 12.dp),
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    type: TransactionType,
    date: LocalDateTime,
    fromStation: String,
    toStation: String,
    balance: String,
    amount: String,
    amountValue: Int?
) {
    val isDarkTheme = isSystemInDarkTheme()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = if (type == TransactionType.Commute) "${translateStationToBangla(fromStation)} → ${translateStationToBangla(toStation)}" else stringResource(Res.string.balanceUpdate),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = TimestampService.formatDateTime(date),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            val amountColor = when {
                type == TransactionType.BalanceUpdate && (amountValue ?: 0) > 0 ->
                    if (isDarkTheme) DarkPositiveGreen else LightPositiveGreen
                else -> MaterialTheme.colors.primary
            }
            
            Text(
                text = amount,
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }
    }
}

@Composable
fun translateStationToBangla(stationName: String): String {
    return when (stationName) {
        "Motijheel" -> stringResource(Res.string.motijheel)
        "Bangladesh Secretariat" -> stringResource(Res.string.bangladeshSecretariat)
        "Dhaka University" -> stringResource(Res.string.dhakaUniversity)
        "Shahbagh" -> stringResource(Res.string.shahbagh)
        "Karwan Bazar" -> stringResource(Res.string.karwanBazar)
        "Farmgate" -> stringResource(Res.string.farmgate)
        "Bijoy Sarani" -> stringResource(Res.string.bijoySarani)
        "Agargaon" -> stringResource(Res.string.agargaon)
        "Shewrapara" -> stringResource(Res.string.shewrapara)
        "Kazipara" -> stringResource(Res.string.kazipara)
        "Mirpur 10" -> stringResource(Res.string.mirpur10)
        "Mirpur 11" -> stringResource(Res.string.mirpur11)
        "Pallabi" -> stringResource(Res.string.pallabi)
        "Uttara South" -> stringResource(Res.string.uttaraSouth)
        "Uttara Center" -> stringResource(Res.string.uttaraCenter)
        "Uttara North" -> stringResource(Res.string.uttaraNorth)
        else -> stationName // Default to English if no match is found
    }
}

// 09 Nov 2024, 12:00 PM
fun translateDate(dateStr: String): String {
    //check locale

    val dateParts = dateStr.split(" ")
    val month = when (dateParts[1]) {
        "Jan" -> "জানুয়ারি"
        "Feb" -> "ফেব্রুয়ারি"
        "Mar" -> "মার্চ"
        "Apr" -> "এপ্রিল"
        "May" -> "মে"
        "Jun" -> "জুন"
        "Jul" -> "জুলাই"
        "Aug" -> "আগস্ট"
        "Sep" -> "সেপ্টেম্বর"
        "Oct" -> "অক্টোবর"
        "Nov" -> "নভেম্বর"
        "Dec" -> "ডিসেম্বর"
        else -> dateParts[1]
    }
    val timeParts = dateParts[3].split(":")
    val hour = timeParts[0].toInt()
    val minute = timeParts[1]
    val amPm = if (hour < 12) "এএম" else "পিএম"
    val banglaHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val banglaTime = "$banglaHour:$minute $amPm"
    return "${dateParts[0]} $month ${dateParts[2]}, $banglaTime"
}
