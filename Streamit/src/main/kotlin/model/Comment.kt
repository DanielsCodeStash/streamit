package model

import LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class TopContainer(val data: TopContainerData)

@Serializable
data class TopContainerData(val after: String?, val children: List<CommentContainer>)

@Serializable
data class CommentContainer(val data: Comment)

@Serializable
data class Comment(
    val id: String,
    val ups: Int,
    val downs: Int,
    val score: Int,
    val author: String,

    val subreddit: String,

    @SerialName("link_title")
    val linkTitle: String,

    val body: String,

    @SerialName("created_utc")
    @Serializable(with = LocalDateTimeSerializer::class)
    val created: LocalDateTime,
)

