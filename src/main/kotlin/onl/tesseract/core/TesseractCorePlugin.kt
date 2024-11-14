package onl.tesseract.core

import onl.tesseract.core.achievement.AchievementService
import onl.tesseract.core.afk.AfkManager
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.command.BoutiqueCommand
import onl.tesseract.core.command.CosmeticCommand
import onl.tesseract.core.command.FamilierCommand
import onl.tesseract.core.command.VoteCommand
import onl.tesseract.core.cosmetics.TrailsAndFilterEventHandlers
import onl.tesseract.core.persistence.hibernate.achievement.AchievementHibernateRepository
import onl.tesseract.core.persistence.hibernate.boutique.BoutiqueHibernateRepository
import onl.tesseract.core.persistence.hibernate.title.TitleHibernateRepository
import onl.tesseract.core.placeholder.TesseractPlaceHolder
import onl.tesseract.core.title.StaffTitle
import onl.tesseract.core.title.TitleService
import onl.tesseract.lib.service.ServiceContainer
import org.bukkit.plugin.java.JavaPlugin

class TesseractCorePlugin : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        instance = this

        ServiceContainer.getInstance().registerService(
            AchievementService::class.java, AchievementService(AchievementHibernateRepository)
        )
        val titleService = ServiceContainer.getInstance().registerService(
            TitleService::class.java, TitleService(TitleHibernateRepository)
        )
        StaffTitle.entries.forEach { titleService.save(it.title) }
        ServiceContainer.getInstance().registerService(
            BoutiqueService::class.java, BoutiqueService(
                BoutiqueHibernateRepository
            )
        )

        this.server.pluginManager.registerEvents(AfkManager.getINSTANCE(), this)
        this.server.pluginManager.registerEvents(TrailsAndFilterEventHandlers(), this)

        TesseractPlaceHolder(this).register()
        registerCommands()
    }

    fun registerCommands() {
        instance.getCommand("boutique")!!.setExecutor(BoutiqueCommand())
        instance.getCommand("familier")!!.setExecutor(FamilierCommand())
        instance.getCommand("cosmetic")!!.setExecutor(CosmeticCommand())
        instance.getCommand("vote")!!.setExecutor(VoteCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        lateinit var instance: TesseractCorePlugin
    }
}

internal val log = TesseractCorePlugin.instance.logger
