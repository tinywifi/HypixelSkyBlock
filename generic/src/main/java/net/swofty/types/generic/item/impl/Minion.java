package net.swofty.types.generic.item.impl;

import com.mongodb.lang.Nullable;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.recipe.Recipe;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.item.impl.recipes.ShapedRecipe;
import net.swofty.types.generic.item.impl.recipes.ShapelessRecipe;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.MinionRecipe;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.user.statistics.ItemStatistics;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface Minion extends CustomSkyBlockItem, SkullHead, Placeable, Unstackable, Craftable {
    MinionRegistry getMinionRegistry();

    ItemType getBaseCraftMaterial();
    ItemType getEnchantedCraftMaterial();
    ItemType getFirstBaseItem();

    default List<SkyBlockRecipe<?>> getRecipes() {
        List<SkyBlockRecipe<?>> toReturn = new ArrayList<>();

        getMinionRegistry().asSkyBlockMinion().getTiers().forEach(tier -> {
            List<String> pattern = new ArrayList<>(Arrays.asList(
                    "AAA",
                    "ABA",
                    "AAA"
            ));

            SkyBlockItem item = new SkyBlockItem(getMinionRegistry().getItemType());
            item.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(tier.tier(), 0));

            ShapedRecipe recipe = new ShapedRecipe(
                    SkyBlockRecipe.RecipeType.MINION,
                    item,
                    MinionRecipe.fromNumber(tier.tier() - 1).getRecipeFunction().apply(new MinionRecipe.MinionRecipeData(
                            getBaseCraftMaterial(),
                            getEnchantedCraftMaterial(),
                            getFirstBaseItem(),
                            getMinionRegistry().getItemType()
                    )),
                    pattern
            );
            recipe.addExtraRequirement('B', (minionItem) -> {
                if (minionItem.getGenericInstance() instanceof Minion)
                    return minionItem.getAttributeHandler().getMinionData().tier() == Math.max(1, tier.tier() - 1);
                return true;
            });

            toReturn.add(recipe);
        });

        return toReturn;
    }

    @Override
    default void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        if (!SkyBlockConst.isIslandServer()) {
            player.sendMessage("§cYou can only place minions on your island!");
            event.setCancelled(true);
            return;
        }

        DatapointMinionData.ProfileMinionData playerData = player.getDataHandler().get(
                DataHandler.Data.MINION_DATA, DatapointMinionData.class
        ).getValue();

        int slots = playerData.getSlots();

        IslandMinionData minionData = player.getSkyBlockIsland().getMinionData();

        if (minionData.getMinions().size() == slots) {
            player.sendMessage("§cYou have reached the maximum amount of minions you can place!");
            event.setCancelled(true);
            return;
        }

        IslandMinionData.IslandMinion minion = minionData.initializeMinion(Pos.fromPoint(event.getBlockPosition()),
                getMinionRegistry(),
                item.getAttributeHandler().getMinionData());
        minionData.spawn(minion);

        event.setBlock(Block.AIR);

        player.sendMessage(
                "§bYou placed a minion! (" + minionData.getMinions().size() + "/" + playerData.getSlots() + ")"
        );
    }

    @Override
    default ItemStatistics getStatistics() {
        return ItemStatistics.EMPTY;
    }

    @Override
    default String getSkullTexture(SkyBlockPlayer player, SkyBlockItem item) {
        int tier = item.getAttributeHandler().getMinionData().tier();

        return getMinionRegistry().asSkyBlockMinion().getTiers().get(tier - 1).texture();
    }


    @Override
    default String getAbsoluteName(SkyBlockPlayer player, SkyBlockItem item) {
        return "§9" + getMinionRegistry().getDisplay() + " " +
                StringUtility.getAsRomanNumeral(item.getAttributeHandler().getMinionData().tier());
    }

    @Override
    default List<String> getAbsoluteLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>(Arrays.asList(
                "§7Place this minion and it will start",
                "§7generating and mining " + getMinionRegistry().name().toLowerCase() + "!",
                "§7Requires an open area to place",
                getMinionRegistry().name().toLowerCase() + ". Minions also work",
                "§7you are offline!",
                ""
        ));

        SkyBlockMinion minion = item.getAttributeHandler().getMinionType().asSkyBlockMinion();
        ItemAttributeMinionData.MinionData data = item.getAttributeHandler().getMinionData();
        SkyBlockMinion.MinionTier tier = minion.getTiers().get(data.tier() - 1);

        lore.add("§7Time Between Actions: §a" + tier.timeBetweenActions() + "s");
        lore.add("§7Max Storage: §e" + tier.storage());
        lore.add("§7Resources Generated: §b" + data.generatedResources());

        lore.add(" ");
        lore.add("§9§lRARE");

        return lore;
    }

    @Override
    default SkyBlockRecipe<?> getRecipe() {
        return null;
    }
}
