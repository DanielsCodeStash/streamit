import model.Comment

fun main() {

    val initialBearer = "-ZMn0tc5LXYAUPNa6RlVU3fLH9XOHyA"
    val reddit = Reddit(initialBearer)

    var lastSeenComment = ""
    var commentNum = 0

    while (true) {
        val comments = reddit.getSubredditComments("AskReddit", 200, lastSeenComment)

        if(comments.isNotEmpty()) {
            comments.forEach {
                commentNum++
                printComment(it, commentNum)
            }
            lastSeenComment = comments.first().id
        }

        Thread.sleep(1000)
    }
}

fun printComment(comment: Comment, commentNum: Int) {
    println("\n----- ${comment.author} at ${comment.created.toLocalTime()} num ${commentNum} -----")
    println("----- ${comment.linkTitle}")
    println(comment.body.replace("\n\n", "\n"))
}






