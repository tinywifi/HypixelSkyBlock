package net.swofty.types.generic.item.items.minion;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class SnowMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.SNOW;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.SNOW_BLOCK;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_SNOW_BLOCK;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.WOODEN_PICKAXE;
    }

}
