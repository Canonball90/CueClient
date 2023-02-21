package cn.origin.cube.guis.alt.manage;

import cn.origin.cube.core.plugin.ListManager;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import cn.origin.cube.guis.alt.Alt;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

public class AltManager extends ListManager<Alt> {

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Path altsFile;

    public AltManager(Path directory) {
        altsFile = Paths.get(directory.toString() + File.separator + "alts.json");
    }

    public void init() {
        try {
            if (!Files.exists(altsFile)) {
                Files.createFile(altsFile);
                save();
                return;
            }
            load();
        } catch (IOException ignored) {

        }
    }

    public void save() {
        try {
            final JsonObject object = new JsonObject();
            getContents().forEach(alt -> {
                JsonObject altObject = new JsonObject();
                altObject.add("username", new JsonPrimitive(alt.getUsername() != null ? alt.getUsername() : alt.getEmail()));
                altObject.add("password", new JsonPrimitive(alt.getPassword()));

                object.add(alt.getEmail(), altObject);
            });
            Files.write(altsFile, GSON.toJson(object).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            final JsonObject object = new JsonParser().parse(new JsonReader(Files.newBufferedReader(altsFile))).getAsJsonObject();
            final Set<Map.Entry<String, JsonElement>> elements = object.entrySet();
            elements.forEach(entry -> {
                JsonObject alt = entry.getValue().getAsJsonObject();
                getContents().add(new Alt(alt.get("username").getAsString(), entry.getKey(), alt.get("password").getAsString()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(String username) {
        for (Alt alt : getContents()) {
            if (alt.getEmail().equalsIgnoreCase(username)) {
                getContents().remove(alt);
            }
        }
    }

}
