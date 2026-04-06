package br.com.brunocarvalhs.group.create.app.presentation.forms

import android.net.Uri
import br.com.brunocarvalhs.group.create.app.domain.model.ContactModel
import br.com.brunocarvalhs.group.create.app.domain.model.GroupModel
import java.util.UUID

data class FormsUiState(
    val name: String = "",
    val description: String = "",
    val date: String = "",
    val minPrice: String = "",
    val maxPrice: String = "",
    val members: List<ContactModel> = emptyList(),
    val contacts: Int = 0,
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPriceError: Boolean = false,
    val isValid: Boolean = false
)