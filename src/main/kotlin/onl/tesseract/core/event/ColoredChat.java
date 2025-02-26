package onl.tesseract.core.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColoredChat implements Listener {
    public static final Pattern HEX_PATTERN = Pattern.compile("(&#)([A-Fa-f0-9]{6})");
    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event)
    {
        if (event.isCancelled()) return;
        if (event.getPlayer().hasPermission("tesseract.chat.color") && event.message() instanceof TextComponent textComponent)
            event.message(colorMessage(textComponent));
    }

    public static TextComponent colorMessage(TextComponent message)
    {
        return colorMessage(message.content());
    }

    public static Component colorComponent(Component message)
    {
        if (message instanceof TextComponent text)
        {
            message = colorMessage(text);
        }
        return message;
    }

    public static TextComponent colorMessage(String message)
    {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder s;
        while (matcher.find()) {
            s = new StringBuilder();
            for (char c : matcher.group().toCharArray()) {
                if (c != '#' && c != '&')
                    s.append("§").append(c);
            }
            message = message.replace(matcher.group(), "§x" + s.toString());
        }
        message = message.replaceAll("(&)([0-9a-fklnorm])", "§$2");
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }
}