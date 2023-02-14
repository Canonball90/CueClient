package cn.origin.cube.module.modules.function.Inv;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.module.modules.client.ClickGui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;

@Constant
@ModuleInfo(name = "InventoryManager", descriptions = "", category = Category.FUNCTION)
public class InvManager extends Module {

    public IntegerSetting cap = registerSetting("Block Cap", 128, 8, 256);
    public final FloatSetting delay1 = registerSetting("Sort Delay", Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(10.0f));
    public BooleanSetting archer = registerSetting( "Archer", false);
    public BooleanSetting food = registerSetting( "Food", false);
    public BooleanSetting sword = registerSetting( "Sword", true);
    public BooleanSetting cleaner = registerSetting( "Inv Cleaner", true);
    public BooleanSetting openinv = registerSetting( "Open Inv", true);

    public static InvManager INSTANCE;

    public InvManager() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        long delay = (long) (delay1.getValue() * 50);
        if (!(mc.currentScreen instanceof GuiInventory) && (openinv.getValue()))
            return;
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat) {
            if (utils.timer.passedMs(delay) && utils.weaponSlot >= 36) {
                if (!mc.player.inventoryContainer.getSlot(utils.weaponSlot).getHasStack()) {
                    utils.getBestWeapon(utils.weaponSlot);
                } else {
                    if (!utils.isBestWeapon(mc.player.inventoryContainer.getSlot(utils.weaponSlot).getStack())) {
                        utils.getBestWeapon(utils.weaponSlot);
                    }
                }
            }
            if (utils.timer.passedMs(delay) && utils.pickaxeSlot >= 36) {
                utils.getBestPickaxe();
            }
            if (utils.timer.passedMs(delay) && utils.shovelSlot >= 36) {
                utils.getBestShovel();
            }
            if (utils.timer.passedMs(delay) && utils.axeSlot >= 36) {
                utils.getBestAxe();
            }
            if (utils.timer.passedMs(delay) && cleaner.getValue()) {
                for (int i = 9; i < 45; i++) {
                    if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                        if (utils.shouldDrop(is, i)) {
                            utils.drop(i);
                            if (delay == 0) {
                                mc.player.closeScreen();
                            }
                            utils.timer.reset();
                            if (delay > 0)
                                break;
                        }
                    }
                }
            }
        }
    }

}
