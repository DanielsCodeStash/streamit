const val maxComments =
    100 // max number of comments read from a subreddit at a time (before moving on to next subreddit or waiting)

fun main() {

    val initialBearer = "-fMriHoIZlZNlZ8OZ47xWIKMjTAozBw"
    val elasticUrl = "http://172.29.192.215:9200/streamit/_doc/"

    val commentStreamer = CommentStreamer(initialBearer, elasticUrl)

    commentStreamer.start()

}







