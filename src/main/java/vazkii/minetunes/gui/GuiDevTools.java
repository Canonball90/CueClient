package vazkii.minetunes.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;

import cn.origin.cube.Cube;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import vazkii.minetunes.player.chooser.FileSelector;
import vazkii.minetunes.player.chooser.action.ActionDebug;
import vazkii.minetunes.player.chooser.action.ActionMakePlaylist;
import vazkii.minetunes.player.chooser.action.ActionPlayMp3;
import vazkii.minetunes.player.chooser.filter.MusicFilter;
import vazkii.minetunes.player.chooser.filter.PlaylistFilter;

public class GuiDevTools extends GuiMineTunes {

	private static final int max = 35;
	private volatile static List<String> debugOut = new ArrayList(max);
			
	Random rand = new Random();
	
	@Override
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(-1, width - 102, height - 22, 100, 20, I18n.format("minetunes.gui.exit")));
		
		buttonList.add(new GuiButton(0, 10, 30, 200, 20, I18n.format("minetunes.guidev.clear")));
		buttonList.add(new GuiButton(1, 10, 55, 200, 20, I18n.format("minetunes.guidev.resetThread")));
		buttonList.add(new GuiButton(2, 10, 80, 200, 20, I18n.format("minetunes.guidev.chooser")));
		buttonList.add(new GuiButton(3, 10, 105, 200, 20, I18n.format("minetunes.guidev.playMp3")));
		buttonList.add(new GuiButton(4, 10, 130, 200, 20, I18n.format("minetunes.guidev.playLast")));
		buttonList.add(new GuiButton(5, 10, 155, 200, 20, I18n.format("minetunes.guidev.playPause")));
		buttonList.add(new GuiButton(6, 10, 180, 200, 20, I18n.format("minetunes.guidev.volumeControl")));
		buttonList.add(new GuiButton(7, 10, 205, 200, 20, I18n.format("minetunes.guidev.generatePlaylist")));

		Cube.DEBUG_MODE = true;
	}
	
	@Override
	public void drawScreen(int mx, int my, float partialTicks) {
		int boxHeight = (buttonList.size() - 1) * 25 + 50; 
		drawBox(-4, -4, 224, boxHeight);
		
		for(int i = 0; i < debugOut.size(); i++)
			fontRenderer.drawStringWithShadow(debugOut.get(debugOut.size() - i - 1), 240, (i + 1) * 10, 0xFFFFFF);

		fontRenderer.drawStringWithShadow(I18n.format("minetunes.guidev.warning1"), 10, 5, 0xFFFFFF);
		fontRenderer.drawStringWithShadow(I18n.format("minetunes.guidev.warning2"), 10, 15, 0xFFFFFF);
		
		super.drawScreen(mx, my, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		switch(button.id) {
		case -1:
			mc.displayGuiScreen(new GuiPlaylistManager());
			break;
		case 0:
			debugOut.clear();
			break;
		case 1:
			if(Cube.musicPlayerThread != null)
				Cube.musicPlayerThread.forceKill();
			Cube.startMusicPlayerThread();
			debugLog("Reset Thread: " + Cube.musicPlayerThread);
			
			break;
		case 2:
			new FileSelector(PlaylistFilter.instance, JFileChooser.FILES_AND_DIRECTORIES, ActionDebug.instance);
			break;
		case 3:
			new FileSelector(MusicFilter.instance, JFileChooser.FILES_ONLY, ActionPlayMp3.instance);
			break;
		case 4:
			ActionPlayMp3.instance.playLast();
			break;
		case 5:
			if(Cube.musicPlayerThread != null)
				Cube.musicPlayerThread.pauseOrPlay();
			break;
		case 6:
			if(Cube.musicPlayerThread != null) {
				float gainVal = 0.5F;
				Cube.musicPlayerThread.addGain(isShiftKeyDown() ? -gainVal : gainVal);
				float gain = Cube.musicPlayerThread.getGain();
				debugLog("Audio Gain: " + gain + " (Relative Volume: " + Cube.musicPlayerThread.getRelativeVolume(gain) + ")");
			}
			break;
		case 7:
			new FileSelector(PlaylistFilter.instance, JFileChooser.FILES_AND_DIRECTORIES, ActionMakePlaylist.instance.withName(randomPlaylistName()));
			break;
		}
	}
	
	private String randomPlaylistName() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		
		String name = "";
		for(int i = 0; i < 6; i++)
			name += chars[rand.nextInt(chars.length)];
		
		return name;
	}
	
	public static void debugLog(String s) {
		debugLog(s, true);
	}
	
	public static void debugLog(String s, boolean print) {
		if(Cube.DEBUG_MODE) {
			debugOut.add(s);
			if(print)
				System.out.println(s);
			
			while(debugOut.size() > max)
				debugOut.remove(0);
		}
	}
	
	public static void logThrowable(Throwable e) {
		e.printStackTrace();
		for(int i = e.getStackTrace().length - 1; i >= 0; i--)
			debugLog(TextFormatting.DARK_RED + e.getStackTrace()[i].toString(), false);
	}
	
}
