package vazkii.minetunes.key.handler;

import cn.origin.cube.Cube;
import net.minecraft.client.settings.KeyBinding;
import vazkii.minetunes.key.KeyHandler;

public class HandlerPlayPause extends KeyHandler {

	boolean down = false;
	
	@Override
	public void keyDown(KeyBinding key) {
		if(!down && isInValidGui()) {
			if(Cube.musicPlayerThread != null)
				Cube.musicPlayerThread.pauseOrPlay();
			else {
				Cube.startMusicPlayerThread();
				Cube.musicPlayerThread.next();
			}
		}
			
		
		down = true;
	}

	@Override
	public void keyUp(KeyBinding key) {
		down = false;
	}

}
