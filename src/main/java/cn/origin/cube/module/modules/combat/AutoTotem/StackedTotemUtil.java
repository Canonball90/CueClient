package cn.origin.cube.module.modules.combat.AutoTotem;

import cn.origin.cube.utils.player.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.util.Date;

public class StackedTotemUtil {

    static final Minecraft mc = Minecraft.getMinecraft();
    static AutoTote at = new AutoTote();
    public static boolean shouldPrepare = false;
    static int totems;
    static long selectLast = new Date().getTime();
    static long swapLast = new Date().getTime();
    static long replaceLast = new Date().getTime();

    public static void doStack(){
        try {
            if (mc.player.getHeldItemOffhand().getCount() <= at.getSwitchCount().getValue() + at.getPrepareCount().getValue()) {
                shouldPrepare = true;
            }

            if (mc.player.getHeldItemOffhand().getCount() > at.getSwitchCount().getValue() + at.getPrepareCount().getValue()) {
                shouldPrepare = false;
            }
            totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();

            if ((mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING && mc.player.getHeldItemOffhand().getCount() <= at.getSwitchCount().getValue()) || mc.player.getHeldItemOffhand().isEmpty()) {
                final int slot = InventoryUtil.findWindowItem(Items.TOTEM_OF_UNDYING, at.getMinCount().getValue().intValue(), 9, 44);

                if (slot == - 1) return;
                if (mc.currentScreen instanceof GuiContainer) mc.player.closeScreen();

                if (mc.player.inventoryContainer.getSlot(slot).getStack().getItem() != Items.TOTEM_OF_UNDYING) return;
                if (mc.player.inventoryContainer.getSlot(slot).getStack().getCount() <= at.getMinCount().getValue())
                    return;

                if (new Date().getTime() >= selectLast + at.getDelay().getValue()) {
                    selectLast = new Date().getTime();
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 8, ClickType.SWAP, mc.player);
                }

                if (new Date().getTime() >= swapLast + at.getDelay2().getValue()) {
                    swapLast = new Date().getTime();
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 8, ClickType.SWAP, mc.player);
                }

                if (new Date().getTime() >= replaceLast + at.getDelay2().getValue() / 3) {
                    replaceLast = new Date().getTime();
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 8, ClickType.SWAP, mc.player);
                }

                shouldPrepare = false;
            }
        } catch (Exception e) {

        }
    }
}
