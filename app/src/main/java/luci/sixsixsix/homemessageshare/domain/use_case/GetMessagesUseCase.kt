package luci.sixsixsix.homemessageshare.domain.use_case

import luci.sixsixsix.homemessageshare.domain.MessagesRepository
import javax.inject.Inject

/**
 * use cases should only have one public function, the function to execute the use case, in this
 * case get the coins. there is only feature that we expose to the view model that the class should
 * contain
 */
class GetMessagesUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    /**
     * with this is possible to call the use case as a function
     * We return a flow because we want to emit multiple values over a period of time, we want to
     * emit Loading, Successful with data or Error.
     */
    suspend operator fun invoke() =
        messagesRepository.getMessages()
}
