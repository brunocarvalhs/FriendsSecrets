package br.com.brunocarvalhs.group.create.app.data.model

import br.com.brunocarvalhs.core.domain.model.UserModel
import org.junit.Assert.assertEquals
import org.junit.Test

class UserCreateDTOTest {

    @Test
    fun `fromDomain should map Domain model to DTO correctly`() {
        // Given
        val userModel = UserModel(
            id = "1",
            name = "Bruno",
            phoneNumber = "123456789",
            photoUrl = "http://photo.com",
            likes = listOf("Games", "Code")
        )

        // When
        val dto = UserCreateDTO.fromDomain(userModel)

        // Then
        assertEquals(userModel.id, dto.id)
        assertEquals(userModel.name, dto.name)
        assertEquals(userModel.phoneNumber, dto.phoneNumber)
        assertEquals(userModel.photoUrl, dto.photoUrl)
        assertEquals(userModel.likes, dto.likes)
    }
}
