package onl.tesseract.core.vote

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import onl.tesseract.core.TesseractCorePlugin
import onl.tesseract.core.log
import onl.tesseract.lib.menu.ItemBuilder
import onl.tesseract.lib.menu.Menu
import onl.tesseract.lib.menu.MenuSize
import onl.tesseract.lib.profile.PlayerProfileService
import onl.tesseract.lib.service.ServiceContainer
import onl.tesseract.lib.task.TaskScheduler
import onl.tesseract.lib.util.ItemLoreBuilder
import onl.tesseract.lib.util.Util
import onl.tesseract.core.vote.goal.VoteGoal
import onl.tesseract.core.vote.goal.VoteGoalManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.scheduler.BukkitTask
import java.time.Duration
import java.util.UUID
import java.util.logging.Level

class VoteMenu(
    val playerID: UUID,
    previous: Menu? = null,
) : Menu(MenuSize.Three, "Votes", NamedTextColor.RED, previous) {

    companion object {
        var rewardMenuClass: Class<out AVoteRewardMenu> = AVoteRewardMenu::class.java
    }

    var buttonsTask: BukkitTask? = null

    override fun placeButtons(viewer: Player) {
        fill(ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").build())
        addCloseButton()
        addCloseButton(18)

        val scheduler = ServiceContainer[TaskScheduler::class.java]
        val voteService = ServiceContainer[VoteService::class.java]
        scheduler.runAsyncTimer(0L, 20L) {
            if (!hasViewer()) {
                it.cancel()
                return@runAsyncTimer
            }
            val remainingDurations: Map<VoteSite, Duration> = voteService.getRemainingTimeUntilVote(playerID)

            putAllSitesButton(remainingDurations, viewer)
        }

        scheduler.runAsyncTimer {
            val remainingDurations: Map<VoteSite, Duration> = voteService.getRemainingTimeUntilVote(playerID)
            var index = 10
            voteService.getVoteSites().forEach { site ->
                putSiteButton(site, remainingDurations[site] ?: Duration.ZERO, viewer, index++)
            }
            for (i in index until 18) {
                addButton(i, ItemBuilder(Material.BARRIER, " ").build())
            }

            putVoteGoalButton()
            putPlayerButton()
            putTopButton()
            putRewardButton()
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        this.buttonsTask?.cancel()
    }

    fun putRewardButton() {
        val keys = ServiceContainer[VoteService::class.java].getPlayerVotePoints(playerID)
        addButton(
            22, ItemBuilder(Material.RAW_GOLD)
                .name("Récompenses", NamedTextColor.GOLD)
                .lore()
                .newline()
                .append("Mes points de vote", NamedTextColor.YELLOW)
                .append(" : ", NamedTextColor.GRAY)
                .append("" + keys, NamedTextColor.GOLD)
                .newline(2)
                .append("Cliquez pour voir les différentes récompenses", NamedTextColor.AQUA, TextDecoration.ITALIC)
                .buildLore()
                .build()
        ) {
            try {
                val constructor = rewardMenuClass.getDeclaredConstructor(UUID::class.java, Menu::class.java)
                val menu: Menu = constructor.newInstance(playerID, this)
                menu.open(viewer ?: return@addButton)
            } catch (e: Exception) {
                log.log(Level.SEVERE, "Failed to instantiate reward menu", e)
            }
        }
    }

    fun putVoteGoalButton() {
        var lore: ItemLoreBuilder = ItemLoreBuilder(45).newline()
            .append("Vote pendant les ", NamedTextColor.YELLOW)
            .append("Vote Goals", NamedTextColor.GOLD)
            .append(" pour obtenir encore plus de récompenses !", NamedTextColor.YELLOW)
            .newline()
            .append(
                "Si tu as voté pendant un vote goal, tu recevras à la fin de celui-ci une récompense différente à chaque goal.",
                NamedTextColor.GRAY
            )
            .newline(2)

        val voteGoalService = ServiceContainer[VoteGoalService::class.java]
        val goals: Collection<VoteGoal> = VoteGoalManager.getGoals()
        if (goals.isEmpty())
            lore = lore.append("Il n'y a pas de Vote Goal en cours pour le moment...", NamedTextColor.GRAY)
        else {
            goals.forEach { goal ->
                val voteCount = voteGoalService.getVoteCount(goal)
                lore = lore.append("→ ", NamedTextColor.RED)
                    .append("" + voteCount, NamedTextColor.YELLOW)
                    .append("/", NamedTextColor.GRAY)
                    .append("" + goal.requiredQuantity(), NamedTextColor.YELLOW)
                    .append(" | ", NamedTextColor.WHITE, TextDecoration.OBFUSCATED)
                    .append("Temps restant : ", NamedTextColor.GRAY)
                    .append(goal.printableRemainingDuration, NamedTextColor.YELLOW)
                    .append(" | ", NamedTextColor.WHITE, TextDecoration.OBFUSCATED)
                    .append(" Récompense : ", NamedTextColor.GRAY)
                    .append(goal.reward().toString(), NamedTextColor.YELLOW)
                    .newline()
            }
        }

        addButton(
            2, ItemBuilder(Material.CLOCK)
                .name("Vote Goals", NamedTextColor.GOLD)
                .lore(lore.get())
                .build()
        )
    }

    fun putTopButton() {
        val lore: ItemLoreBuilder = ItemLoreBuilder()
        val voteService = ServiceContainer[VoteService::class.java]
        val top: List<Pair<UUID, Int>> = voteService.getTop()
        var index = 1
        top.forEach { (playerID: UUID, voteAmount: Int) ->
            val offlinePlayer = Bukkit.getOfflinePlayer(playerID)
            lore.newline()
                .append("${index++}. ", NamedTextColor.RED)
                .append(offlinePlayer.name, NamedTextColor.YELLOW)
                .append(" : ", NamedTextColor.GRAY)
                .append("$voteAmount", NamedTextColor.GOLD)
        }

        lore.newline(2)
            .append("Récompenses pour les top voteurs :", NamedTextColor.YELLOW, TextDecoration.BOLD)
            .newline().append("1er : ", NamedTextColor.YELLOW).append("250 Lys d'or", NamedTextColor.GOLD)
            .newline().append("2e : ", NamedTextColor.YELLOW).append("150 Lys d'or", NamedTextColor.GOLD)
            .newline().append("3e : ", NamedTextColor.YELLOW).append("50 Lys d'or", NamedTextColor.GOLD)

        addButton(
            6, ItemBuilder(Material.DIAMOND)
                .name("Top Voteurs", NamedTextColor.GOLD)
                .lore(lore.get())
                .build()
        )
    }

    fun putPlayerButton() {
        val voteService = ServiceContainer[VoteService::class.java]
        val lore: ItemLoreBuilder = ItemLoreBuilder()
            .newline()
            .append("Aujourd'hui : ", NamedTextColor.YELLOW)
            .append(
                "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.DAILY, playerID, null)}",
                NamedTextColor.GOLD
            )
            .newline()
            .append("Semaine : ", NamedTextColor.YELLOW)
            .append(
                "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.WEEKLY, playerID, null)}",
                NamedTextColor.GOLD
            )
            .newline()
            .append("Mois : ", NamedTextColor.YELLOW)
            .append(
                "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.MONTHLY, playerID, null)}",
                NamedTextColor.GOLD
            )
            .newline()
            .append("Total : ", NamedTextColor.YELLOW)
            .append(
                "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.TOTAL, playerID, null)}",
                NamedTextColor.GOLD
            )


        val pseudo = Bukkit.getOfflinePlayer(playerID).name ?: "??"
        addButton(4, TesseractCorePlugin.instance, async = {
            val playerHead = ServiceContainer[PlayerProfileService::class.java].getPlayerHead(playerID)
            ItemBuilder(playerHead)
                .name(pseudo, NamedTextColor.GOLD)
                .lore(lore.get())
                .build()
        })
    }

    fun putSiteButton(voteSite: VoteSite, remainingDuration: Duration, viewer: Audience, index: Int) {
        val voteService = ServiceContainer[VoteService::class.java]
        val lore: ItemLoreBuilder = ItemLoreBuilder().newline()
        if (remainingDuration.isZero || remainingDuration.isNegative)
            lore.append("Va voter !", NamedTextColor.GREEN)
        else
            lore.append("Temps restant : ", NamedTextColor.GRAY)
                .append(Util.getPrintableDuration(remainingDuration), NamedTextColor.RED)

        lore.newline(2)
            .append("Mes votes ce mois-ci : ", NamedTextColor.GRAY)
                .append(
                    "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.MONTHLY, playerID, voteSite)}",
                    NamedTextColor.GOLD)
            .newline()
            .append("Mes votes (total) : ", NamedTextColor.GRAY)
            .append(
                "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.TOTAL, playerID, voteSite)}",
                NamedTextColor.GOLD
            )
            .newline()
            .horizontalLine(40, NamedTextColor.YELLOW)
            .newline()
            .append("Tous les votes ce mois-ci : ", NamedTextColor.GRAY)
            .append(
                "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.MONTHLY, null, voteSite)}",
                NamedTextColor.GOLD
            )
            .newline()
            .append("Tous les votes (total) : ", NamedTextColor.GRAY)
            .append(
                "${voteService.countVotesDuringPeriod(VoteService.VotePeriod.TOTAL, null, null)}",
                NamedTextColor.GOLD
            )
            .newline(2)
            .append("Clique pour obtenir le lien", NamedTextColor.AQUA)

        val canVote = remainingDuration.isNegative || remainingDuration.isZero
        addButton(
            index, ItemBuilder(if (canVote) Material.EMERALD_BLOCK else Material.STRUCTURE_VOID)
                .name(voteSite.serviceName, NamedTextColor.YELLOW)
                .lore(lore.get())
                .build()
        ) {
            close()
            sendVoteLink(viewer, voteSite)
        }
    }

    fun putAllSitesButton(remainingDurations: Map<VoteSite, Duration>, viewer: Audience) {
        val lore = ItemLoreBuilder()
        remainingDurations.forEach { (voteSite, duration) ->
            lore.newline()
                .append(voteSite.serviceName, NamedTextColor.YELLOW)
                .append(" : ", NamedTextColor.GRAY)
            if (duration.isZero || duration.isNegative)
                lore.append("Va voter !", NamedTextColor.GREEN)
            else
                lore.append(Util.getPrintableDuration(duration), NamedTextColor.RED)
        }
        lore.newline(2)
            .append("Clique pour obtenir les liens", NamedTextColor.AQUA)

        addButton(
            9, ItemBuilder(Material.COMMAND_BLOCK)
                .name("Tous les sites", NamedTextColor.GOLD)
                .lore(lore.get())
                .build()
        ) {
            close()
            sendVoteLinks(viewer)
        }
    }

    fun sendVoteLinks(viewer: Audience) {
        ServiceContainer[VoteService::class.java].getVoteSites().forEach { sendVoteLink(viewer, it) }
    }

    fun sendVoteLink(viewer: Audience, site: VoteSite) {
        viewer.sendMessage(
            Component.text(site.serviceName, NamedTextColor.YELLOW)
                .append(Component.text(" : ", NamedTextColor.GRAY))
                .append(
                    Component.text(site.address, NamedTextColor.GOLD)
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, site.address))
                )
        )
    }
}