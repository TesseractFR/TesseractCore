package onl.tesseract.core.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerSitEvent extends Event implements Cancellable {
    private boolean isCancelled = false;
    static private final HandlerList handlerList = new HandlerList();

    private Player player;
    private Location location;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public float getRotation() {
        return rotation;
    }

    private float rotation;

    public PlayerSitEvent(Player player, Location location, float rotation)
    {
        this.player = player;
        this.location = location;
        this.rotation = rotation;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }

    static public HandlerList getHandlerList() {
        return handlerList;
    }
}
