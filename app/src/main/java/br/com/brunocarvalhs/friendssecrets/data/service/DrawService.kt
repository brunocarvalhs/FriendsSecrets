package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.data.exceptions.MinimumsMembersOfDrawException
import br.com.brunocarvalhs.friendssecrets.domain.entities.GroupEntities

class DrawService {
    
    fun drawMembers(group: GroupEntities) : Map<String, String> {
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
            secretSantaMap[participant] = shuffled[index]
        }

        return secretSantaMap
    }
}