package luci.sixsixsix.homemessageshare.data.remote

import luci.sixsixsix.homemessageshare.data.remote.dto.MessagesDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Main network interface
 */
interface MainNetwork {
    @GET("api.php?action=new")
    suspend fun writeMessage(
        @Query("username") username: String,
        @Query("title") title: String,
        @Query("message") message: String,
        @Query("tags") tags: List<String> = listOf()
    ): MessagesDto

    @GET("api.php?action=edit")
    suspend fun editMessage(
        @Query("id") id: String,
        @Query("username") username: String,
        @Query("title") title: String,
        @Query("message") message: String,
        @Query("tags") tags: List<String> = listOf()
    ): MessagesDto

    @GET("api.php?action=delete")
    suspend fun deleteMessage(
        @Query("username") username: String,
        @Query("id") id: String
    ): MessagesDto

    @GET("api.php?action=view")
    suspend fun getMessages(
        @Query("username") username: String,
    ): MessagesDto
}
