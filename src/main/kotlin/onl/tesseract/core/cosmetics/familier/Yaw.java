package onl.tesseract.core.cosmetics.familier;

import org.bukkit.entity.Player;

public enum Yaw {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public static Yaw getYaw(Player p)
    {
        float yaw = p.getLocation().getYaw();
        if ((yaw > 135 && yaw < 225) || yaw > -135)
        {
            return Yaw.NORTH;
        }
        else if (yaw < 225)
        {
            return Yaw.EAST;
        }
        else if (yaw > 45)
        {
            return Yaw.WEST;
        }
        else
        {
            return Yaw.SOUTH;
        }
    }
}