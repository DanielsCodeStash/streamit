package model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

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

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.DOUBLE)
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val value = decoder.decodeDouble()
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(value.toLong()), ZoneId.systemDefault())
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        TODO("Not yet implemented")
    }
}