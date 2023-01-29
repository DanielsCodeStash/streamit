import model.Comment
import java.time.LocalDateTime
import java.time.ZoneOffset

class StreamStatistics {

    private var totalNumComments = 0
    private val subredditStats = mutableMapOf<String, SubredditStats>()

    fun registerComment(subreddit: String, comment: Comment) {

        val subStats = subredditStats[subreddit] ?: getNewSubredditStats(subreddit, comment)

        totalNumComments++
        subStats.numComments++

        val currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val startTime = subStats.firstCommentTime.toEpochSecond(ZoneOffset.UTC)
        val secondsBetween = currentTime - startTime

        subStats.commentsPerSec = subStats.numComments.toDouble() / secondsBetween.toDouble()
    }

    private fun getNewSubredditStats(subreddit: String, comment: Comment): SubredditStats {
        val stats = SubredditStats(0, 0.0, comment.created)
        subredditStats[subreddit] = stats
        return stats
    }

    fun getStats(subreddit: String): String {
        val stats = subredditStats[subreddit]!!
        val commentsPerSec = String.format("%.3f", stats.commentsPerSec).replace(",", ".")
        return "$totalNumComments total comments, ${stats.numComments} from subreddit, $commentsPerSec comments per sec avg"
    }


}

data class SubredditStats(
    var numComments: Int = 0,
    var commentsPerSec: Double = 0.0,
    val firstCommentTime: LocalDateTime
)