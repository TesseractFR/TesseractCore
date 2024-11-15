package onl.tesseract.core.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import onl.tesseract.core.afk.AfkManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TesseractPlaceHolder extends PlaceholderExpansion {
    private Plugin plugin;

    public TesseractPlaceHolder(Plugin plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public @NotNull String getIdentifier()
    {
        return "tesseract";
    }

    @Override
    public @NotNull String getAuthor()
    {
        return "tesseract";
    }

    @Override
    public @NotNull String getVersion()
    {
        return "1.0";
    }

    @Override
    public boolean persist()
    {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String params)
    {
        if (p == null)
            return "";
        if (params.equals("isafk"))
        {
            return String.valueOf(AfkManager.getINSTANCE().isAfk(p));
        }

        return null;
    }
}