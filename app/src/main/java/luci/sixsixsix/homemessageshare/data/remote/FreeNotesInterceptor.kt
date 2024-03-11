/**
 * Copyright (C) 2024  Antonio Tari
 *
 * This file is a part of Libre Notes
 * Android self-hosting, note-taking, client + server application
 * @author Antonio Tari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package luci.sixsixsix.homemessageshare.data.remote

import android.util.Log
import kotlinx.coroutines.runBlocking
import luci.sixsixsix.homemessageshare.common.Constants.errorStr
import luci.sixsixsix.homemessageshare.domain.SettingsRepository
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.URISyntaxException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FreeNotesInterceptor @Inject constructor(
    private val settingsRepository: SettingsRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        var request = chain.request()
        val baseUrl = settingsRepository.getCurrentNotesCollection(true).serverAddress
        if (!baseUrl.isNullOrBlank()) {
            val host = buildServerUrl(baseUrl).toHttpUrlOrNull()
            host?.let { newHost ->
                try {
                    request.url.newBuilder()
                        .scheme(newHost.scheme)
                        .host(newHost.host)
                        .encodedPath("${newHost.encodedPath}${request.url.encodedPath}")
                        .encodedQuery(request.url.encodedQuery)
                        .build()
                } catch (e: URISyntaxException) {
                    e.printStackTrace()
                    null
                }?.let { newUrl ->
                    Log.d("aaaaa", newUrl.toString())
                    request = request.newBuilder()
                        .url(newUrl)
                        .build()
                }
            }

            try {
                chain.proceed(request)
            } catch (e: Exception) {
                Response.Builder()
                    .body("{ \"exception\" : \"${e.localizedMessage}\" }".toResponseBody(null))
                    .protocol(Protocol.HTTP_2)
                    .message("{ \"exception\" : \"${e.localizedMessage}\" }")
                    .request(chain.request())
                    .code(404)
                    .build()
            }
        } else {
            Response.Builder()
                .body("{ \"error\" : \"$errorStr\"}".toResponseBody(null))
                .protocol(Protocol.HTTP_2)
                .message("{ \"error\" : \"$errorStr\"}")
                .request(chain.request())
                .code(200)
                .build()
        }
    }

    fun buildServerUrl(baseUrl: String): String {
        val sb = StringBuilder()
        // check if url starts with http or https, if not start the string builder with that
        if (!baseUrl.startsWith("http://") &&
            !baseUrl.startsWith("https://")) {
            sb.append("http://")
        }
        // add the url
        sb.append(baseUrl)
        // check if url contains server, if not add it
//    if (!baseUrl.contains("/server")) {
//        sb.append("/server")
//    }

        return sb.toString()
    }
}

