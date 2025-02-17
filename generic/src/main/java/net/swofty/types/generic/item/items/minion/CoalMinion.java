package net.swofty.types.generic.item.items.minion;

import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Minion;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.user.statistics.ItemStatistics;

public class CoalMinion implements CustomSkyBlockItem, Minion {
    @Override
    public MinionRegistry getMinionRegistry() {
        return MinionRegistry.COAL;
    }

    @Override
    public ItemType getBaseCraftMaterial() {
        return ItemType.COAL;
    }

    @Override
    public ItemType getEnchantedCraftMaterial() {
        return ItemType.ENCHANTED_COAL;
    }

    @Override
    public ItemType getFirstBaseItem() {
        return ItemType.WOODEN_PICKAXE;
    }

}
