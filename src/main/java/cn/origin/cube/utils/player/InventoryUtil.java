package cn.origin.cube.utils.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int findHotbarItem(final Class<?> clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean isBlock(Item item, Class clazz) {
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            return clazz.isInstance(block);
        }
        return false;
    }

    public static void switchToHotbarSlot(final int slot, final boolean silent) {
        if (mc.player == null || mc.world == null || mc.player.inventory == null) {
            return;
        }
        if (mc.player.inventory.currentItem == slot || slot < 0) {
            return;
        }
        if (silent) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.playerController.updateController();
        } else {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
        }
    }

    public static int findHotbarBlock(final Class<?> clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (clazz.isInstance(stack.getItem())) {
                    return i;
                }
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock) stack.getItem()).getBlock();
                    if (clazz.isInstance(block)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static int getItemSlot(Item items) {
        for (int i = 0; i < 36; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == items) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

    public static int findItemInHotbar(Item itemToFind) {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = InventoryUtil.mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            stack.getItem();
            Item item = stack.getItem();
            if (!item.equals((Object)itemToFind)) continue;
            slot = i;
            break;
        }
        return slot;
    }

    public static int findWindowItem(Item item, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; i++) {
            ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (stack.getItem() == item) {
                return i;
            }
        }

        return - 1;
    }

    public static int findWindowItem(Item item, int count, int minimum, int maximum) {
        for (int i = minimum; i <= maximum; i++) {
            ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (stack.getItem() == item && stack.getCount() >= count) {
                return i;
            }
        }

        return - 1;
    }
}
