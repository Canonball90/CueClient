package cn.origin.cube.viaforge.platform;

import cn.origin.cube.viaforge.handler.CommonTransformer;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.libs.gson.JsonObject;
import cn.origin.cube.viaforge.ViaForge;

public class Injector implements ViaInjector {

    @Override
    public void inject() {
    }

    @Override
    public void uninject() {
    }

    @Override
    public int getServerProtocolVersion() {
        return ViaForge.SHARED_VERSION;
    }

    @Override
    public String getEncoderName() {
        return CommonTransformer.HANDLER_ENCODER_NAME;
    }

    @Override
    public String getDecoderName() {
        return CommonTransformer.HANDLER_DECODER_NAME;
    }

    @Override
    public JsonObject getDump() {
        JsonObject obj = new JsonObject();
        return obj;
    }
}
