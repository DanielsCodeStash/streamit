const val maxComments =
    100 // max number of comments read from a subreddit at a time (before moving on to next subreddit or waiting)

fun main() {

    val initialBearer = "-GyQlnGFHava8meph1YCnos5zigPBzg"
    val elasticUrl = "http://172.19.43.76:9200/streamit/_doc/"

    val commentStreamer = CommentStreamer(initialBearer, elasticUrl)

    commentStreamer.start()

}







