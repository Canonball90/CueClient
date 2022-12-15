// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.utils.render.advancedRender.misc.parsers.util;

import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.ArrayList;

public final class FileSystemHelper
{
    public static String getSeparator() {
        final String sep = System.getProperty("file.separator");
        return (sep == null || sep.isEmpty()) ? "/" : sep;
    }
    
    public static String getMinecraftFolder() {
        final String os = System.getProperty("os.name").toLowerCase();
        String home = System.getProperty("user.home");
        final String sep = getSeparator();
        home += sep;
        if (os.contains("win")) {
            return home + "AppData/Roaming/.minecraft/";
        }
        if (os.contains("mac") || os.contains("darwin")) {
            return home + "Library/Application Support/minecraft/";
        }
        if (os.contains("linux")) {
            return home + ".config/.minecraft/";
        }
        return null;
    }
    
    public static void zip(final String dir, final ArrayList<File> files) {
        final File outputFile = new File(dir);
        try {
            final FileOutputStream outputStream = new FileOutputStream(outputFile);
            final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            for (final File file : files) {
                writeFileToZip(zipOutputStream, file);
            }
            zipOutputStream.close();
        }
        catch (Exception ex) {}
    }
    
    private static void writeFileToZip(final ZipOutputStream stream, final File file) {
        try {
            final FileInputStream inputStream = new FileInputStream(file);
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 1024);
            final ZipEntry entry = new ZipEntry(file.getName());
            stream.putNextEntry(entry);
            final byte[] data = new byte[1024];
            int i;
            while ((i = bufferedInputStream.read(data, 0, 1024)) != -1) {
                stream.write(data, 0, i);
            }
            stream.closeEntry();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
