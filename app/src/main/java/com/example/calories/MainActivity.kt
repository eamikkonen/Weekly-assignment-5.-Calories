package com.example.calories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calories.ui.theme.CaloriesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloriesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CaloriesCalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CaloriesCalculatorApp() {
    var weightInput by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var selectedIntensity by remember { mutableStateOf("Light") }
    var expanded by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf(0) }

    val intensityOptions = listOf("Light", "Moderate", "Hard")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = weightInput,
            onValueChange = { weightInput = it },
            label = { Text("Enter weight") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select gender")
        Row {
            RadioButton(
                selected = gender == "Male",
                onClick = { gender = "Male" }
            )
            Text(text = "Male", modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = gender == "Female",
                onClick = { gender = "Female" }
            )
            Text(text = "Female")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select intensity")
        Spacer(modifier = Modifier.height(8.dp))

        Box {
            OutlinedButton(onClick = { expanded = !expanded }) {
                Text(selectedIntensity)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                intensityOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        selectedIntensity = option
                        expanded = false
                    }) {
                        Text(text = option)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val weight = weightInput.toIntOrNull() ?: 0
            result = calculateCalories(weight, gender, selectedIntensity)
        }) {
            Text(text = "Calculate")
        }

        if (result > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Calories burned: $result", style = MaterialTheme.typography.h6)
        }
    }
}

fun calculateCalories(weight: Int, gender: String, intensity: String): Int {
    val baseCalories = when (intensity) {
        "Light" -> 200
        "Moderate" -> 300
        "Hard" -> 400
        else -> 0
    }

    val genderFactor = if (gender == "Male") 1.2 else 1.0
    return (baseCalories * genderFactor * (weight / 70.0)).toInt()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CaloriesTheme {
        CaloriesCalculatorApp()
    }
}
