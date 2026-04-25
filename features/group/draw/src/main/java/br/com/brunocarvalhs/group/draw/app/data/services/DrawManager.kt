package br.com.brunocarvalhs.group.draw.app.data.services

import br.com.brunocarvalhs.friendssecrets.core.security.domain.CryptoService
import br.com.brunocarvalhs.group.draw.app.domain.services.DrawService
import javax.inject.Inject

internal class DrawManager @Inject constructor(
    private val crypto: CryptoService
) : DrawService {

    override fun draw(participants: MutableList<String>): Map<String, String> {
        require(participants.size >= MIN_PARTICIPANTS) {
            "Minimum number of participants is $MIN_PARTICIPANTS"
        }

        var shuffled: List<String>

        do {
            shuffled = participants.shuffled()
        } while (shuffled.zip(participants).any { (sorted, participant) -> sorted == participant })

        val secretSantaMap = HashMap<String, String>()

        participants.forEachIndexed { index, participant ->
            secretSantaMap[participant] = crypto.encrypt(shuffled[index])
        }

        return secretSantaMap
    }

    companion object {
        private const val MIN_PARTICIPANTS = 3
    }
}
