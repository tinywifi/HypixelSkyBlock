package net.swofty.types.generic.item.items.weapon.vanilla;

import net.swofty.types.generic.item.ReforgeType;
import net.swofty.types.generic.item.impl.Reforgable;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.groups.EnchantItemGroups;
import net.swofty.types.generic.item.impl.CustomSkyBlockItem;
import net.swofty.types.generic.item.impl.Enchantable;
import net.swofty.types.generic.item.impl.ExtraRarityDisplay;

import java.util.List;

public class IronSword implements CustomSkyBlockItem, Enchantable, ExtraRarityDisplay, Reforgable {
    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, 30D)
                .build();
    }

    @Override
    public boolean showEnchantLores() {
        return true;
    }

    @Override
    public List<EnchantItemGroups> getEnchantItemGroups() {
        return List.of(EnchantItemGroups.SWORD);
    }

    @Override
    public String getExtraRarityDisplay() {
        return " SWORD";
    }

    @Override
    public ReforgeType getReforgeType() {
        return ReforgeType.SWORDS;
    }
}

