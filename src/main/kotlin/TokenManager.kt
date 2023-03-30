import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import org.http4k.core.*
import org.http4k.filter.ClientFilters
import org.http4k.filter.RequestFilters
import java.time.LocalDateTime

class TokenManager(private val config: Config) {

    private val authUrl = "https://www.reddit.com/api/v1/access_token"
    private var lastToken: Token? = null

    fun getInitialClient(): HttpHandler {

        return RequestFilters.SetHeader("Authorization", "bearer ${config.initialBearer}")
            .then(getBasicClient())
    }

    fun preRequestTokenCheck(client: HttpHandler): HttpHandler {
        return client
    }

    fun authWasSuccessful(response: Response): Boolean {
        return response.header("x-ratelimit-remaining") != null
    }

    fun getRefreshedToken(): HttpHandler {
        println("Refreshing token")
        lastToken = getToken()
        println("New token: $lastToken")
        return RequestFilters.SetHeader("Authorization", "bearer ${lastToken!!.tokenResponse.accessToken}")
            .then(getBasicClient())
    }

    data class Token(val tokenResponse: TokenResponse, val localDateTime: LocalDateTime = LocalDateTime.now())

    @Serializable
    data class TokenResponse(

        @SerialName("access_token")
        val accessToken: String,

        @SerialName("expires_in")
        val expiresIn: Int
    )

    private fun getToken(): Token {

        val client = ClientFilters.BasicAuth(config.clientString, config.clientSecret)
            .then(getBasicClient())

        val request = Request(Method.POST, authUrl)
            .body(Body("grant_type=client_credentials"))

        val response = client(request)

        val tokenResponse: TokenResponse = json.decodeFromString(response.body.toString())

        return Token(tokenResponse)
    }


}