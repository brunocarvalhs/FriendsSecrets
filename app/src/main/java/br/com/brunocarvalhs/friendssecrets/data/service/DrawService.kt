package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.commons.security.CryptoService
import br.com.brunocarvalhs.friendssecrets.data.exceptions.MinimumsMembersOfDrawException
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities
import com.google.firebase.perf.metrics.AddTrace

class DrawService(
    private val cryptoService: CryptoService = CryptoService()
) {

    @AddTrace(name = "DrawService.drawMembers")
    fun drawMembers(group: GroupEntities): Map<String, String> {
        val participants = group.members.keys.toMutableList()

        if (participants.size < 3) {
            throw MinimumsMembersOfDrawException()
        }

        var shuffled: List<String>
        do {
            shuffled = participants.shuffled()
        } while (shuffled.zip(participants)
                .any { (sorteado, participante) -> sorteado == participante }
        )

        val secretSantaMap = HashMap<String, String>()

        participants.forEachIndexed { index, participant ->
            secretSantaMap[participant] = cryptoService.encrypt(shuffled[index])
        }

        return secretSantaMap
    }

    @AddTrace(name = "DrawService.revelation")
    fun revelation(code: String): String {
        return cryptoService.decrypt(code)
    }
}

