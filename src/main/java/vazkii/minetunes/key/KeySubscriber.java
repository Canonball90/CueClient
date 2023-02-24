package vazkii.minetunes.key;

import cn.origin.cube.Cube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import vazkii.minetunes.player.HUDHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class KeySubscriber {

	public static final KeySubscriber instance = new KeySubscriber();
	
	public static float delta = 0;
	float lastPartTicks = 0;
	
	@SubscribeEvent
	public void playerTick(RenderTickEvent event) {
		delta = event.renderTickTime - lastPartTicks;
		if(delta < 0)
			delta = event.renderTickTime;
		lastPartTicks = event.renderTickTime;
		
		if(Minecraft.getMinecraft().player != null) {
			if(event.phase == Phase.START) {
				for(KeyBinding key : KeyBindings.handlers.keySet()) {
					KeyHandler handler = KeyBindings.handlers.get(key);
					if(key.isKeyDown())
						handler.keyDown(key);
					else handler.keyUp(key);
				}
			} else HUDHandler.showVolume = false;
		} else if(Cube.musicPlayerThread != null) {
			Cube.musicPlayerThread.forceKill();
			Cube.startMusicPlayerThread();
		}
	}
	
}
