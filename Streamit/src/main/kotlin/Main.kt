
const val maxComments = 100

fun main() {

    val initialBearer = "-ZMn0tc5LXYAUPNa6RlVU3fLH9XOHyA"
    val elasticUrl = "http://172.29.194.196:9200/streamit/_doc/"

    val commentStreamer = CommentStreamer(initialBearer, elasticUrl)

    commentStreamer.start()

}







