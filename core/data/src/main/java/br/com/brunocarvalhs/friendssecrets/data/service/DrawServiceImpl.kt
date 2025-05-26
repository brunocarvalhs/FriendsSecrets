package br.com.brunocarvalhs.friendssecrets.data.service

import br.com.brunocarvalhs.friendssecrets.domain.exceptions.MinimumsMembersOfDrawException
import br.com.brunocarvalhs.friendssecrets.domain.services.CryptoService
import br.com.brunocarvalhs.friendssecrets.domain.services.DrawService
import javax.inject.Inject

class DrawServiceImpl @Inject constructor(
    private val crypto: CryptoService
) : DrawService {

    override fun drawMembers(participants: MutableList<String>): Map<String, String> {

        if (participants.size < 3) {
            throw MinimumsMembersOfDrawException()
        }

        var shuffled: List<String>
        do {
            shuffled = participants.shuffled()
        } while (shuffled.zip(participants)
                .any { (sorted, participant) -> sorted == participant }
        )

        val secretSantaMap = HashMap<String, String>()

        participants.forEachIndexed { index, participant ->
            secretSantaMap[participant] = crypto.encrypt(shuffled[index])
        }

        return secretSantaMap
    }

    override fun revelation(code: String): String {
        return crypto.decrypt(code)
    }
}

