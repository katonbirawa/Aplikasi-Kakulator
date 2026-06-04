package com.example.kalkulator_app

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kalkulator_app.ui.theme.Kalkulator_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kalkulator_AppTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaxLayout(
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

fun calculateTax(
    amount: Double,
    taxRate: Double = 10.0,
    roundUp: Boolean
):String {
    var tax = amount * taxRate / 100
    if (roundUp == true) {
        tax = kotlin.math.ceil(x = tax)
    }

    return NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID")).format(tax);
}

@Composable
fun SwitchTax(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Text(text = stringResource(R.string.round_up_tax))
        androidx.compose.material3.Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Composable
fun EditTextNumber(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        singleLine = true,
        leadingIcon = { Icon(painterResource(id = leadingIcon),null)},
        keyboardOptions = keyboardOptions,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TaxLayout(modifier: Modifier = Modifier) {

    var amountInput by remember { mutableStateOf("") }
    var taxInput by remember { mutableStateOf(value = "") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    var roundUp by remember { mutableStateOf(value = false)}
    val taxPercentage = taxInput.toDoubleOrNull() ?: 0.0

    val tax = calculateTax(amount, taxPercentage, roundUp)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .statusBarsPadding()
            .safeDrawingPadding()
            .width(350.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tax),
            modifier = Modifier
                .padding(bottom = 20.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditTextNumber(
            label = R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChange = { amountInput = it },
            leadingIcon = R.drawable.ic_bill,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxSize()
        )

        Spacer ( modifier = Modifier.height(24.dp))

        EditTextNumber(
            label = R.string.tax_percentage,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = taxInput,
            onValueChange = { taxInput = it },
            leadingIcon = R.drawable.ic_tax,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxSize()
        )

        Spacer ( modifier = Modifier.height(24.dp))

        SwitchTax(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Spacer ( modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.Tax_amount, tax),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier,
            fontSize = 26.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Kalkulator_AppTheme {
        TaxLayout()
    }
}