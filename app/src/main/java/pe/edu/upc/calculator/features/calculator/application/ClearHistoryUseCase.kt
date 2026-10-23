package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.HistoryRepository

class ClearHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke() {
        repository.clearHistory()
    }
}