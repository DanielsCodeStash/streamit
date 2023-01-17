import kotlinx.serialization.encodeToString
import model.Comment
import model.ElasticComment
import org.http4k.client.ApacheClient
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.RequestFilters
import java.time.LocalDateTime

class Elastic(private val elasticUrl: String) {

    private val client = ClientFilters.BasicAuth("elastic", "dJ1usBsu_mq5C=s_GaUj")
        .then(RequestFilters.SetHeader("Content-Type", "application/json"))
        .then(ApacheClient())

    fun sendToElastic(comment: Comment) {

        val json = json.encodeToString(ElasticComment(comment))

        val request = org.http4k.core.Request(Method.POST, elasticUrl)
            .body(Body(json))

        val start = System.currentTimeMillis()
        val response = client(request)
        val sendTime = System.currentTimeMillis() - start
        println("Es send time: $sendTime ms")

        if(response.status != Status.CREATED) {
            System.err.println("Elastic error: $response")
        }
    }
}