package onl.tesseract.core.autoChatMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import onl.tesseract.core.TesseractCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AutoChatMessage {
    /**
     * Période d'envoi de message 5*60*20 = 5min
     */
    private static final int PERIOD = (int) (7.5 * 60 * 20);
    /**
     * Instance
     */
    private static AutoChatMessage INSTANCE;
    private final List<Component> messages = new ArrayList<>();
    private int currentMessageIndex = 0;

    public static AutoChatMessage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AutoChatMessage();
        }
        return INSTANCE;
    }

    public void addMessage(Component message) {
        messages.add(message);
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!messages.isEmpty()) {
                    Component message = messages.get(currentMessageIndex++);
                    Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
                    currentMessageIndex %= messages.size();
                }
            }
        }.runTaskTimer(TesseractCorePlugin.instance, 0, PERIOD);
    }
}
