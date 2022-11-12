import model.Comment

const val maxComments = 100

fun main() {

    val initialBearer = "-ZMn0tc5LXYAUPNa6RlVU3fLH9XOHyA"

    val commentStreamer = CommentStreamer(initialBearer)

    commentStreamer.start()

}







