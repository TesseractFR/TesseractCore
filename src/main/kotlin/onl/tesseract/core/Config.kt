package onl.tesseract.core

import onl.tesseract.lib.exception.ConfigurationException
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config private constructor(
    var firstSpawnLocation: Location,
    var dbHost: String,
    var dbDatabase: String,
    var dbUsername: String,
    var dbPassword: String,
    var dbPort: Int,
) {
    companion object {
        private lateinit var instance: Config

        operator fun invoke(): Config {
            if (this::instance.isInitialized)
                return instance
            instance = load()
            return instance
        }

        fun load(): Config {
            return load(YamlConfiguration.loadConfiguration(File("plugins/Tesseract/config.yml")))
        }

        fun load(yaml: ConfigurationSection): Config {
            return Config(
                dbHost = yaml.getString("db_host") ?: throw ConfigurationException("Missing config db_host"),
                dbDatabase = yaml.getString("db_database")
                    ?: throw ConfigurationException("Missing config db_database"),
                dbUsername = yaml.getString("db_username")
                    ?: throw ConfigurationException("Missing config db_username"),
                dbPassword = yaml.getString("db_password")
                    ?: throw ConfigurationException("Missing config db_password"),
                dbPort = yaml.getInt("db_port"),
                firstSpawnLocation = yaml.getLocation("firstSpawnLocation")
                    ?: throw ConfigurationException("Missing config firstSpawnLocation"),
            )
        }
    }
}
