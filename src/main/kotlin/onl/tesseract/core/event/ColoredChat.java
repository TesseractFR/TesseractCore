package onl.tesseract.core.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ColoredChat implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event)
    {
        if (event.isCancelled()) return;
        if (event.getPlayer().hasPermission("tesseract.chat.color") && event.message() instanceof TextComponent textComponent)
            event.message(colorMessage(textComponent));
    }

    public static TextComponent colorMessage(TextComponent message)
    {
        var coloredString = message.content().replaceAll("(&)([0-9a-fklnorm])", "§$2");
        return message.content(coloredString);
    }

    public static Component colorComponent(Component message)
    {
        if (message instanceof TextComponent text)
        {
            message = colorMessage(text);
        }
        return message;
    }

    public static String colorMessage(String message)
    {
        return message.replaceAll("(&)([0-9a-fklnorm])", "§$2");
    }
}