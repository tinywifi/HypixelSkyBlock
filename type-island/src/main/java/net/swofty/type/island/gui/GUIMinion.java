package net.swofty.type.island.gui;

import net.minestom.server.entity.fakeplayer.FakePlayer;
import net.minestom.server.entity.fakeplayer.FakePlayerController;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointMinionData;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.GUIMinionRecipes;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.MaterialQuantifiable;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class GUIMinion extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final int[] SLOTS = new int[]{
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
            39, 40, 41, 42, 43
    };
    private final IslandMinionData.IslandMinion minion;

    public GUIMinion(IslandMinionData.IslandMinion minion) {
        super(minion.getMinion().getDisplay() + " " + StringUtility.getAsRomanNumeral(minion.getTier()),
                InventoryType.CHEST_6_ROW);

        this.minion = minion;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");

        set(new GUIClickableItem(53) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
                player.addAndUpdateItem(minion.asSkyBlockItem());
                minion.removeMinion();
                player.getSkyBlockIsland().getMinionData().getMinions().remove(minion);

                player.sendMessage("§aYou picked up a minion! You currently have " +
                        player.getSkyBlockIsland().getMinionData().getMinions().size() +
                " out of a maximum of " + player.getDataHandler().get(
                        DataHandler.Data.MINION_DATA,
                        DatapointMinionData.class).getValue().getSlots()
                + " minions placed.");
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPickup Minion", Material.BEDROCK, 1,
                        "§eClick to pickup!");
            }
        });

        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (minion.getItemsInMinion().isEmpty()) {
                    player.sendMessage("§cThis Minion does not have any items stored!");
                    return;
                }

                minion.getItemsInMinion().forEach(item -> {
                    player.addAndUpdateItem(new SkyBlockItem(item.getMaterial(), item.getAmount()));
                });
                minion.setItemsInMinion(new ArrayList<>());

                refreshItems(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCollect All", Material.CHEST, 1,
                        "§eClick to collect all items!");
            }
        });

        set(new GUIClickableItem(3) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aIdeal Layout", Material.REDSTONE_TORCH, 1,
                        "§7View the most efficient spot for this",
                        "§7minion to be placed in.",
                        " ",
                        "§eClick to view!");
            }
        });

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return PlayerItemUpdater.playerUpdate(player, minion.asSkyBlockItem().getItemStack());
            }
        });

        set(new GUIClickableItem(5) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIMinionRecipes(minion.getMinion(), GUIMinion.this).open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<SkyBlockMinion.MinionTier> minionTiers = minion.getMinion().asSkyBlockMinion().getTiers();

                return ItemStackCreator.getStack("§aNext Tier", Material.GOLD_INGOT, 1,
                        "§7View the items required to upgrade",
                        "§7this minion to the next tier.",
                        " ",
                        "§7Time Between Actions: §8" + minionTiers.get(minion.getTier() - 1).timeBetweenActions() + "s"
                             + " §l> §a" + minionTiers.get(minion.getTier()).timeBetweenActions() + "s",
                        "§7Max Storage: §8" + minionTiers.get(minion.getTier() - 1).storage() + " §l> " +
                                "§e" + minionTiers.get(minion.getTier()).storage(),
                        " ",
                        "§eClick to view!");
            }
        });

        SkyBlockMinion.MinionTier minionTier = minion.getMinion().asSkyBlockMinion().getTiers().get(
                minion.getTier() - 1
        );

        int i = 0;
        for (int slot : SLOTS) {
            i++;
            boolean unlocked = minionTier.getSlots() >= i;

            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return unlocked ? ItemStack.builder(Material.AIR) :
                            ItemStackCreator.createNamedItemStack(Material.WHITE_STAINED_GLASS_PANE);
                }
            });
        }
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!player.getSkyBlockIsland().getMinionData().getMinions().contains(minion)) {
            player.closeInventory();
            return;
        }

        SkyBlockMinion.MinionTier minionTier = minion.getMinion().asSkyBlockMinion().getTiers().get(
                minion.getTier() - 1
        );

        int i = 0;
        for (int slot : SLOTS) {
            i++;
            boolean unlocked = minionTier.getSlots() >= i;

            int finalI = i;
            set(new GUIClickableItem(slot) {
                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (!e.getCursorItem().isAir()) {
                        player.sendMessage("§cYou can't put items in this inventory!");

                        e.setCancelled(true);
                        return;
                    }

                    if (!unlocked) {
                        e.setCancelled(true);
                        return;
                    }
                    if (minion.getItemsInMinion().size() < finalI) return;

                    MaterialQuantifiable item = minion.getItemsInMinion().get(finalI - 1);
                    minion.getItemsInMinion().remove(item);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (!unlocked) return ItemStackCreator.createNamedItemStack(Material.WHITE_STAINED_GLASS_PANE);

                    if (minion.getItemsInMinion().size() < finalI) return ItemStack.builder(Material.AIR);

                    MaterialQuantifiable item = minion.getItemsInMinion().get(finalI - 1);
                    SkyBlockItem skyBlockItem = new SkyBlockItem(item.getMaterial(), item.getAmount());

                    return new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                }
            });
        }
    }

    @Override
    public int refreshRate() {
        return 5;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
