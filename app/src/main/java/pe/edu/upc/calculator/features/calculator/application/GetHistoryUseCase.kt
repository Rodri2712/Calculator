package pe.edu.upc.calculator.features.calculator.application

import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import pe.edu.upc.calculator.features.calculator.domain.HistoryRepository

class GetHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    operator fun invoke(): List<HistoryItem> {
        return repository.getHistory()
    }
}