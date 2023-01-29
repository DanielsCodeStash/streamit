package model

import LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ElasticComment(
    @SerialName("@timestamp")
    @Serializable(with = LocalDateTimeSerializer::class)
    val created: LocalDateTime,

    val author: String,
    val subreddit: String,
    val linkTitle: String,
    val body: String,
) {
    constructor(comment: Comment) : this(
        comment.created,
        comment.author,
        comment.subreddit,
        comment.linkTitle,
        comment.body
    )
}
