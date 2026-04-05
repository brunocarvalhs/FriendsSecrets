package br.com.brunocarvalhs.group.create.app.presentation.forms

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import br.com.brunocarvalhs.group.create.app.domain.entities.GroupModel
import br.com.brunocarvalhs.group.create.app.domain.useCases.GroupCreateUseCase
import br.com.brunocarvalhs.group.create.commons.navigation.FormsRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@Stable
@HiltViewModel
class FormsViewModel @Inject constructor(
    private val application: Application,
    savedStateHandle: SavedStateHandle,
    private val groupCreateUseCase: GroupCreateUseCase
) : ViewModel() {

    private val args = savedStateHandle.toRoute<FormsRouter>(FormsRouter.typeMap)
    private val _uiState = MutableStateFlow(
        FormsUiState(
            members = args.members,
            contacts = args.contacts
        )
    )
    val uiState: StateFlow<FormsUiState> = _uiState.asStateFlow()

    fun handleIntent(intent: FormsIntent) = when (intent) {
        is FormsIntent.CreateGroup -> createGroup(intent.onFinish)
        is FormsIntent.UpdateName -> updateState { copy(name = intent.name) }
        is FormsIntent.UpdateDescription -> updateState { copy(description = intent.description) }
        is FormsIntent.UpdateDate -> updateState { copy(date = intent.date) }
        is FormsIntent.UpdateMinPrice -> updateState { copy(minPrice = intent.minPrice) }
        is FormsIntent.UpdateMaxPrice -> updateState { copy(maxPrice = intent.maxPrice) }
        is FormsIntent.UpdateImage -> updateState { copy(imageUri = intent.uri) }
    }

    private fun updateState(update: FormsUiState.() -> FormsUiState) {
        _uiState.update { 
            val newState = it.update()
            validate(newState)
        }
    }

    private fun validate(state: FormsUiState): FormsUiState {
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

    private fun createGroup(onFinish: (String) -> Unit) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (!currentState.isValid) return@launch
            
            _uiState.value = currentState.copy(isLoading = true, error = null)
            
            val photoBase64 = currentState.imageUri?.toBase64(application)

            val group = GroupModel(
                id = currentState.id,
                name = currentState.name,
                description = currentState.description,
                members = currentState.members,
                token = currentState.token,
                minPrice = currentState.minPrice.toIntOrNull(),
                maxPrice = currentState.maxPrice.toIntOrNull(),
                date = currentState.date.ifBlank { null },
                photoBase64 = photoBase64
            )
            
            groupCreateUseCase(group).onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
                onFinish(group.token)
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }
        }
    }

    fun Uri.toBase64(context: Context, maxSize: Int = 512, quality: Int = 80): String? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, this)
                ImageDecoder.decodeBitmap(source)
            } else {
                context.contentResolver.openInputStream(this)?.use {
                    BitmapFactory.decodeStream(it)
                }
            } ?: return null

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
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun Uri.toDecodeBase64(): ByteArray? {
        return try {
            Base64.decode(this.toString(), Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    fun Uri.toBitmap(context: Context): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, this)
                ImageDecoder.decodeBitmap(source)
            } else {
                context.contentResolver.openInputStream(this)?.use {
                    BitmapFactory.decodeStream(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
