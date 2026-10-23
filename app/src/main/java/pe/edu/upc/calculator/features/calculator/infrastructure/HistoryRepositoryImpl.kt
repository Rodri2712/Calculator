package pe.edu.upc.calculator.features.calculator.infrastructure

import android.content.Context
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import pe.edu.upc.calculator.features.calculator.domain.HistoryRepository

class HistoryRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : HistoryRepository {

    private val preferences = context.getSharedPreferences(
        "calculator_preferences",
        Context.MODE_PRIVATE
    )

    override fun getHistory(): List<HistoryItem> {
        val savedHistory = preferences.getString(
            KEY_HISTORY,
            null
        ) ?: return emptyList()

        return try {
            val jsonArray = JSONArray(savedHistory)

            buildList {
                for (index in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(index)

                    add(
                        HistoryItem(
                            operation = item.getString("operation"),
                            result = item.getString("result")
                        )
                    )
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override fun saveHistory(history: List<HistoryItem>) {
        val jsonArray = JSONArray()

        history.forEach { item ->
            jsonArray.put(
                JSONObject()
                    .put("operation", item.operation)
                    .put("result", item.result)
            )
        }

        preferences.edit {
            putString(KEY_HISTORY, jsonArray.toString())
        }
    }

    override fun clearHistory() {
        preferences.edit {
            remove(KEY_HISTORY)
        }
    }

    private companion object {
        const val KEY_HISTORY = "calculator_history"
    }
}