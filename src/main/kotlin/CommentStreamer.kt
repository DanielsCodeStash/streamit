import model.Comment

class CommentStreamer(private val config: Config) {

    private val reddit = Reddit(config)
    private val history = CommentHistory(config)
    private val statistics = StreamStatistics()
    private val elastic = Elastic(config)

    fun start() {

        while (true) {
            config.subreddits.forEach { subreddit ->
                processNewComments(subreddit)
                Thread.sleep(1000)
            }
        }
    }

    private fun processNewComments(subreddit: String) {

        val comments = reddit.getComments(subreddit, config.maxCommentsPerRead, history)

        comments.forEach { comment ->
            statistics.registerComment(subreddit, comment)
            //printComment(comment, subreddit)
            elastic.sendToElastic(comment)
        }
    }

    private fun printComment(comment: Comment, subreddit: String) {
        println("\n----- ${comment.author} at ${comment.created.toLocalTime()} - ${statistics.getStats(subreddit)} -----")
        println("${comment.subreddit} | ${comment.linkTitle}")
        println(comment.body.replace("\n\n", "\n"))
    }

}