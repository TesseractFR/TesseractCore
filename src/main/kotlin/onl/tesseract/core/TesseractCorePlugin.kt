package onl.tesseract.core

import onl.tesseract.core.achievement.AchievementService
import onl.tesseract.core.persistence.hibernate.achievement.AchievementHibernateRepository
import onl.tesseract.lib.service.ServiceContainer
import org.bukkit.plugin.java.JavaPlugin

class TesseractCorePlugin : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic

        ServiceContainer.getInstance().registerService(
            AchievementService::class.java, AchievementService(AchievementHibernateRepository)
        )
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
