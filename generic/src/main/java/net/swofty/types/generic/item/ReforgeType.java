package net.swofty.types.generic.item;

import lombok.Getter;
import net.swofty.types.generic.user.statistics.ItemStatistic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

@Getter
public enum ReforgeType {
    SWORDS(List.of(
            new Reforge("Epic", List.of(
                    new Reforge.ReforgeSet(ItemStatistic.STRENGTH, level -> (double) (10 + (level * 5)))
            ))
    )),
    BOWS(List.of()),
    ARMOR(List.of()),
    EQUIPMENT(List.of()),
    FISHING_RODS(List.of()),
    PICKAXES(List.of(
            new Reforge("Unyielding", List.of(
                    new Reforge.ReforgeSet(ItemStatistic.SPEED, level -> level * 1.15),
                    new Reforge.ReforgeSet(ItemStatistic.MINING_SPEED, Double::valueOf)
            )),
            new Reforge("Excellent", List.of(
                    new Reforge.ReforgeSet(ItemStatistic.SPEED, level -> level * 1.1),
                    new Reforge.ReforgeSet(ItemStatistic.MINING_SPEED, level -> level * 4d)
            ))
    )),
    AXES(List.of()),
    HOES(List.of()),
    VACUUMS(List.of()),
    ;

    private final List<Reforge> reforges;

    ReforgeType(List<Reforge> reforges) {
        this.reforges = reforges;
    }

    public record Reforge(String prefix, List<ReforgeSet> set) {
        public Set<ItemStatistic> getStatistics() {
            return Set.copyOf(set.stream().map(ReforgeSet::statistic).toList());
        }

        public Double getBonusCalculation(ItemStatistic statistic, Integer level) {
            try {
                return set.stream()
                        .filter(reforgeSet -> reforgeSet.statistic() == statistic)
                        .findFirst()
                        .orElseThrow()
                        .bonusCalculation()
                        .apply(level);
            } catch (NoSuchElementException ex) {
                return 0D;
            }
        }

        public record ReforgeSet(ItemStatistic statistic, Function<Integer, Double> bonusCalculation) {
        }
    }
}
