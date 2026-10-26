package pe.edu.upc.calculator.features.calculator.presentation

import pe.edu.upc.calculator.features.calculator.application.AddHistoryItemUseCase
import pe.edu.upc.calculator.features.calculator.application.ClearHistoryUseCase
import pe.edu.upc.calculator.features.calculator.application.SaveHistoryUseCase
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import javax.inject.Inject

class CalculatorHistoryHandler @Inject constructor(
    private val addHistoryItem: AddHistoryItemUseCase,
    private val saveHistory: SaveHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase
) {

    fun addItem(
        operation: String,
        result: String,
        currentHistory: List<HistoryItem>
    ): List<HistoryItem> {
        return addHistoryItem(
            operation = operation,
            result = result,
            currentHistory = currentHistory
        )
    }

    fun save(history: List<HistoryItem>) {
        saveHistory(history)
    }

    fun clear() {
        clearHistoryUseCase()
    }
}