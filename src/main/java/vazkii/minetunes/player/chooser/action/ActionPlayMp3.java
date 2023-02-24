package vazkii.minetunes.player.chooser.action;

import java.io.File;

import cn.origin.cube.Cube;
import vazkii.minetunes.playlist.MP3Metadata;

public class ActionPlayMp3 implements ISelectorAction {

	public static final ActionPlayMp3 instance = new ActionPlayMp3();
	
	String lastFile = ""; 
	
	@Override
	public void select(File file) {
		lastFile = file.getAbsolutePath();
		play(file);
	}
	
	public void play(MP3Metadata meta) {
		if(Cube.musicPlayerThread == null)
			Cube.startMusicPlayerThread();

		Cube.musicPlayerThread.play(meta);
		ActionDebug.instance.select(meta.file);
	}
	
	public void play(File file) {
		try {
			play(new MP3Metadata(0, file));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playLast() {
		File file = new File(lastFile);
		if(file.exists() && file.getName().endsWith(".mp3"))
			play(file);
	}

}
