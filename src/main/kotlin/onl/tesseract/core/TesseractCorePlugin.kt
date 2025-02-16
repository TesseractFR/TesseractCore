package onl.tesseract.core

import onl.tesseract.core.achievement.AchievementService
import onl.tesseract.core.afk.AfkManager
import onl.tesseract.core.autochatmessage.AutoChatMessage
import onl.tesseract.core.autochatmessage.AutoChatMessages
import onl.tesseract.core.boutique.BoutiqueService
import onl.tesseract.core.command.*
import onl.tesseract.core.command.staff.*
import onl.tesseract.core.cosmetics.TrailsAndFilterEventHandlers
import onl.tesseract.core.cosmetics.familier.PetManager
import onl.tesseract.core.dailyConnection.DailyConnectionService
import onl.tesseract.core.event.ColoredChat
import onl.tesseract.core.event.EntityBossBar
import onl.tesseract.core.event.PlayerSit
import onl.tesseract.core.persistence.hibernate.BDDManager
import onl.tesseract.core.persistence.hibernate.achievement.AchievementHibernateRepository
import onl.tesseract.core.persistence.hibernate.boutique.BoutiqueHibernateRepository
import onl.tesseract.core.persistence.hibernate.boutique.TPlayerInfoService
import onl.tesseract.core.persistence.hibernate.dailyConnection.DailyConnectionHibernateRepository
import onl.tesseract.core.persistence.hibernate.title.TitleHibernateRepository
import onl.tesseract.core.persistence.hibernate.vote.PlayerVoteHibernateRepository
import onl.tesseract.core.persistence.hibernate.vote.PlayerVotePointsHibernateRepository
import onl.tesseract.core.persistence.hibernate.vote.VoteSiteHibernateRepository
import onl.tesseract.core.persistence.hibernate.vote.goal.VoteGoalHibernateRepository
import onl.tesseract.core.placeholder.TesseractPlaceHolder
import onl.tesseract.core.title.StaffTitle
import onl.tesseract.core.title.TitleService
import onl.tesseract.core.vote.VoteGoalService
import onl.tesseract.core.vote.VoteService
import onl.tesseract.core.vote.goal.VoteGoalManager
import onl.tesseract.lib.TesseractLib
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

        TesseractLib.registerOnEnable(this)
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
        val voteService = ServiceContainer.getInstance().registerService(
            VoteService::class.java, VoteService(
                VoteSiteHibernateRepository,
                PlayerVoteHibernateRepository,
                PlayerVotePointsHibernateRepository))
        ServiceContainer.getInstance().registerService(
            VoteGoalService::class.java, VoteGoalService(voteService, VoteGoalHibernateRepository))
        ServiceContainer.getInstance().registerService(TPlayerInfoService::class.java, TPlayerInfoService())
        val afkManager = ServiceContainer.getInstance().registerService(AfkManager::class.java, AfkManager())
        afkManager.startTask()
        this.server.pluginManager.registerEvents(afkManager, this)
        this.server.pluginManager.registerEvents(TrailsAndFilterEventHandlers(), this)
        this.server.pluginManager.registerEvents(PetManager(), this)
        this.server.pluginManager.registerEvents(EntityBossBar(), this)
        this.server.pluginManager.registerEvents(PlayerSit(), this)
        this.server.pluginManager.registerEvents(ColoredChat(), this)
        this.server.pluginManager.registerEvents(dailyConnectionService, this)

        TesseractPlaceHolder().register()
        registerCommands()

        VoteGoalManager.startLoops()
        val autoChatMessage = AutoChatMessage()
        autoChatMessage.start()
        autoChatMessage.addMessage(AutoChatMessages.voteMessage())
        autoChatMessage.addMessage(AutoChatMessages.discordMessage())
        autoChatMessage.addMessage(AutoChatMessages.recrutementMessage())
    }

    fun registerCommands() {
        instance.getCommand("boutique")!!.setExecutor(BoutiqueCommand())
        instance.getCommand("familier")!!.setExecutor(FamilierCommand())
        instance.getCommand("ptime")!!
                .setExecutor(PTimeCommand())
        instance.getCommand("cosmetic")!!.setExecutor(CosmeticCommand())
        instance.getCommand("vote")!!.setExecutor(VoteCommand())
        MarketCurrencyCommand().register(this, "marketCurrency")
        instance.getCommand("votetopreward")!!.setExecutor(VoteTopRewardCommand())
        instance.getCommand("cosmetic")!!.tabCompleter = CosmeticCompleter()
        instance.getCommand("votegoal")!!.setExecutor(VoteGoalCommand())
        instance.getCommand("votegoal")!!.tabCompleter = VoteGoalCommand()
        instance.getCommand("socialspy")!!.setExecutor(SocialSpy())
        instance.getCommand("msg")!!.setExecutor(MsgCommand())
        instance.getCommand("reply")!!.setExecutor(ReplyToMsg())
    }

    override fun onDisable() {
        // Plugin shutdown logic
        if (this::bddManager.isInitialized)
            bddManager.close()
    }

    companion object {
        lateinit var instance: TesseractCorePlugin
    }
}

internal val log = TesseractCorePlugin.instance.logger
