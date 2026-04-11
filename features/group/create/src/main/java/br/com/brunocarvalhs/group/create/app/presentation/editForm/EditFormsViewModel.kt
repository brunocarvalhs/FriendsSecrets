package br.com.brunocarvalhs.group.create.app.presentation.editForm

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.compose.runtime.Stable
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.friendssecrets.domain.model.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupEditUseCase
import br.com.brunocarvalhs.group.create.commons.navigation.EditFormsRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@Stable
@HiltViewModel
class EditFormsViewModel @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle,
    private val groupEditUseCase: GroupEditUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<EditFormsRouter>(EditFormsRouter.typeMap)
    private val _uiState = MutableStateFlow(
        EditFormsUiState(
            name = args.group.name,
            description = args.group.description.orEmpty(),
            date = args.group.date.orEmpty(),
            minPrice = args.group.minPrice.toString(),
            maxPrice = args.group.maxPrice.toString(),
            members = args.group.members,
            imageUri = args.group.photo?.toUri(),
        )
    )
    val uiState: StateFlow<EditFormsUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: EditFormsIntent) = when (intent) {
        is EditFormsIntent.SaveGroup -> saveGroup(intent.onFinish)
        is EditFormsIntent.UpdateName -> updateState { copy(name = intent.name) }
        is EditFormsIntent.UpdateDescription -> updateState { copy(description = intent.description) }
        is EditFormsIntent.UpdateDate -> updateState { copy(date = intent.date) }
        is EditFormsIntent.UpdateMinPrice -> updateState { copy(minPrice = intent.minPrice) }
        is EditFormsIntent.UpdateMaxPrice -> updateState { copy(maxPrice = intent.maxPrice) }
        is EditFormsIntent.UpdateImage -> updateState { copy(imageUri = intent.uri) }
    }

    private fun updateState(update: EditFormsUiState.() -> EditFormsUiState) {
        _uiState.update { 
            val newState = it.update()
            validate(newState)
        }
    }

    private fun validate(state: EditFormsUiState): EditFormsUiState {
        val min = state.minPrice.toLongOrNull() ?: 0L
        val max = state.maxPrice.toLongOrNull() ?: Long.MAX_VALUE
        
        val isPriceError = state.minPrice.isNotEmpty() && state.maxPrice.isNotEmpty() && min > max
        val isNameValid = state.name.isNotBlank()
        val isMembersValid = state.members.isNotEmpty()

        return state.copy(
            isPriceError = isPriceError,
            isValid = isNameValid && isMembersValid && !isPriceError
        )
    }

    private fun saveGroup(onFinish: () -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (!currentState.isValid) return@launch
            
            _uiState.value = currentState.copy(isLoading = true, error = null)
            
            val photoBase64 = currentState.imageUri?.toBase64(application)

            val group = GroupModel(
                name = currentState.name,
                description = currentState.description,
                members = currentState.members,
                minPrice = currentState.minPrice.toDoubleOrNull(),
                maxPrice = currentState.maxPrice.toDoubleOrNull(),
                date = currentState.date.ifBlank { null },
                photo = photoBase64
            )
            
            groupEditUseCase(group).onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onFinish()
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    suspend fun Uri.toBase64(context: Context, maxSize: Int = 512, quality: Int = 80): String? =
        withContext(Dispatchers.IO) {
            return@withContext runCatching {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, this@toBase64)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    context.contentResolver.openInputStream(this@toBase64)?.use {
                        BitmapFactory.decodeStream(it)
                    }
                } ?: return@withContext null

                val ratio = minOf(
                    maxSize.toFloat() / bitmap.width,
                    maxSize.toFloat() / bitmap.height
                ).coerceAtMost(1.0f)

                val width = (bitmap.width * ratio).toInt()
                val height = (bitmap.height * ratio).toInt()

                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

                val outputStream = ByteArrayOutputStream()
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                val byteArray = outputStream.toByteArray()

                Base64.encodeToString(byteArray, Base64.NO_WRAP)
            }.getOrThrow()
    }
}
