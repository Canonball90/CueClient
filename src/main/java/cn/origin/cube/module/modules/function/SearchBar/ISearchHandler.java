package cn.origin.cube.module.modules.function.SearchBar;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface ISearchHandler {

    @CapabilityInject(ISearchHandler.class)
    Capability<ISearchHandler> CAPABILITY = null;

    static boolean hasHandler(ItemStack stack) {
        return stack.hasCapability(CAPABILITY, null);
    }

    static ISearchHandler getHandler(ItemStack stack) {
        return stack.getCapability(CAPABILITY, null);
    }

    boolean stackMatchesSearchQuery(String query, ICustomSearchHandler.StringMatcher matcher, ICustomSearchHandler.SearchMethod search);
}
