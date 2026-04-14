package br.com.brunocarvalhs.group.list.app.domain.services

import javax.inject.Inject

internal class GroupDrawService @Inject constructor() {

    fun draw(participants: List<String>): Map<String, String> {
        if (participants.size < 3) throw IllegalArgumentException("Mínimo de 3 participantes para o sorteio.")
        
        return performDraw(participants)
    }

    private fun performDraw(participants: List<String>): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val available = participants.toMutableList()
        
        participants.forEach { participant ->
            var secretFriend: String
            do {
                // Se sobrar apenas o próprio participante, reinicia o sorteio (Deadlock)
                if (available.size == 1 && available[0] == participant) {
                    return performDraw(participants)
                }
                secretFriend = available.random()
            } while (secretFriend == participant)
            
            result[participant] = secretFriend
            available.remove(secretFriend)
        }
        return result
    }
}
