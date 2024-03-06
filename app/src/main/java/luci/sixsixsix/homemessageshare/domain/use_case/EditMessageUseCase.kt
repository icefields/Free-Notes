package luci.sixsixsix.homemessageshare.domain.use_case

import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import javax.inject.Inject

class EditMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(username: String, editedMessage: Message) =
        messagesRepository.editMessage(username, editedMessage)
}
