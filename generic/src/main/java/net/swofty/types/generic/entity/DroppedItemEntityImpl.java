package net.swofty.types.generic.entity;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.item.ItemMeta;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DroppedItemEntityImpl extends Entity {
    @Getter
    private static Map<SkyBlockPlayer, List<DroppedItemEntityImpl>> droppedItems = new HashMap<>();
    private final SkyBlockPlayer player;
    private final long endPickupDelay;

    public DroppedItemEntityImpl(SkyBlockItem item, SkyBlockPlayer player) {
        super(EntityType.ITEM);

        this.player = player;
        this.endPickupDelay = System.currentTimeMillis() + 500;

        ItemEntityMeta meta = (ItemEntityMeta) this.entityMeta;
        meta.setItem(item.getItemStack());

        setAutoViewable(false);

        this.scheduleRemove(Duration.ofSeconds(60));
        this.addViewer(player);

        droppedItems.computeIfPresent(player, (key, value) -> {
            if (value.size() > 50) {
                value.get(0).remove();
            }
            value.add(this);
            return value;
        });
        droppedItems.putIfAbsent(player, new ArrayList<>(List.of(this)));
    }

    public SkyBlockItem getItem() {
        return new SkyBlockItem(((ItemEntityMeta) this.entityMeta).getItem());
    }
}
