package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem

class AddHistoryItemUseCase @Inject constructor() {

    operator fun invoke(
        operation: String,
        result: String,
        currentHistory: List<HistoryItem>
    ): List<HistoryItem> {
        val newItem = HistoryItem(
            operation = operation,
            result = result
        )

        return listOf(newItem) + currentHistory
    }
}