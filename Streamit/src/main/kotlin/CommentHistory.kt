import model.Comment
import java.util.*

class CommentHistory(config: Config) {

    private val queueSizeLimit = config.maxCommentsPerRead * 2
    private val subredditToLastComments = mutableMapOf<String, Queue<String>>()

    fun commentExistsInRecentHistory(subreddit: String, comment: Comment): Boolean {
        return subredditToLastComments[subreddit]?.contains(comment.id) ?: return false
    }

    fun registerMostRecentComment(subreddit: String, comment: Comment) {
        var queue = subredditToLastComments[subreddit]
        if (queue == null) {
            queue = LinkedList()
            subredditToLastComments[subreddit] = queue
        }
        if (queue.size >= queueSizeLimit) {
            queue.remove()
        }
        queue.add(comment.id)
    }
}