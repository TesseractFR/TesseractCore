package onl.tesseract.core

import onl.tesseract.core.achievement.AchievementService
import onl.tesseract.core.afk.AfkManager
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.command.BoutiqueCommand
import onl.tesseract.core.command.CosmeticCommand
import onl.tesseract.core.command.FamilierCommand
import onl.tesseract.core.command.MsgCommand
import onl.tesseract.core.command.ReplyToMsg
import onl.tesseract.core.command.VoteCommand
import onl.tesseract.core.command.staff.CosmeticCompleter
import onl.tesseract.core.command.staff.MarketCurrencyCommand
import onl.tesseract.core.command.staff.SocialSpy
import onl.tesseract.core.command.staff.VoteGoalCommand
import onl.tesseract.core.command.staff.VoteTopRewardCommand
import onl.tesseract.core.cosmetics.TrailsAndFilterEventHandlers
import onl.tesseract.core.cosmetics.familier.PetManager
import onl.tesseract.core.dailyConnection.DailyConnectionService
import onl.tesseract.core.event.ColoredChat
import onl.tesseract.core.event.EntityBossBar
import onl.tesseract.core.event.PlayerSit
import onl.tesseract.core.persistence.hibernate.BDDManager
import onl.tesseract.core.persistence.hibernate.achievement.AchievementHibernateRepository
import onl.tesseract.core.persistence.hibernate.boutique.BoutiqueHibernateRepository
import onl.tesseract.core.persistence.hibernate.dailyConnection.DailyConnectionHibernateRepository
import onl.tesseract.core.persistence.hibernate.title.TitleHibernateRepository
import onl.tesseract.core.placeholder.TesseractPlaceHolder
import onl.tesseract.core.title.StaffTitle
import onl.tesseract.core.title.TitleService
import onl.tesseract.core.vote.VoteManager
import onl.tesseract.core.vote.goal.VoteGoalManager
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.task.TaskScheduler
import org.bukkit.plugin.java.JavaPlugin

class TesseractCorePlugin : JavaPlugin() {

    lateinit var bddManager: BDDManager
        private set

    override fun onEnable() {
        // Plugin startup logic
        instance = this
        val config = Config()
        bddManager = BDDManager(config.dbHost, config.dbPort, config.dbUsername, config.dbPassword, config.dbDatabase)

        ServiceContainer.getInstance().registerService(TaskScheduler::class.java, TaskScheduler(this))
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
        val dailyConnectionService = DailyConnectionService(config.serverName, DailyConnectionHibernateRepository)
        ServiceContainer.getInstance().registerService(DailyConnectionService::class.java, dailyConnectionService)

        this.server.pluginManager.registerEvents(AfkManager.getINSTANCE(), this)
        this.server.pluginManager.registerEvents(TrailsAndFilterEventHandlers(), this)
        this.server.pluginManager.registerEvents(PetManager(), this)
        this.server.pluginManager.registerEvents(EntityBossBar(), this)
        this.server.pluginManager.registerEvents(PlayerSit(), this)
        this.server.pluginManager.registerEvents(ColoredChat(), this)
        this.server.pluginManager.registerEvents(dailyConnectionService, this)

        TesseractPlaceHolder(this).register()
        registerCommands()

        VoteManager.getInstance().init()
        VoteGoalManager.startLoops()
    }

    fun registerCommands() {
        instance.getCommand("boutique")!!.setExecutor(BoutiqueCommand())
        instance.getCommand("familier")!!.setExecutor(FamilierCommand())
        instance.getCommand("cosmetic")!!.setExecutor(CosmeticCommand())
        instance.getCommand("vote")!!.setExecutor(VoteCommand())
        instance.getCommand("marketCurrency")!!.setExecutor(MarketCurrencyCommand())
        instance.getCommand("votetopreward")!!.setExecutor(VoteTopRewardCommand())
        instance.getCommand("cosmetic")!!.setTabCompleter(CosmeticCompleter())
        instance.getCommand("votegoal")!!.setExecutor(VoteGoalCommand())
        instance.getCommand("votegoal")!!.setTabCompleter(VoteGoalCommand())
        instance.getCommand("socialspy")!!.setExecutor(SocialSpy())
        instance.getCommand("msg")!!.setExecutor(MsgCommand())
        instance.getCommand("reply")!!.setExecutor(ReplyToMsg())
    }

    override fun onDisable() {
        // Plugin shutdown logic
        bddManager.close()
    }

    companion object {
        lateinit var instance: TesseractCorePlugin
    }
}

internal val log = TesseractCorePlugin.instance.logger
