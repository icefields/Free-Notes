package luci.sixsixsix.homemessageshare.domain.use_case

import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import javax.inject.Inject

class WriteNewMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(username: String, noteStr: String, title: String, tags: List<String>) =
        messagesRepository.submitMessage(
            username = username,
            noteStr = noteStr,
            title = title,
            tags = tags
        )
}
