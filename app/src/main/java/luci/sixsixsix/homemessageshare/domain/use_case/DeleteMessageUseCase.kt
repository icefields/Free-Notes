package luci.sixsixsix.homemessageshare.domain.use_case

import kotlinx.coroutines.flow.flow
import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke(id: String) = flow {
        emit(messagesRepository.deleteMessage(id))
    }
}