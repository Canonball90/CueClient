package cn.origin.cube.inject.client;

import cn.origin.cube.core.viaforge.ViaForge;
import cn.origin.cube.core.viaforge.handler.CommonTransformer;
import cn.origin.cube.core.viaforge.handler.DecodeHandler;
import cn.origin.cube.core.viaforge.handler.EncodeHandler;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.NetworkManager$5")
public abstract class MixinNetworkManagerChInit {

    @Inject(method = "initChannel", at = @At(value = "TAIL"), remap = false)
    private void onInitChannel(Channel channel, CallbackInfo ci) {
        if (channel instanceof SocketChannel && ViaForge.getInstance().getVersion() != ViaForge.SHARED_VERSION) {

            UserConnection user = new UserConnectionImpl(channel, true);
            new ProtocolPipelineImpl(user);

            channel.pipeline()
                    .addBefore("encoder", CommonTransformer.HANDLER_ENCODER_NAME, new EncodeHandler(user))
                    .addBefore("decoder", CommonTransformer.HANDLER_DECODER_NAME, new DecodeHandler(user));
        }
    }
}
