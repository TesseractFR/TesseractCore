package onl.tesseract.core.command.staff;

import onl.tesseract.core.cosmetics.CosmeticManager;
import onl.tesseract.core.cosmetics.Cosmetic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CosmeticCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command,
                                                @NotNull String s, @NotNull String[] args)
    {
        if(!commandSender.hasPermission("cosmetic.admin"))return null;
        if (args.length == 1)
            return Stream.of("give", "remove").filter(sub -> sub.startsWith(args[0])).collect(Collectors.toList());
        if (args.length == 3)
        {
            return CosmeticManager.getTypes()
                         .stream().filter(sub -> sub.startsWith(args[2])).collect(Collectors.toList());

        }
        if (args.length == 4){
            return CosmeticManager.getCosmetics(args[2]).stream().map(Cosmetic::toString)
                                  .filter(sub -> sub.startsWith(args[3])).collect(Collectors.toList());
        }
        return null;
    }
}
