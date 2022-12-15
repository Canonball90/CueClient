// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.utils.render.advancedRender.misc.parsers;

import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.FileSystemHelper;

public interface JSONObject
{
    public static final String sep = FileSystemHelper.getSeparator();
    public static final String mcFolder = FileSystemHelper.getMinecraftFolder();
    public static final String home = System.getProperty("user.home");
    
    void handle();
    
    String getName();
}
