package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import pe.edu.upc.calculator.features.calculator.domain.HistoryRepository

class SaveHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(history: List<HistoryItem>) {
        repository.saveHistory(history)
    }
}