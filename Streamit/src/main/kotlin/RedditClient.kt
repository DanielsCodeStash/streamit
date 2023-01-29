import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.then
import org.http4k.filter.RequestFilters
import kotlin.system.exitProcess

class RedditClient(initialBearer: String) {
    private val tokenManager = TokenManager(initialBearer)
    private var client = tokenManager.getInitialClient()

    fun sendRequest(request: Request): Response {

        client = tokenManager.preRequestTokenCheck(client)

        var response = client(request)

        if (!tokenManager.authWasSuccessful(response)) {
            client = tokenManager.getRefreshedToken()
            response = client(request)
            if (!tokenManager.authWasSuccessful(response)) {
                System.err.println("Failed to auth")
                exitProcess(-1)
            }
        }

        rateLimitCheck(response)

        return response
    }

}

fun getBasicClient(): HttpHandler {
    var client = JavaHttpClient()

//    client = DebuggingFilters.PrintRequest().then(client)
//    client = DebuggingFilters.PrintResponse().then(client)
    client = RequestFilters.SetHeader("User-Agent", "windows:streamit:v0.1 (by /u/landfisk)").then(client)

    return client
}

private fun rateLimitCheck(response: Response) {
    val remaining = response.header("x-ratelimit-remaining")!!.toInt()

    if (remaining < 50) {
        System.err.println("Warning: only $remaining api requests remaining")
    }

    if (remaining == 0) {

        System.err.println("OUT OF REQUESTS")
        exitProcess(1)

    } else if (remaining < 5) {

        val timeToReset = response.header("X-Ratelimit-Reset")!!.toInt()
        System.err.println("Only $remaining requests left, sleeping $timeToReset s until estimated reset ")
        Thread.sleep((timeToReset * 1000).toLong())
    }
}
