package com.example.conversordemonedas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.conversordemonedas.ui.theme.ConversorDeMonedasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConversorDeMonedasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CurrencyConverterScreen()
                }
            }
        }
    }
}

@Composable
fun CurrencyConverterScreen() {
    var amountInput by remember { mutableStateOf("") }
    var selectedFrom by remember { mutableStateOf("COP") }
    var selectedTo by remember { mutableStateOf("USD") }
    var result by remember { mutableStateOf("") }

    val currencies = listOf("COP", "USD", "EUR")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Conversor de Monedas", fontSize = 24.sp)

        OutlinedTextField(
            value = amountInput,
            onValueChange = { amountInput = it },
            label = { Text("Cantidad") },
            singleLine = true
        )

        CurrencyDropdown("De:", selectedFrom, currencies) { selectedFrom = it }
        CurrencyDropdown("A:", selectedTo, currencies) { selectedTo = it }

        Button(onClick = {
            result = convertCurrency(amountInput, selectedFrom, selectedTo)
        }) {
            Text("Convertir")
        }

        if (result.isNotEmpty()) {
            Text("Resultado: $result", fontSize = 20.sp)
        }
    }
}

@Composable
fun CurrencyDropdown(label: String, selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label)
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selected)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            onSelected(currency)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

fun convertCurrency(amountStr: String, from: String, to: String): String {
    val rates = mapOf(
        "COP" to 1.0,
        "USD" to 0.00026,
        "EUR" to 0.00023
    )

    val amount = amountStr.toDoubleOrNull()
    if (amount == null) return "Cantidad inválida"

    val base = amount / (rates[from] ?: 1.0)
    val converted = base * (rates[to] ?: 1.0)

    return "%.2f $to".format(converted)
}
