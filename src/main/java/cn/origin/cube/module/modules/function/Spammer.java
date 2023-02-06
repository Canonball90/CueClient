package cn.origin.cube.module.modules.function;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.utils.client.event.concurrent.repeat.RepeatUnit;
import cn.origin.cube.utils.client.event.event.Parallel;
import net.minecraft.network.play.client.CPacketChatMessage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static cn.origin.cube.utils.client.event.concurrent.ConcurrentTaskManager.runRepeat;

@Parallel
@Constant(constant = false)
@ModuleInfo(name = "Spammer", descriptions = "Prevents you from taking fall damage", category = Category.FUNCTION)
public class Spammer extends Module {

    IntegerSetting delay = registerSetting("Delay", 10, 1, 100);
    BooleanSetting randomSuffix = registerSetting("Random", false);
    BooleanSetting greenText = registerSetting("GreenText", false);

    private static final String fileName = "Cue/spammer/Spammer.txt";
    private static final String defaultMessage = "hello world";
    private static final List<String> spamMessages = new ArrayList<>();
    private static final Random rnd = new Random();

    RepeatUnit fileChangeListener = new RepeatUnit(1000, this::readSpamFile);

    RepeatUnit runner = new RepeatUnit(() -> delay.getValue() * 1000, () -> {
        if (mc.player == null) {
            disable();
        } else if (spamMessages.size() > 0) {
            String messageOut;
            if (randomSuffix.getValue()) {
                int index = rnd.nextInt(spamMessages.size());
                messageOut = spamMessages.get(index);
                spamMessages.remove(index);
            } else {
                messageOut = spamMessages.get(0);
                spamMessages.remove(0);
            }
            spamMessages.add(messageOut);
            if (this.greenText.getValue()) {
                messageOut = "> " + messageOut;
            }
            mc.player.connection.sendPacket(new CPacketChatMessage(messageOut.replaceAll("\u00a7", "")));
        }
    });

    public Spammer() {
        runner.suspend();
        runRepeat(runner);
        runRepeat(fileChangeListener);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            this.disable();
            return;
        }
        runner.resume();
        readSpamFile();
    }

    @Override
    public void onDisable() {
        spamMessages.clear();
        runner.suspend();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void readSpamFile() {
        File file = new File(fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception ignored) {
            }
        }
        List<String> fileInput = readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        spamMessages.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            spamMessages.add(s);
        }

        if (spamMessages.size() == 0) {
            spamMessages.add(defaultMessage);
        }
    }

    public static List<String> readTextFileAllLines(String file) {
        try {
            Path path = Paths.get(file);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            System.out.println("WARNING: Unable to read file, creating new file: " + file);
            appendTextFile("", file);
            return Collections.emptyList();
        }
    }

    public static boolean appendTextFile(String data, String file) {
        try {
            Path path = Paths.get(file);
            Files.write(path, Collections.singletonList(data), StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }

}
