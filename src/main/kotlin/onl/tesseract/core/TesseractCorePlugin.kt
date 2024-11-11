package onl.tesseract.core

import onl.tesseract.core.achievement.AchievementService
import onl.tesseract.core.persistence.hibernate.achievement.AchievementHibernateRepository
import onl.tesseract.core.persistence.hibernate.title.TitleHibernateRepository
import onl.tesseract.core.title.StaffTitle
import onl.tesseract.core.title.TitleService
import onl.tesseract.lib.service.ServiceContainer
import org.bukkit.plugin.java.JavaPlugin

class TesseractCorePlugin : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic

        ServiceContainer.getInstance().registerService(
            AchievementService::class.java, AchievementService(AchievementHibernateRepository)
        )
        val titleService = ServiceContainer.getInstance().registerService(
            TitleService::class.java, TitleService(TitleHibernateRepository)
        )

        StaffTitle.entries.forEach { titleService.save(it.title) }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
