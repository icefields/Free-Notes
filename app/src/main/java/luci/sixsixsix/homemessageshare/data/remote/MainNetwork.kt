package luci.sixsixsix.homemessageshare.data.remote

import luci.sixsixsix.homemessageshare.data.remote.dto.MessagesDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Main network interface
 */
interface MainNetwork {
    @GET("message.php")
    suspend fun writeMessage(
        @Query("title") title: String,
        @Query("message") message: String
    ): MessagesDto

    @GET("message.php?action=delete")
    suspend fun deleteMessage(
        @Query("id") id: String
    ): MessagesDto

    @GET("log.json")
    suspend fun getMessages(): MessagesDto
}
