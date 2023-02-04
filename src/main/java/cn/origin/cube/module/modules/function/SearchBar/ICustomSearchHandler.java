package cn.origin.cube.module.modules.function.SearchBar;

import net.minecraft.item.ItemStack;

public interface ICustomSearchHandler {

    boolean stackMatchesSearchQuery(ItemStack stack, String query, StringMatcher matcher, SearchMethod search);

    interface StringMatcher {
        boolean matches(String str1, String str2);
    }

    interface SearchMethod {
        boolean namesMatch(ItemStack stack, String query);
    }

}
