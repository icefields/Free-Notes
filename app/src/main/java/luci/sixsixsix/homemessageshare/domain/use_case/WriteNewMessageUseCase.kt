package luci.sixsixsix.homemessageshare.domain.use_case

import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import luci.sixsixsix.homemessageshare.domain.models.Message
import javax.inject.Inject

class WriteNewMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(message: String, title: String, tags: List<String>) =
        messagesRepository.submitMessage(message = message, title = title, tags = tags)
}
