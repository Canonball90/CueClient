package vazkii.minetunes.key.handler;

import cn.origin.cube.Cube;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import vazkii.minetunes.key.KeyHandler;
import vazkii.minetunes.key.KeySubscriber;
import vazkii.minetunes.player.HUDHandler;

public class HandlerVolume extends KeyHandler {

	boolean down = false;
	
	boolean positive = false;
	
	public HandlerVolume(boolean positive) {
		this.positive = positive;
	}
	
	@Override
	public void keyDown(KeyBinding key) {
		if(isInValidGui() && Cube.musicPlayerThread != null)
			if(GuiScreen.isCtrlKeyDown()) {
				if(!down) {
					if(positive)
						Cube.musicPlayerThread.prev();
					else Cube.musicPlayerThread.next();
				}
			} else {
				float gainVal = 0.5F * KeySubscriber.delta;
				Cube.musicPlayerThread.addGain(positive ? -gainVal : gainVal);
				HUDHandler.showVolume = true;
			}
		
		down = true;
	}

	@Override
	public void keyUp(KeyBinding key) {
		down = false;
	}

}
