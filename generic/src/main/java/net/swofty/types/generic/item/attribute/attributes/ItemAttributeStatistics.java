package net.swofty.types.generic.item.attribute.attributes;

import net.swofty.types.generic.item.attribute.ItemAttribute;
import net.swofty.types.generic.user.statistics.ItemStatistic;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.MathUtility;

public class ItemAttributeStatistics extends ItemAttribute<ItemStatistics> {

    @Override
    public String getKey() {
        return "statistics";
    }

    @Override
    public ItemStatistics getDefaultValue() {
        return ItemStatistics.builder().build();
    }

    @Override
    public ItemStatistics loadFromString(String string) {
        String[] split = string.split(",");
        return ItemStatistics.builder()
                .with(ItemStatistic.DAMAGE, Double.parseDouble(split[0]))
                .with(ItemStatistic.DEFENSE, Double.parseDouble(split[1]))
                .with(ItemStatistic.HEALTH, Double.parseDouble(split[2]))
                .with(ItemStatistic.INTELLIGENCE, Double.parseDouble(split[3]))
                .with(ItemStatistic.STRENGTH, Double.parseDouble(split[4]))
                .with(ItemStatistic.MINING_SPEED, Double.parseDouble(split[5]))
                .build();
    }

    @Override
    public String saveIntoString() {
        return this.value.get(ItemStatistic.DAMAGE) +
                "," +
                this.value.get(ItemStatistic.DEFENSE) +
                "," +
                this.value.get(ItemStatistic.HEALTH) +
                "," +
                this.value.get(ItemStatistic.INTELLIGENCE) +
                "," +
                this.value.get(ItemStatistic.STRENGTH) +
                "," +
                this.value.get(ItemStatistic.MINING_SPEED);
    }
}
