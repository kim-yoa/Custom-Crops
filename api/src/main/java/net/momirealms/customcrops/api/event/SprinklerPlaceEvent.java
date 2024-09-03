/*
 *  Copyright (C) <2024> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customcrops.api.event;

import net.momirealms.customcrops.api.core.mechanic.sprinkler.SprinklerConfig;
import net.momirealms.customcrops.api.core.world.CustomCropsBlockState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;


/**
 * An event that is triggered when a player places a sprinkler in the CustomCrops plugin.
 */
public class SprinklerPlaceEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final ItemStack itemInHand;
    private final Location location;
    private final SprinklerConfig config;
    private final CustomCropsBlockState blockState;
    private final EquipmentSlot hand;

    /**
     * Constructor for the SprinklerPlaceEvent.
     *
     * @param who        The player who is placing the sprinkler.
     * @param itemInHand The ItemStack representing the item in the player's hand.
     * @param hand       The hand (main or offhand) used by the player to place the sprinkler.
     * @param location   The location where the sprinkler is being placed.
     * @param config     The configuration of the sprinkler being placed.
     * @param blockState The state of the block where the sprinkler is placed.
     */
    public SprinklerPlaceEvent(
            @NotNull Player who,
            @NotNull ItemStack itemInHand,
            @NotNull EquipmentSlot hand,
            @NotNull Location location,
            @NotNull SprinklerConfig config,
            CustomCropsBlockState blockState
    ) {
        super(who);
        this.itemInHand = itemInHand;
        this.location = location;
        this.config = config;
        this.hand = hand;
        this.blockState = blockState;
    }

    /**
     * Returns whether the event is cancelled.
     *
     * @return true if the event is cancelled, false otherwise.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled state of the event.
     *
     * @param cancel true to cancel the event, false otherwise.
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Gets the list of handlers for this event.
     *
     * @return the static handler list.
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets the list of handlers for this event instance.
     *
     * @return the handler list.
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    /**
     * Gets the ItemStack representing the item in the player's hand.
     *
     * @return the item in hand.
     */
    @NotNull
    public ItemStack getItemInHand() {
        return itemInHand;
    }

    /**
     * Gets the state of the block where the sprinkler is placed.
     *
     * @return the block state of the sprinkler.
     */
    @NotNull
    public CustomCropsBlockState getBlockState() {
        return blockState;
    }

    /**
     * Gets the location where the sprinkler is being placed.
     *
     * @return the location of the sprinkler.
     */
    @NotNull
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the configuration of the sprinkler being placed.
     *
     * @return the sprinkler configuration.
     */
    @NotNull
    public SprinklerConfig getSprinklerConfig() {
        return config;
    }

    /**
     * Gets the hand (main or offhand) used by the player to place the sprinkler.
     *
     * @return the equipment slot representing the hand used.
     */
    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }
}