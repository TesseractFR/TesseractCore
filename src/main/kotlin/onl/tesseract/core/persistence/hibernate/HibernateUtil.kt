package onl.tesseract.core.persistence.hibernate

import onl.tesseract.core.Config
import onl.tesseract.core.achievement.Achievement
import onl.tesseract.core.persistence.hibernate.boutique.TPlayerInfo
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.AvailableSettings
import org.hibernate.cfg.Configuration
import org.hibernate.service.ServiceRegistry
import org.hibernate.tool.schema.Action
import java.lang.Exception

object HibernateUtil {
    val sessionFactory: SessionFactory = buildSessionFactory()

    fun buildSessionFactory(): SessionFactory {
        try {
            val configuration = setConfiguration(Config())

            // Enregistrer les classes d'entité
            configuration.addAnnotatedClass(TPlayerInfo::class.java)
            configuration.addAnnotatedClass(Achievement::class.java)
            val serviceRegistry: ServiceRegistry? =
                StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build()
            return configuration.buildSessionFactory(serviceRegistry)
        } catch (ex: Exception) {
            System.err.println("Initial SessionFactory creation failed." + ex)
            throw ExceptionInInitializerError(ex)
        }
    }

    fun setConfiguration(config: Config): Configuration {
        val configuration = Configuration()
        configuration.setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver")
        configuration.setProperty(
            AvailableSettings.JAKARTA_JDBC_URL,
            "jdbc:mysql://" + config.dbHost + ":" + config.dbPort + "/" + config.dbDatabase
        )
        configuration.setProperty(AvailableSettings.JAKARTA_JDBC_USER, config.dbUsername)
        configuration.setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, config.dbPassword)

        configuration.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect")

        // Enable Hibernate's automatic session context management
        configuration.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread")

        // Echo all executed SQL to stdout
        configuration.setProperty(AvailableSettings.SHOW_SQL, "false")
        configuration.setProperty(AvailableSettings.FORMAT_SQL, "true")

        // Drop and re-create the database schema on startup
        configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, Action.ACTION_VALIDATE)
        return configuration
    }
}
