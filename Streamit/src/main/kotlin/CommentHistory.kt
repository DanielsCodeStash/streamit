class CommentHistory {

    private val subredditToLastReadComment = mutableMapOf<String, String>()

    fun registerLastComment(subreddit: String, commentId: String) {
        subredditToLastReadComment[subreddit] = commentId
    }

    fun getLastComment(subreddit: String) = subredditToLastReadComment[subreddit] ?: ""
}