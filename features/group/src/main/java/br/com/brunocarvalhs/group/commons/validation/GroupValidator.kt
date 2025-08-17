package br.com.brunocarvalhs.group.commons.validation

import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import br.com.brunocarvalhs.group.R
import br.com.brunocarvalhs.group.app.create.GroupCreateUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object GroupValidator {

    fun validate(uiState: GroupCreateUiState): Map<String, Int> {
        val errors = mutableMapOf<String, Int>()

        // 1️⃣ Valida membros
        if (uiState.members.size < 3) {
            errors[GroupEntities.MEMBERS] = R.string.error_min_members
        }

        // 2️⃣ Valida informações do grupo
        if (uiState.name.isBlank()) {
            errors[GroupEntities.NAME] = R.string.error_name_required
        }

        if (uiState.description.isBlank()) {
            errors[GroupEntities.DESCRIPTION] = R.string.error_description_required
        }

        if (uiState.drawDate.isBlank()) {
            errors[GroupEntities.DATE] = R.string.error_draw_date_required
        } else if (!isDateValid(uiState.drawDate)) {
            errors[GroupEntities.DATE] = R.string.error_draw_date_invalid
        }

        if (uiState.minValue.isBlank()) {
            errors[GroupEntities.MIN_PRICE] = R.string.error_min_value_required
        } else if (uiState.minValue.toDoubleOrNull() == null) {
            errors[GroupEntities.MIN_PRICE] = R.string.error_min_value_invalid
        }

        if (uiState.maxValue.isBlank()) {
            errors[GroupEntities.MAX_PRICE] = R.string.error_max_value_required
        } else if (uiState.maxValue.toDoubleOrNull() == null) {
            errors[GroupEntities.MAX_PRICE] = R.string.error_max_value_invalid
        }

        val min = uiState.minValue.toDoubleOrNull()
        val max = uiState.maxValue.toDoubleOrNull()
        if (min != null && max != null && min > max) {
            errors[GroupEntities.MAX_PRICE] = R.string.error_max_value_greater
        }

        if (uiState.drawType.isBlank()) {
            errors[GroupEntities.TYPE] = R.string.error_draw_type_required
        }

        return errors
    }

    private fun isDateValid(date: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val parsed = dateFormat.parse(date)
            parsed != null && !parsed.before(today.time)
        } catch (e: Exception) {
            false
        }
    }
}
