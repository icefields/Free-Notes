package luci.sixsixsix.homemessageshare.domain.use_case

import kotlinx.coroutines.flow.flow
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import javax.inject.Inject

class WriteNewMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke(title: String, message: String) = flow {
        emit(messagesRepository.submitMessage(title, message))
    }
}
