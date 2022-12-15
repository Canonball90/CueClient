// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.utils.render.advancedRender.misc.parsers.util;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public final class JSONParser
{
    public static String get(final String url) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0");
            return convert(new BufferedInputStream(connection.getInputStream()));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String post(final String url, final String data) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "DiscordBot (v1.0.0, http://github.com)");
            connection.setRequestMethod("POST");
            final OutputStream stream = connection.getOutputStream();
            stream.write(data.getBytes(StandardCharsets.UTF_8));
            stream.close();
            connection.disconnect();
            return convert(new BufferedInputStream(connection.getInputStream()));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String sendFile(final String url, final File file) {
        try {
            final HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            final String boundary = "===" + System.currentTimeMillis() + "===";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setRequestProperty("User-Agent", "DiscordBot (v1.0.0, http://github.com)");
            connection.setReadTimeout(5000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            final MultipartForm form = new MultipartForm(boundary, connection.getOutputStream());
            form.addFile("fileName", file);
            form.end();
            connection.disconnect();
            return convert(new BufferedInputStream(connection.getInputStream()));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Matcher getValue(final String json, final String key) {
        final Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^,]*)\",");
        final Matcher matcher = pattern.matcher(json);
        matcher.find();
        return matcher;
    }
    
    private static String convert(final InputStream stream) {
        final Scanner scanner = new Scanner(stream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "/";
    }
}
