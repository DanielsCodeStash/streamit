
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider
import org.http4k.client.ApacheClient
import org.http4k.client.JavaHttpClient
import org.http4k.core.*
import org.http4k.filter.ClientFilters
import org.http4k.filter.DebuggingFilters
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.http4k.filter.RequestFilters
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest

private val json = Json { ignoreUnknownKeys = true }

fun main(args: Array<String>) {

    var client = JavaHttpClient()

    client = DebuggingFilters.PrintRequest().then(client)
    client = DebuggingFilters.PrintResponse().then(client)
    val basicAuthClient = ClientFilters.BasicAuth("zOs9TUgOtaL5cKzlgdJYAQ", "6Z4D66XymXEOjYc2lwqdmBrfq6uUgg")
        .then(client)

    val request = Request(Method.POST, "https://www.reddit.com/api/v1/access_token")
        .body(Body("grant_type=client_credentials"))

    val response: Response = basicAuthClient(request)
    val token: Token = json.decodeFromString(response.body.toString())

    println(token)
    println(response.header("x-ratelimit-remaining"))

    val request2 = Request(Method.GET, "https://oauth.reddit.com/r/DotA2/comments/")
    val r3 = request2.replaceHeader("Authorization", "bearer ${token.accessToken}")

    client(r3)
}

fun mainLoop() {


    /**
     * Final
     *  for each subreddit
     *      until 1. hit known comment or 2. out of api calls 3. or at sufficient depth
     *        get more comments
     *
     *
     * First
     *  Get two pages of comments
     *
     *
     *
     */






}




@Serializable
data class Token(

    @SerialName("access_token")
    val accessToken: String,

    @SerialName("expires_in")
    val expiresIn: Int
)
