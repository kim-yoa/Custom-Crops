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

package net.momirealms.customcrops.api.core.block;

import net.momirealms.customcrops.api.BukkitCustomCropsPlugin;
import net.momirealms.customcrops.api.core.BuiltInBlockMechanics;
import net.momirealms.customcrops.api.core.ConfigManager;
import net.momirealms.customcrops.api.core.world.CustomCropsBlockState;
import net.momirealms.customcrops.api.core.world.CustomCropsWorld;
import net.momirealms.customcrops.api.core.world.Pos3;
import net.momirealms.customcrops.api.core.wrapper.WrappedBreakEvent;
import net.momirealms.customcrops.api.core.wrapper.WrappedInteractEvent;
import net.momirealms.customcrops.api.core.wrapper.WrappedPlaceEvent;
import net.momirealms.customcrops.api.event.ScarecrowBreakEvent;
import net.momirealms.customcrops.api.event.ScarecrowInteractEvent;
import net.momirealms.customcrops.api.event.ScarecrowPlaceEvent;
import net.momirealms.customcrops.api.util.EventUtils;

import java.util.Optional;

public class ScarecrowBlock extends AbstractCustomCropsBlock {

    public ScarecrowBlock() {
        super(BuiltInBlockMechanics.SCARECROW.key());
    }

    @Override
    public void randomTick(CustomCropsBlockState state, CustomCropsWorld<?> world, Pos3 location) {
        //tickScarecrow(world, location);
    }

    @Override
    public void scheduledTick(CustomCropsBlockState state, CustomCropsWorld<?> world, Pos3 location) {
        tickScarecrow(world, location);
    }

    private void tickScarecrow(CustomCropsWorld<?> world, Pos3 location) {
        if (!ConfigManager.doubleCheck()) return;
        String id = BukkitCustomCropsPlugin.getInstance().getItemManager().id(location.toLocation(world.bukkitWorld()), ConfigManager.scarecrowExistenceForm());
        if (ConfigManager.scarecrow().contains(id)) return;
        // remove outdated data
        BukkitCustomCropsPlugin.getInstance().getPluginLogger().warn("Scarecrow is removed at location[" + world.worldName() + "," + location + "] because the id of the block/furniture is [" + id + "]");
        world.removeBlockState(location);
    }

    @Override
    public void onInteract(WrappedInteractEvent event) {
        CustomCropsWorld<?> world = event.world();
        Pos3 pos3 = Pos3.from(event.location());
        CustomCropsBlockState state = getOrFixState(world, pos3);
        ScarecrowInteractEvent interactEvent = new ScarecrowInteractEvent(event.player(), event.itemInHand(), event.location(), event.relatedID(), state, event.hand());
        EventUtils.fireAndForget(interactEvent);
    }

    @Override
    public void onBreak(WrappedBreakEvent event) {
        CustomCropsWorld<?> world = event.world();
        Pos3 pos3 = Pos3.from(event.location());
        CustomCropsBlockState state = getOrFixState(world, pos3);
        ScarecrowBreakEvent breakEvent = new ScarecrowBreakEvent(event.entityBreaker(), event.blockBreaker(), event.location(), event.brokenID(), state, event.reason());
        if (EventUtils.fireAndCheckCancel(breakEvent)) {
            return;
        }
        world.removeBlockState(pos3);
    }

    @Override
    public void onPlace(WrappedPlaceEvent event) {
        CustomCropsBlockState state = createBlockState();
        ScarecrowPlaceEvent placeEvent = new ScarecrowPlaceEvent(event.player(), event.location(), event.itemID(), state);
        if (EventUtils.fireAndCheckCancel(placeEvent)) {
            return;
        }
        Pos3 pos3 = Pos3.from(event.location());
        CustomCropsWorld<?> world = event.world();
        world.addBlockState(pos3, state).ifPresent(previous -> {
            BukkitCustomCropsPlugin.getInstance().debug(
                    "Overwrite old data with " + state +
                            " at location[" + world.worldName() + "," + pos3 + "] which used to be " + previous
            );
        });
    }

    @Override
    public boolean isBlockInstance(String id) {
        return ConfigManager.scarecrow().contains(id);
    }

    public CustomCropsBlockState getOrFixState(CustomCropsWorld<?> world, Pos3 pos3) {
        Optional<CustomCropsBlockState> optional = world.getBlockState(pos3);
        if (optional.isPresent() && optional.get().type() instanceof ScarecrowBlock) {
            return optional.get();
        }
        CustomCropsBlockState state = createBlockState();
        world.addBlockState(pos3, state).ifPresent(previous -> {
            BukkitCustomCropsPlugin.getInstance().debug(
                    "Overwrite old data with " + state +
                            " at pos3[" + world.worldName() + "," + pos3 + "] which used to be " + previous
            );
        });
        return state;
    }
}
