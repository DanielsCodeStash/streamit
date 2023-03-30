import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream

class Config {

    val clientString: String
    val clientSecret: String
    val initialBearer: String
    val subreddits: List<String>
    val elasticUrl: String
    val elasticUser: String
    val elasticPassword: String
    val maxCommentsPerRead: Int

    init {

        val secret = readResourceYml("secret.yml")
        initialBearer = secret["initialBearer"] as String
        clientString = secret["clientString"] as String
        clientSecret = secret["clientString"] as String
        elasticUser = secret["elasticUser"] as String
        elasticPassword = secret["elasticPassword"] as String

        val config = readResourceYml("config.yml")
        subreddits = config["subreddits"] as List<String>
        elasticUrl = config["elasticUrl"] as String
        maxCommentsPerRead = config["maxCommentsPerRead"] as Int
    }

    private fun readResourceYml(fileName: String): Map<String, Any> {
        val yaml = Yaml()
        val path = this::class.java.classLoader.getResource(fileName)
        return yaml.load(FileInputStream(File(path.toURI())))
    }
}