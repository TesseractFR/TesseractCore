package onl.tesseract.core.persistence.hibernate

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException

class BDDManager(
    host: String,
    port: Int,
    username: String,
    password: String,
    database: String?
) {
    val bddConnection: BDDConnection = BDDConnection(BDD(host, username, password, database, port))

    fun close() {
        try {
            bddConnection.close()
        } catch (throwables: SQLException) {
            log.error("Failed to close database connection", throwables)
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(BDDManager::class.java)
    }
}
