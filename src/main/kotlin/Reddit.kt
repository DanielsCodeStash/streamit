import kotlinx.serialization.decodeFromString
import model.Comment
import model.TopContainer
import org.http4k.core.Method
import org.http4k.core.Request

class Reddit(config: Config) {

    private val client = RedditClient(config)

    fun getComments(subreddit: String, maxNumComments: Int, commentHistory: CommentHistory): List<Comment> {

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

            for (comment in pageComments) {
                if (commentHistory.commentExistsInRecentHistory(subreddit, comment)) {
                    bumpedIntoLastSeen = true
                    break
                }

                comments.add(comment)
                commentHistory.registerMostRecentComment(subreddit, comment)

                if (comments.size >= maxNumComments) {
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

        if (after.isNotEmpty()) {
            request = request.query("after", after)
        }
        return request
    }

}