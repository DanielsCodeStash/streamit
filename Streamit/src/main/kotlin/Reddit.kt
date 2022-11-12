import kotlinx.serialization.decodeFromString
import model.Comment
import model.TopContainer
import org.http4k.core.Method
import org.http4k.core.Request

class Reddit(initialBearer: String) {

    private val client = RedditClient(initialBearer)

    fun getComments(subreddit: String, maxNumComments: Int = 500, lastSeen: String = ""): List<Comment> {

        var bumpedIntoMaxLimit = false
        var bumpedIntoLastSeen = false
        var after: String? = ""
        val comments = mutableListOf<Comment>()

        while (!bumpedIntoLastSeen && !bumpedIntoMaxLimit && after != null) {

            val request = getRequest(subreddit, after)
            val response = client.sendRequest(request)
            val body = response.body.toString()
            val redditResponse = json.decodeFromString<TopContainer>(body)

            after = redditResponse.data.after
            val pageComments = redditResponse.data.children.map { it.data }

            for(comment in pageComments) {
                if(lastSeen.isNotEmpty() && comment.id == lastSeen) {
                    bumpedIntoLastSeen = true
                    break
                }

                comments.add(comment)

                if(comments.size >= maxNumComments) {
                    bumpedIntoMaxLimit = true
                    System.err.println("Bumped into maxNumComment limit on $subreddit of $maxNumComments")
                    break
                }
            }
        }
        return comments
    }

    private fun getRequest(subreddit: String, after: String): Request {
        val baseUrl = "https://oauth.reddit.com/r/${subreddit}/comments/"

        var request = Request(Method.GET, baseUrl)
            .query("limit", "100")

        if(after.isNotEmpty()) {
            request = request.query("after", after)
        }
        return request
    }



}