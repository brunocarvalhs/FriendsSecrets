package br.com.brunocarvalhs.group.commons.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunocarvalhs.group.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawTypeDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit
) {
    val types = hashMapOf(
        "Amigo secreto" to "Cada participante sorteia outro, sem revelar quem é, até a revelação.",
        "Amigo chocolate" to "Todos trocam chocolates como presentes. Simples e doce.",
        "Anjo oculto" to "Participantes mandam mensagens ou mimos antes da revelação.",
        "Inverso" to "O sorteado tenta adivinhar quem o tirou com pistas recebidas.",
        "Manual" to "Você configura quem tira quem. Total controle, sem sorteio."
    )

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isFocused by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selectedType,
            onValueChange = {},
            label = { Text(stringResource(R.string.tipo_de_sorteio)) },
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { showSheet = true },
                ) { Icon(Icons.Filled.Category, contentDescription = stringResource(R.string.selecionar_tipo_de_sorteio)) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && !showSheet) {
                        showSheet = true
                    }
                    isFocused = focusState.isFocused
                },
            singleLine = true
        )
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.escolha_o_tipo_de_amigo_secreto),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                types.forEach { (type, description) ->
                    Card(
                        onClick = {
                            onTypeSelected(type)
                            showSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(type, style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(description, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { showSheet = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        }
    }
}
