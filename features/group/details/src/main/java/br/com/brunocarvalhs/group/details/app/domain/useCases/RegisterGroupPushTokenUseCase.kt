package br.com.brunocarvalhs.group.details.app.domain.useCases

import br.com.brunocarvalhs.core.notifications.domain.PushTokenRepository
import br.com.brunocarvalhs.deviceid.DeviceService
import javax.inject.Inject

internal class RegisterGroupPushTokenUseCase @Inject constructor(
    private val pushTokenRepository: PushTokenRepository,
    private val deviceService: DeviceService,
) {
    suspend operator fun invoke(groupId: String): Result<Unit> {
        val deviceId = deviceService.getDeviceId()
        return pushTokenRepository.registerToken(groupId = groupId, deviceId = deviceId)
    }
}
