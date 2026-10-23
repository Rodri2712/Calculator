package pe.edu.upc.calculator.features.calculator.domain

interface HistoryRepository {
    fun getHistory(): List<HistoryItem>

    fun saveHistory(history: List<HistoryItem>)

    fun clearHistory()
}