import model.Comment

class CommentStreamer(initialBearer: String) {

    private val reddit = Reddit(initialBearer)
    private val history = CommentHistory()
    private val statistics = StreamStatistics()

    private val subreddits = listOf(
        "AskReddit",
        "DotA2",
        "politics",
        "sweden",
        "AmItheAsshole",
        "teenagers",
        "wallstreetbets",
        "memes",
        "CryptoCurrency")

    fun start() {

        while (true) {
            subreddits.forEach { subreddit ->
                processNewComments(subreddit)
                Thread.sleep(1000)
            }
        }
    }

    private fun processNewComments(subreddit: String) {

        val comments = reddit.getComments(subreddit, maxComments, history.getLastComment(subreddit)).reversed()

        comments.forEach { comment ->
            statistics.registerComment(subreddit, comment)
            printComment(comment, subreddit)
        }

        if (comments.isNotEmpty()) history.registerLastComment(subreddit, comments.last().id)
    }

    private fun printComment(comment: Comment, subreddit: String) {
        println("\n----- ${comment.author} at ${comment.created.toLocalTime()} - ${statistics.getStats(subreddit)} -----")
        println("${comment.subreddit} | ${comment.linkTitle}")
        println(comment.body.replace("\n\n", "\n"))
    }

}