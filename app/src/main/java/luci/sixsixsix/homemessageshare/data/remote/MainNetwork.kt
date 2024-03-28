package luci.sixsixsix.homemessageshare.data.remote

import luci.sixsixsix.homemessageshare.data.remote.dto.MessagesDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Main network interface
 */
interface MainNetwork {
    @FormUrlEncoded
    @POST("api.php?action=new")
    suspend fun writeMessage(
        @Field("id") id: String? = null,
        @Field("username") username: String,
        @Field("title") title: String,
        @Field("date_created") dateCreated: String,
        @Field("message") message: String,
        @Field("tags") tags: List<String> = listOf()
    ): MessagesDto

    @FormUrlEncoded
    @POST("api.php?action=edit")
    suspend fun editMessage(
        @Field("id") id: String,
        @Field("username") username: String,
        @Field("title") title: String,
        @Field("message") message: String,
        @Field("tags") tags: List<String> = listOf()
    ): MessagesDto

    @FormUrlEncoded
    @POST("api.php?action=delete")
    suspend fun deleteMessage(
        @Field("username") username: String,
        @Field("id") id: String
    ): MessagesDto

    @FormUrlEncoded
    @POST("api.php?action=view")
    suspend fun getMessages(
        @Field("username") username: String,
    ): MessagesDto
}
