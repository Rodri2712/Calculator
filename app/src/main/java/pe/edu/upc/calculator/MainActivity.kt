package pe.edu.upc.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.upc.calculator.features.calculator.presentation.CalculatorViewModel
import pe.edu.upc.calculator.main.MainScreen
import pe.edu.upc.calculator.core.designsystems.theme.CalculatorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val calculatorViewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CalculatorTheme {
                MainScreen(
                    calculatorViewModel = calculatorViewModel
                )
            }
        }
    }
}